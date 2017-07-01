package peterfajdiga.sszj.logic.requests;

import android.text.Html;
import android.text.Spanned;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class DefinitionRequest extends StringRequest {

    private static final String feature = "sskj";

    public DefinitionRequest(final Owner owner, final String word) {
        super(Method.GET, Constants.buildUrl(feature, word), new Listener(owner), new ErrorListener(owner));
        setTag(owner);
    }


    private static class Listener implements Response.Listener<String> {
        private Owner requestOwner;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(String response) {
            Spanned definition;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                definition = Html.fromHtml(response, Html.FROM_HTML_MODE_COMPACT);
            } else {
                definition = Html.fromHtml(response);
            }
            requestOwner.onWordDefinitionLoaded(definition);
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
            requestOwner.onWordDefinitionFailed();
        }
    }


    public interface Owner {
        void onWordDefinitionLoaded(Spanned definition);
        void onWordDefinitionFailed();
    }
}
