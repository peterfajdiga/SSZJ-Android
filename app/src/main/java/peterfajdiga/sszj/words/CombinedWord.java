package peterfajdiga.sszj.words;

import androidx.annotation.NonNull;

public class CombinedWord implements Word {
    private final String headword;
    private final BaseWord[] base;

    CombinedWord(final String headword, final BaseWord... base) {
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
            filenames[i] = base[i].gestureFile;
        }
        return filenames;
    }

    @NonNull
    public BaseWord[] getBase() {
        return base;
    }

    @NonNull
    public String[] getBaseHeadwords() {
        final String[] headwords = new String[base.length];
        for (int i = 0; i < headwords.length; i++) {
            headwords[i] = base[i].getHeadword();
        }
        return headwords;
    }
}
