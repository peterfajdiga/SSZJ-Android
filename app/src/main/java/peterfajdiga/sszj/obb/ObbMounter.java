package peterfajdiga.sszj.obb;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import peterfajdiga.sszj.R;

public class ObbMounter {
    private static final String OBB_URL = "https://github.com/peterfajdiga/SSZJ-Android/releases/download/v1.4/data.obb";
    private static final byte[] OBB_MD5 = new byte[] {
        (byte)0xc3, (byte)0xff, (byte)0x6e, (byte)0x78,
        (byte)0xf1, (byte)0xb9, (byte)0xa9, (byte)0x14,
        (byte)0xd7, (byte)0x63, (byte)0x0b, (byte)0x6a,
        (byte)0xda, (byte)0x1a, (byte)0xa2, (byte)0xd9,
    };

    private final Context context;
    private final File obbFile;

    public ObbMounter(@NonNull final Context context) {
        this.context = context;
        this.obbFile = getObbFile(context);
    }

    @NonNull
    private static File getObbFile(@NonNull final Context context) {
        return new File(context.getObbDir(), "data.obb");
    }

    public synchronized void init(@NonNull final OnObbMountedListener listener) {
        final StorageManager storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        if (storageManager.isObbMounted(obbFile.getPath())) {
            final ObbLoader obbLoader = new ObbLoader(storageManager, obbFile);
            listener.onObbMounted(obbLoader);
            return;
        }

        final DownloadStatus status = findDownload();
        if (status != null) {
            switch (status.status) {
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                case DownloadManager.STATUS_PAUSED: {
                    waitForDownloadAndMount(status.downloadId, listener);
                    return;
                }
                case DownloadManager.STATUS_FAILED:
                case DownloadManager.STATUS_SUCCESSFUL: {
                    break;
                }
            }
        }

        if (!obbFile.exists()) {
            askStartDownload(listener);
            return;
        }

        mount(listener);
    }

    private void mount(@NonNull final OnObbMountedListener listener) {
        final StorageManager storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        final boolean success = storageManager.mountObb(obbFile.getPath(), null, new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(final String path, final int state) {
                super.onObbStateChange(path, state);
                switch (state) {
                    case MOUNTED:
                    case ERROR_ALREADY_MOUNTED: {
                        System.err.println("mounted OBB: " + state);
                        final ObbLoader obbLoader = new ObbLoader(storageManager, obbFile);
                        listener.onObbMounted(obbLoader);
                        break;
                    }
                    case ERROR_INTERNAL: {
                        // OBB file is most likely corrupted
                        if (!checkObbMd5()) {
                            obbFile.delete();
                        }
                        // fallthrough
                    }
                    default: {
                        System.err.println("Failed to mount OBB, state: " + state);
                        listener.onObbFailure();
                    }
                }
            }
        });
        if (!success) {
            listener.onObbFailure();
        }
    }

    private void askStartDownload(@NonNull final OnObbMountedListener listener) {
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!connectivityManager.isActiveNetworkMetered()) {
            final long downloadId = startDownload();
            waitForDownloadAndMount(downloadId, listener);
            return;
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.obb_download_dialog_title);
        dialogBuilder.setMessage(R.string.obb_download_dialog_message);
        dialogBuilder.setPositiveButton(R.string.obb_download_dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final long downloadId = startDownload();
                waitForDownloadAndMount(downloadId, listener);
            }
        });

        dialogBuilder.setNegativeButton(R.string.obb_download_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                listener.onObbFailure();
            }
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    // returns download id
    private long startDownload() {
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(OBB_URL));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(context.getString(R.string.obb_download_title));
        request.setDestinationUri(Uri.fromFile(obbFile));
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true);

        final DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager.enqueue(request);
    }

    private void waitForDownloadAndMount(final long downloadId, @NonNull final OnObbMountedListener listener) {
        final DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        final DownloadManager.Query downloadQuery = new DownloadManager.Query();
        downloadQuery.setFilterById(downloadId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (final InterruptedException e) {
                        return;
                    }

                    final Cursor cursor = downloadManager.query(downloadQuery);
                    if (!cursor.moveToFirst()) {
                        // download was most likely cancelled
                        listener.onObbFailure();
                        return;
                    }
                    assert cursor.getCount() == 1;

                    final int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    final int status = cursor.getInt(statusIndex);
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            mount(listener);
                            return;
                        }
                        case DownloadManager.STATUS_FAILED: {
                            listener.onObbFailure();
                            return;
                        }
                        case DownloadManager.STATUS_RUNNING:
                        case DownloadManager.STATUS_PAUSED: {
                            final int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                            if (bytesTotalIndex == -1) {
                                break;
                            }
                            final int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            final int bytesTotal = cursor.getInt(bytesTotalIndex);
                            final int bytesDownloaded = cursor.getInt(bytesDownloadedIndex);
                            listener.onObbDownloadProgress(bytesDownloaded, bytesTotal);
                            break;
                        }
                    }

                    if (!listener.shouldKeepListening()) {
                        return;
                    }
                }
            }
        }).start();
    }

    private DownloadStatus findDownload() {
        final DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        final DownloadManager.Query downloadQuery = new DownloadManager.Query();
        final Cursor cursor = downloadManager.query(downloadQuery);

        DownloadStatus lastDownloadStatus = null;
        long lastDownloadTime = Long.MIN_VALUE;

        while (cursor.moveToNext()) {
            final String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            final String correctUri = Uri.fromFile(obbFile).toString();
            if (localUri == null || !localUri.equals(correctUri)) {
                continue;
            }

            final long modifiedTime = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));
            if (modifiedTime >= lastDownloadTime) {
                final long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                lastDownloadStatus = new DownloadStatus(downloadId, status);
                lastDownloadTime = modifiedTime;
            }
        }
        return lastDownloadStatus;
    }

    private static class DownloadStatus {
        long downloadId;
        int status;

        DownloadStatus(final long downloadId, final int status) {
            this.downloadId = downloadId;
            this.status = status;
        }
    }

    public interface OnObbMountedListener {
        void onObbMounted(ObbLoader obbLoader);
        void onObbFailure();
        void onObbDownloadProgress(int bytesDownloaded, int bytesTotal);
        boolean shouldKeepListening();
    }

    private boolean checkObbMd5() {
        try {
            final byte[] actualMd5 = getMd5(obbFile);
            return Arrays.equals(actualMd5, OBB_MD5);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: speed up, do on its own thread or avoid calculating md5 hash
    private static byte[] getMd5(@NonNull final File file) throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new FileInputStream(file);

        final byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            digest.update(buffer, 0, bytesRead);
        }
        return digest.digest();
    }
}
