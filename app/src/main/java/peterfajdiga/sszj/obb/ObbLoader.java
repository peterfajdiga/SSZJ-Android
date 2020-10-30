package peterfajdiga.sszj.obb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import java.io.File;

public class ObbLoader {
    private final StorageManager storageManager;
    private final File obbFile;

    public ObbLoader(@NonNull final Context context) {
        this.storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        this.obbFile = getObbFile(context);
    }

    @NonNull
    private static File getObbFile(@NonNull final Context context) {
        return new File(new File(Environment.getExternalStorageDirectory(), context.getPackageName()), "data.obb");
    }

    public void mount() {
        if (!obbFile.exists()) {
            download();
        }
        final boolean done = storageManager.mountObb(obbFile.getPath(), null, new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(final String path, final int state) {
                super.onObbStateChange(path, state);
            }
        });
    }

    private void download() {
        // TODO
    }

    @NonNull
    public Bitmap getBitmap(@NonNull final String filename) throws InvalidStateException {
        final String obbPath = obbFile.getPath();
        if (!storageManager.isObbMounted(obbPath)) {
            throw new InvalidStateException(OnObbStateChangeListener.ERROR_NOT_MOUNTED);
        }

        final String dir = storageManager.getMountedObbPath(obbPath);
        final File wordFile = new File(new File(dir, "gestures"), filename);
        return BitmapFactory.decodeFile(wordFile.getPath());
    }
}
