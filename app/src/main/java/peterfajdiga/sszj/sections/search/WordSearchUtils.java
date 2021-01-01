package peterfajdiga.sszj.sections.search;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import peterfajdiga.sszj.words.Words;

public final class WordSearchUtils {
    private WordSearchUtils() {}

    // ignores case
    public static String[] getWordsStartingWith(final String prefix) {
        if (prefix.length() == 0) {
            return new String[0];
        }

        final Collator sl = Collator.getInstance(new Locale("sl"));
        sl.setStrength(Collator.PRIMARY);  // ignore case

        int topIndex = Arrays.binarySearch(Words.HEADWORDS, prefix, sl);
        if (topIndex < 0) {
            topIndex = -topIndex - 1;
        }

        final String prefixLower = prefix.toLowerCase();
        final List<String> matches = new ArrayList<>();
        for (int i = topIndex; i < Words.HEADWORDS.length; i++) {
            final String word = Words.HEADWORDS[i];
            if (word.toLowerCase().startsWith(prefixLower)) {
                matches.add(word);
            } else {
                break;
            }
        }
        return matches.toArray(new String[0]);
    }

    // ignores case
    // empty returns false
    public static boolean isValidWord(final String word) {
        final Collator sl = Collator.getInstance(new Locale("sl"));
        sl.setStrength(Collator.PRIMARY);  // ignore case
        return Arrays.binarySearch(Words.HEADWORDS, word, sl) >= 0;
    }

    // ignores case
    // empty returns false
    // may contain space
    private static final Pattern spellingChars = Pattern.compile("[ABCČDEFGHIJKLMNOPRSŠTUVZŽ\\s]+");
    public static boolean isValidWordSpelling(final String word) {
        return spellingChars.matcher(word.toUpperCase()).matches();
    }

    // converts to upper case
    // removes spaces
    private static final Pattern illegalChars = Pattern.compile("[^ABCČDEFGHIJKLMNOPRSŠTUVZŽ]+");
    public static String makeValidWordSpelling(final String word) {
        return illegalChars.matcher(word.toUpperCase()).replaceAll("");
    }

    public static String getCorrectCase(final String word) {
        final Collator sl = Collator.getInstance(new Locale("sl"));
        sl.setStrength(Collator.PRIMARY);  // ignore case
        final int index = Arrays.binarySearch(Words.HEADWORDS, word, sl);
        return Words.HEADWORDS[index];
    }
}
