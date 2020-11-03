package peterfajdiga.sszj.logic.requests;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import peterfajdiga.sszj.logic.AnimationBuilder;
import peterfajdiga.sszj.logic.ReportingAnimationDrawable;
import peterfajdiga.sszj.logic.pojo.WordLegacy;
import peterfajdiga.sszj.obb.ObbLoader;

// TODO: refactor
public class WordRequest extends JsonObjectRequest {
    private static final String feature = "slovar";

    public WordRequest(@NonNull final ObbLoader obbLoader, final Owner owner, final String word) {
        super(Method.GET, Constants.buildUrl(feature, word), null, new Listener(obbLoader, owner), new ErrorListener(owner));
        setTag(owner);
    }

    private static class Listener implements Response.Listener<JSONObject> {
        private final ObbLoader obbLoader;
        private Owner requestOwner;
        private Bitmap[] bitmaps;

        Listener(@NonNull final ObbLoader obbLoader, final Owner requestOwner) {
            this.obbLoader = obbLoader;
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(JSONObject response) {
            try {
                // load animation
                final JSONArray jpgs = response.getJSONArray("jpg");
                final int n = jpgs.length();
                bitmaps = new Bitmap[n];
                try {
                    for (int i = 0; i < n; i++) {
                        final String jpgUrl = jpgs.getString(i);
                        final String jpgFilename = new File(jpgUrl).getName();
                        final Bitmap bitmap = obbLoader.getBitmap(jpgFilename);
                        bitmaps[i] = bitmap;
                    }
                    requestOwner.onWordAnimationLoaded(AnimationBuilder.build(bitmaps), getFrameCounts(bitmaps));
                } catch (final Exception e) {
                    requestOwner.onWordAnimationFailed();
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
                final WordLegacy word = new WordLegacy(response.getString("beseda"), base);
                requestOwner.onWordLoaded(word);
            } catch (Exception e) {
                e.printStackTrace();
                requestOwner.onWordFailed();
            }
        }

        private int[] getFrameCounts(@NonNull final Bitmap[] bitmaps) {
            int[] frameCounts = new int[bitmaps.length];
            for (int i = 0; i < bitmaps.length; i++) {
                frameCounts[i] = AnimationBuilder.getFrameCount(bitmaps[i]);
            }
            return frameCounts;
        }
    }


    private static class ErrorListener implements Response.ErrorListener {
        private Owner requestOwner;

        ErrorListener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            //System.err.println(error.getMessage());
            requestOwner.onWordFailed();
        }
    }


    public interface Owner {
        void onWordLoaded(WordLegacy word);
        void onWordFailed();
        void onWordAnimationLoaded(ReportingAnimationDrawable animation, int[] frameCounts);
        void onWordAnimationFailed();
    }
}
