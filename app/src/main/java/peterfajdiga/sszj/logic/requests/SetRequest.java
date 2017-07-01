package peterfajdiga.sszj.logic.requests;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetRequest extends JsonObjectRequest {

    private static final String feature = "sklopi";

    public SetRequest(final Owner owner, final String set) {
        super(Method.GET, Constants.buildUrl(feature, set), null, new Listener(owner), new ErrorListener(owner));
    }


    private static class Listener implements Response.Listener<JSONObject> {
        private Owner requestOwner;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(JSONObject response) {
            try {
                final JSONArray words = response.getJSONArray("words");
                final int n = words.length();
                final String[] words_str = new String[n];
                for (int i = 0; i < n; i++) {
                    final JSONObject word = words.getJSONObject(i);
                    words_str[i] = word.getString("beseda_prava");
                }
                requestOwner.onSetLoaded(words_str);
            } catch (Exception e) {
                e.printStackTrace();
                requestOwner.onSetFailed();
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
            requestOwner.onSetFailed();
        }
    }


    public interface Owner {
        void onSetLoaded(String[] words);
        void onSetFailed();
    }
}
