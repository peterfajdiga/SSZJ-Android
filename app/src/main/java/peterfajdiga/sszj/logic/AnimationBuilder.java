package peterfajdiga.sszj.logic;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AnimationBuilder {

    private static final int frameWidth  = 200;
    private static final int frameHeight = 256;
    private static final int frameDuration = 60;

    public static ReportingAnimationDrawable build(final Bitmap[] bitmaps) {
        final ReportingAnimationDrawable animation = new ReportingAnimationDrawable();

        for (Bitmap bitmap : bitmaps) {
            final int fullWidth = bitmap.getWidth();
            for (int x = 0; x < fullWidth; x += frameWidth) {
                final Bitmap frame = Bitmap.createBitmap(bitmap, x, 0, frameWidth, frameHeight);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    frame.setDensity(1);  // workaround for Android SDK < 18
                }
                final BitmapDrawable frameDrawable = new BitmapDrawable(frame);
                frameDrawable.setTargetDensity(bitmap.getDensity());
                animation.addFrame(frameDrawable, frameDuration);
            }
        }

        return animation;
    }

    public static int[] getFrameCounts(final Bitmap[] bitmaps) {
        int[] frameCounts = new int[bitmaps.length];
        for (int i = 0; i < bitmaps.length; i++) {
            frameCounts[i] = getFrameCount(bitmaps[i]);
        }
        return frameCounts;
    }

    public static int getFrameCount(final Bitmap bitmap) {
        return bitmap.getWidth() / frameWidth;
    }
}
