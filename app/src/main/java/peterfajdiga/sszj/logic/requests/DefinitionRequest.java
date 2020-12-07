package peterfajdiga.sszj.logic.requests;

import android.text.Html;
import android.text.Spanned;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;

public class DefinitionRequest extends StringRequest {
    private static final String entryMatchStart = "<div class=\"col-sm-12 col-md-8 col-md-push-4 entry\">";  // TODO: use regex, ignore classes other than "entry"
    private static final String entryMatchEnd = "</div>";

    public DefinitionRequest(final Owner owner, final String word) {
        super(Method.GET, buildUrl(word), new Listener(owner), new ErrorListener(owner));
        setTag(owner);
    }

    private static String buildUrl(final String word) {
        return "https://www.fran.si/iskanje?FilteredDictionaryIds=133&View=3&Headword=" + word;
    }

    private static class Listener implements Response.Listener<String> {
        private Owner requestOwner;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(final String response) {
            final int indexStart = response.indexOf(entryMatchStart) + entryMatchStart.length();
            final int indexEnd = response.indexOf(entryMatchEnd, indexStart);
            final String definitionHtml = response.substring(indexStart, indexEnd);

            Spanned definition;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                definition = Html.fromHtml(definitionHtml, Html.FROM_HTML_MODE_COMPACT);
            } else {
                definition = Html.fromHtml(definitionHtml);
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
