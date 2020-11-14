package peterfajdiga.sszj.logic.words;

import androidx.annotation.NonNull;

public class CombinedWord implements Word {
    private final String headword;
    private final String[] base;

    public CombinedWord(final String headword, final String... base) {
        this.headword = headword;
        this.base = base;
    }

    @NonNull
    @Override
    public String getHeadword() {
        return headword;
    }

    @NonNull
    @Override
    public String[] getGestureFiles() {
        final String[] filenames = new String[base.length];
        for (int i = 0; i < base.length; i++) {
            final Word baseWord = AllWords.wordMap.get(base[i]);
            filenames[i] = ((BaseWord)baseWord).gestureFile;
        }
        return filenames;
    }

    @NonNull
    public String[] getBase() {
        return base;
    }
}
