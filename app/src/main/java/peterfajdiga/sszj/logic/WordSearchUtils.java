package peterfajdiga.sszj.logic;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import peterfajdiga.sszj.logic.words.AllWords;

public final class WordSearchUtils {
    private WordSearchUtils() {}

    // ignores case
    public static String[] getWordsStartingWith(final String prefix) {
        if (prefix.length() == 0) {
            return new String[0];
        }

        final Collator sl = Collator.getInstance(new Locale("sl"));
        sl.setStrength(Collator.PRIMARY);  // ignore case

        int topIndex = Arrays.binarySearch(AllWords.headwords, prefix, sl);
        if (topIndex < 0) {
            topIndex = -topIndex - 1;
        }

        final String prefixLower = prefix.toLowerCase();
        final List<String> matches = new ArrayList<>();
        for (int i = topIndex; i < AllWords.headwords.length; i++) {
            final String word = AllWords.headwords[i];
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
        return Arrays.binarySearch(AllWords.headwords, word, sl) >= 0;
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
        final int index = Arrays.binarySearch(AllWords.headwords, word, sl);
        return AllWords.headwords[index];
    }
}
