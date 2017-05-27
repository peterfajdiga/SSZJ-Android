package peterfajdiga.sszj;

import android.content.SearchRecentSuggestionsProvider;

public class SearchRecentProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "peterfajdiga.sszj.SearchRecentAuthority";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchRecentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    // TODO: Enable clearing
}
