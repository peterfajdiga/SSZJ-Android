package peterfajdiga.sszj.obb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import java.io.File;

public class ObbLoader {
    private final StorageManager storageManager;
    private final File obbFile;

    ObbLoader(@NonNull final StorageManager storageManager, @NonNull final File obbFile) {
        this.storageManager = storageManager;
        this.obbFile = obbFile;
    }

    @NonNull
    public Bitmap getBitmap(@NonNull final String filename) {
        final String obbPath = obbFile.getPath();
        if (!storageManager.isObbMounted(obbPath)) {
            throw new RuntimeException(obbPath + " is not mounted");
        }

        final String dir = storageManager.getMountedObbPath(obbPath);
        final File wordFile = new File(new File(dir, "gestures"), filename);
        return BitmapFactory.decodeFile(wordFile.getPath());
    }
}
