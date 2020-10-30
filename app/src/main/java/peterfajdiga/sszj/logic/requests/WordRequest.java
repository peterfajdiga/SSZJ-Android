package peterfajdiga.sszj.logic.requests;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import peterfajdiga.sszj.logic.AnimationBuilder;
import peterfajdiga.sszj.logic.ReportingAnimationDrawable;
import peterfajdiga.sszj.logic.pojo.Word;
import peterfajdiga.sszj.obb.ObbLoader;

// TODO: refactor
public class WordRequest extends JsonObjectRequest {
    private static final String feature = "slovar";

    public WordRequest(@NonNull final Context context, final Owner owner, final String word) {
        super(Method.GET, Constants.buildUrl(feature, word), null, new Listener(context, owner), new ErrorListener(owner));
        setTag(owner);
    }

    private static class Listener implements Response.Listener<JSONObject> {
        private final Context context;
        private Owner requestOwner;
        private Bitmap[] bitmaps;

        Listener(@NonNull final Context context, final Owner requestOwner) {
            this.context = context;
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
                    final String jpgUrl = jpgs.getString(i);
                    final String jpgFilename = new File(jpgUrl).getName();
                    final Bitmap bitmap = ObbLoader.getBitmap(context, jpgFilename);
                    onBitmapLoaded(i, bitmap);
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

        // TODO: refactor (remove)
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
        void onWordLoaded(Word word);
        void onWordFailed();
        void onWordAnimationLoaded(ReportingAnimationDrawable animation, int[] frameCounts);
        void onWordAnimationFailed();
    }
}
