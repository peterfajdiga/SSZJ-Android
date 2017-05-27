package peterfajdiga.sszj.volley;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import peterfajdiga.sszj.pojo.Word;

public class WordRequest extends JsonObjectRequest {

    private static final String feature = "slovar";
    private Owner owner = null;

    public WordRequest(final Owner owner, final String word) {
        super(Request.Method.GET, Constants.buildUrl(feature, word), null, new Listener(owner), new ErrorListener());
        this.owner = owner;
    }


    private static class Listener implements Response.Listener<JSONObject>, AnimationRequest.Owner {
        private Owner requestOwner;
        private Word word;
        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }
        @Override
        public void onResponse(JSONObject response) {
            try {
                word = new Word(response.getString("beseda"), "Ni še definicij.");
                final String imgUrl = response.getString("img1");
                final AnimationRequest request = new AnimationRequest(this, imgUrl);
                // TODO: Cancel request when closing fragment
                Constants.getQueue().add(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAnimationLoaded(AnimationDrawable animation) {
            word.animation = animation;
            requestOwner.onWordLoaded(word);
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
    }
}
