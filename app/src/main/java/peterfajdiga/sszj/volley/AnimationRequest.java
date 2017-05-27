package peterfajdiga.sszj.volley;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class AnimationRequest extends ImageRequest {

    private static final int frameWidth  = 200;
    private static final int frameHeight = 256;
    private static final int frameDuration = 60;

    public AnimationRequest(Owner owner, String url) {
        super(url, new Listener(owner), 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new ErrorListener());
    }


    private static class Listener implements Response.Listener<Bitmap> {
        private Owner requestOwner;
        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }
        @Override
        public void onResponse(Bitmap response) {
            final int fullWidth = response.getWidth();
            final AnimationDrawable animation = new AnimationDrawable();
            for (int x = 0; x < fullWidth; x += frameWidth) {
                final Bitmap frame = Bitmap.createBitmap(response, x, 0, frameWidth, frameHeight);
                animation.addFrame(new BitmapDrawable(frame), frameDuration);
            }
            requestOwner.onAnimationLoaded(animation);
        }
    }

    private static class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.err.println(error.getMessage());
        }
    }


    public interface Owner {
        void onAnimationLoaded(AnimationDrawable animation);
    }
}
