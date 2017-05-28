package peterfajdiga.sszj.volley;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class BitmapRequest extends ImageRequest {

    public BitmapRequest(Owner owner, String url, int index) {
        super(url, new Listener(owner, index), 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new ErrorListener());
    }


    private static class Listener implements Response.Listener<Bitmap> {
        private Owner requestOwner;
        private int index;
        Listener(final Owner requestOwner, int index) {
            this.requestOwner = requestOwner;
            this.index = index;
        }
        @Override
        public void onResponse(Bitmap response) {
            requestOwner.onBitmapLoaded(index, response);
        }
    }

    private static class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.err.println(error.getMessage());
        }
    }


    public interface Owner {
        void onBitmapLoaded(int index, Bitmap bitmap);
    }
}
