package peterfajdiga.sszj.obb;

import android.content.Context;
import android.os.Environment;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;

import androidx.annotation.NonNull;

import java.io.File;

public class ObbMounter {
    private final StorageManager storageManager;
    private final File obbFile;

    public ObbMounter(@NonNull final Context context) {
        this.storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        this.obbFile = getObbFile(context);
    }

    @NonNull
    private static File getObbFile(@NonNull final Context context) {
        return new File(context.getObbDir(), "data.obb");
    }

    public void mount(@NonNull final OnObbMountedListener listener) {
        if (storageManager.isObbMounted(obbFile.getPath())) {
            final ObbLoader obbLoader = new ObbLoader(storageManager, obbFile);
            listener.onObbMounted(obbLoader);
            return;
        }

        if (!obbFile.exists()) {
            listener.onObbFailure();  // TODO: download
            return;
        }

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

    public interface OnObbMountedListener {
        void onObbMounted(ObbLoader obbLoader);
        void onObbFailure();
    }
}
