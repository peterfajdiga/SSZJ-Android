package peterfajdiga.sszj.volley;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import peterfajdiga.sszj.AnimationBuilder;
import peterfajdiga.sszj.pojo.Word;

public class WordRequest extends JsonObjectRequest {

    private static final String feature = "slovar";
    private Owner owner = null;

    public WordRequest(final Owner owner, final String word) {
        super(Request.Method.GET, Constants.buildUrl(feature, word), null, new Listener(owner), new ErrorListener());
        this.owner = owner;
    }


    private static class Listener implements Response.Listener<JSONObject>, BitmapRequest.Owner {
        private Owner requestOwner;
        private Word word;
        private Bitmap[] bitmaps;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(JSONObject response) {
            final RequestQueue queue = Constants.getQueue();
            try {
                // load bitmap 2
                if (response.has("jpg2")) {
                    bitmaps = new Bitmap[2];
                    final String jpg2 = response.getString("jpg2");
                    final BitmapRequest request = new BitmapRequest(this, jpg2, 1);
                    // TODO: Cancel request when closing fragment
                    queue.add(request);
                } else {
                    bitmaps = new Bitmap[1];
                }

                // load bitmap 1
                final String jpg1 = response.getString("jpg1");
                final BitmapRequest request = new BitmapRequest(this, jpg1, 0);
                // TODO: Cancel request when closing fragment
                queue.add(request);

                // load word
                word = new Word(response.getString("beseda"), "Ni še definicij.");
                requestOwner.onWordLoaded(word);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapLoaded(int index, Bitmap bitmap) {
            bitmaps[index] = bitmap;
            boolean hasNull = false;
            for (int i = 0; i < bitmaps.length; i++) {
                if (bitmaps[i] == null) {
                    hasNull = true;
                    break;
                }
            }
            if (!hasNull) {
                requestOwner.onWordAnimationLoaded(AnimationBuilder.build(bitmaps));
            }
        }
    }

    private static class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: Handle non-existent word
            System.err.println(error.getMessage());
        }
    }


    public interface Owner {
        void onWordLoaded(Word word);
        void onWordAnimationLoaded(AnimationDrawable animation);
    }
}
