package peterfajdiga.sszj;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;

import peterfajdiga.sszj.logic.Words;

public class SearchRecentProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "peterfajdiga.sszj.SearchRecentAuthority";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchRecentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    // TODO: Enable clearing

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final Cursor recents = super.query(uri, projection, selection, selectionArgs, sortOrder);

        final String query = selectionArgs[0];
        final String[] matches = Words.getWordsStartingWith(query);
        final MatrixCursor autocomplete = new MatrixCursor(recents.getColumnNames(), matches.length);
        if (!Words.isValidWord(query) && Words.isValidWordSpelling(query)) {
            autocomplete.addRow(new String[]{
                    "0",
                    Uri.parse("android.resource://peterfajdiga.sszj/" + R.drawable.ic_letter).toString(),
                    query,
                    query,
                    "-2"
            });
        }
        for (String match : matches) {
            if (cursorContains(recents, match)) {
                continue;
            }
            autocomplete.addRow(new String[]{
                    "0",
                    Uri.parse("android.resource://system/" + android.R.drawable.ic_menu_search).toString(),
                    match,
                    match,
                    "-1"
            });
        }

        return new MergeCursor(new Cursor[] {recents, autocomplete});
    }

    private static boolean cursorContains(Cursor c, String str) {
        c.moveToPosition(-1);
        while (c.moveToNext()) {
            if (c.getString(2).equals(str)) {
                return true;
            }
        }
        return false;
    }
}
