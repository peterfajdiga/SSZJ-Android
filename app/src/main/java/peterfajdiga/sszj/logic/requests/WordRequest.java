package peterfajdiga.sszj.logic.requests;

import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import peterfajdiga.sszj.logic.AnimationBuilder;
import peterfajdiga.sszj.logic.ReportingAnimationDrawable;
import peterfajdiga.sszj.logic.pojo.Word;

public class WordRequest extends JsonObjectRequest {

    private static final String feature = "slovar";

    public WordRequest(final Owner owner, final String word) {
        super(Method.GET, Constants.buildUrl(feature, word), null, new Listener(owner), new ErrorListener(owner));
        setTag(owner);
    }


    private static class Listener implements Response.Listener<JSONObject>, BitmapRequest.Owner {
        private Owner requestOwner;
        private Bitmap[] bitmaps;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(JSONObject response) {
            final RequestQueue queue = Constants.getQueue();
            try {
                // load animation
                final JSONArray jpgs = response.getJSONArray("jpg");
                final int n = jpgs.length();
                bitmaps = new Bitmap[n];
                for (int i = 0; i < n; i++) {
                    final String jpg = jpgs.getString(i);
                    final BitmapRequest request = new BitmapRequest(this, jpg, i);
                    request.setTag(requestOwner);
                    queue.add(request);
                }

                // base
                final String[] base;
                if (response.has("osnovne")) {
                    final JSONArray baseWords = response.getJSONArray("osnovne");
                    //assert n == baseWords.length();
                    base = new String[n];
                    for (int i = 0; i < n; i++) {
                        base[i] = baseWords.getString(i);
                    }
                } else {
                    base = new String[0];
                }

                // build word
                final Word word = new Word(response.getString("beseda"), base);
                requestOwner.onWordLoaded(word);
            } catch (Exception e) {
                e.printStackTrace();
                requestOwner.onWordFailed();
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
                int[] frameCounts = new int[bitmaps.length];
                for (int i = 0; i < bitmaps.length; i++) {
                    frameCounts[i] = AnimationBuilder.getFrameCount(bitmaps[i]);
                }
                requestOwner.onWordAnimationLoaded(AnimationBuilder.build(bitmaps), frameCounts);
            }
        }

        @Override
        public void onBitmapFailed() {
            requestOwner.onWordAnimationFailed();
        }
    }


    private static class ErrorListener implements Response.ErrorListener {
        private Owner requestOwner;

        ErrorListener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: Handle non-existent word
            System.err.println(error.getMessage());
            requestOwner.onWordFailed();
        }
    }


    public interface Owner {
        void onWordLoaded(Word word);
        void onWordFailed();
        void onWordAnimationLoaded(ReportingAnimationDrawable animation, int[] frameCounts);
        void onWordAnimationFailed();
    }
}
