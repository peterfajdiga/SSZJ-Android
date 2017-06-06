package peterfajdiga.sszj.logic.requests;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class BitmapRequest extends ImageRequest {

    public BitmapRequest(final Owner owner, final String url, final int index) {
        super(url, new Listener(owner, index), 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new ErrorListener(owner));
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
        private Owner requestOwner;

        ErrorListener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            System.err.println(error.getMessage());
            requestOwner.onBitmapFailed();
        }
    }


    public interface Owner {
        void onBitmapLoaded(int index, Bitmap bitmap);
        void onBitmapFailed();
    }
}