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
    private ObbLoader() {}

    @NonNull
    private static File getObbFile(@NonNull final Context context) {
        return new File(new File(Environment.getExternalStorageDirectory(), context.getPackageName()), "data.obb");
    }

    public static void mount(@NonNull final Context context) {
        final File obbFile = getObbFile(context);
        if (!obbFile.exists()) {
            download(context, obbFile);
        }
        final StorageManager storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        final boolean done = storageManager.mountObb(obbFile.getPath(), null, new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(final String path, final int state) {
                super.onObbStateChange(path, state);
            }
        });
    }

    private static void download(@NonNull final Context context, @NonNull final File obbFile) {
        // TODO
    }

    @NonNull
    public static Bitmap getBitmap(@NonNull final Context context, @NonNull final String filename) throws InvalidStateException {
        final File obbFile = getObbFile(context);
        final String obbPath = obbFile.getPath();

        final StorageManager storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        if (!storageManager.isObbMounted(obbPath)) {
            throw new InvalidStateException(OnObbStateChangeListener.ERROR_NOT_MOUNTED);
        }

        final String dir = storageManager.getMountedObbPath(obbPath);
        final File wordFile = new File(new File(dir, "gestures"), filename);
        return BitmapFactory.decodeFile(wordFile.getPath());
    }
}
