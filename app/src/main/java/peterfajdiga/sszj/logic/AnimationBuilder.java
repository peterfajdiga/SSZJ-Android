package peterfajdiga.sszj.logic;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AnimationBuilder {

    private static final int frameWidth  = 200;
    private static final int frameHeight = 256;
    private static final int frameDuration = 60;

    public static ReportingAnimationDrawable build(Bitmap[] bitmaps) {
        final ReportingAnimationDrawable animation = new ReportingAnimationDrawable();

        for (Bitmap bitmap : bitmaps) {
            final int fullWidth = bitmap.getWidth();
            for (int x = 0; x < fullWidth; x += frameWidth) {
                final Bitmap frame = Bitmap.createBitmap(bitmap, x, 0, frameWidth, frameHeight);
                animation.addFrame(new BitmapDrawable(frame), frameDuration);
            }
        }

        return animation;
    }

    public static int getFrameCount(Bitmap bitmap) {
        return bitmap.getWidth() / frameWidth;
    }
}
