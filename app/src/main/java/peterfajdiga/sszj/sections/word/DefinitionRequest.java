package peterfajdiga.sszj.sections.word;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class DefinitionRequest extends StringRequest {
    public DefinitionRequest(final Owner owner, final String url) {
        super(Method.GET, url, new Listener(owner), new ErrorListener(owner));
        setTag(owner);
    }

    private static class Listener implements Response.Listener<String> {
        private Owner requestOwner;

        Listener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onResponse(final String response) {
            try {
                final String definitionHtml = extractDefinition(response);

                final Spanned definition;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    definition = Html.fromHtml(definitionHtml, Html.FROM_HTML_MODE_COMPACT);
                } else {
                    definition = Html.fromHtml(definitionHtml);
                }
                requestOwner.onWordDefinitionLoaded(definition);

            } catch (final HtmlParseException e) {
                Log.e("DefinitionRequest", "Failed to parse definition", e);
                requestOwner.onWordDefinitionFailed();
            }
        }

        private static String extractDefinition(final String html) throws HtmlParseException {
            final int definitionAttributeIndex = html.indexOf("data-group=\"explanation");
            if (definitionAttributeIndex == -1) {
                throw new HtmlParseException();
            }

            final int definitionContentStartIndex = html.indexOf('>', definitionAttributeIndex);
            if (definitionContentStartIndex == -1) {
                throw new HtmlParseException();
            }

            final int definitionContentEndIndex = html.indexOf("</span>", definitionContentStartIndex + 1);
            if (definitionContentEndIndex == -1) {
                throw new HtmlParseException();
            }

            final String definition = html.substring(definitionContentStartIndex + 1, definitionContentEndIndex);
            return removeTrailingCharacter(definition, ':');
        }

        private static String removeTrailingCharacter(final String string, final char character) {
            final int lastIndex = string.length() - 1;
            if (string.charAt(lastIndex) == character) {
                return string.substring(0, lastIndex);
            }
            return string;
        }

        private static class HtmlParseException extends Exception {}
    }

    private static class ErrorListener implements Response.ErrorListener {
        private Owner requestOwner;

        ErrorListener(final Owner requestOwner) {
            this.requestOwner = requestOwner;
        }

        @Override
        public void onErrorResponse(final VolleyError error) {
            requestOwner.onWordDefinitionFailed();
        }
    }


    public interface Owner {
        void onWordDefinitionLoaded(CharSequence definition);
        void onWordDefinitionFailed();
    }
}
