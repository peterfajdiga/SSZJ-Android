package peterfajdiga.sszj.obb;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import java.io.File;

public class ObbMounter {
    private static final String OBB_URL = "https://raw.githubusercontent.com/peterfajdiga/SSZJ-Android/master/imgsrc/ic_logo_original.png";

    private final StorageManager storageManager;
    private final DownloadManager downloadManager;
    private final File obbFile;

    public ObbMounter(@NonNull final Context context) {
        this.storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        this.downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.obbFile = getObbFile(context);
    }

    @NonNull
    private static File getObbFile(@NonNull final Context context) {
        return new File(context.getObbDir(), "data.obb");
    }

    public void init(@NonNull final OnObbMountedListener listener) {
        if (storageManager.isObbMounted(obbFile.getPath())) {
            final ObbLoader obbLoader = new ObbLoader(storageManager, obbFile);
            listener.onObbMounted(obbLoader);
            return;
        }

        if (obbFile.exists()) {
            mount(listener);
        } else {
            downloadAndMount(listener);
        }
    }

    private void mount(@NonNull final OnObbMountedListener listener) {
        final boolean success = storageManager.mountObb(obbFile.getPath(), null, new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(final String path, final int state) {
                super.onObbStateChange(path, state);
                switch (state) {
                    case MOUNTED:
                    case ERROR_ALREADY_MOUNTED:
                        System.err.println("mounted OBB: " + state);
                        final ObbLoader obbLoader = new ObbLoader(storageManager, obbFile);
                        listener.onObbMounted(obbLoader);
                        break;
                    default:
                        System.err.println("Failed to mount OBB, state: " + state);
                        listener.onObbFailure();
                }
            }
        });
        if (!success) {
            listener.onObbFailure();
        }
    }

    private void downloadAndMount(@NonNull final OnObbMountedListener listener) {
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(OBB_URL));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(obbFile));
        request.setAllowedOverMetered(true);  // TODO: warn on mobile data
        request.setAllowedOverRoaming(true);  // TODO: warn on mobile data

        final long downloadId = downloadManager.enqueue(request);
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
                        continue;
                    }
                    assert cursor.getCount() == 1;

                    final int status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL:
                            mount(listener);
                            return;
                        case DownloadManager.STATUS_FAILED:
                            listener.onObbFailure();
                            return;
                    }
                }
            }
        }).start();
    }

    public interface OnObbMountedListener {
        void onObbMounted(ObbLoader obbLoader);
        void onObbFailure();
    }
}
