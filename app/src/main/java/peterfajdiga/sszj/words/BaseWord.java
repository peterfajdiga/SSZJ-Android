package peterfajdiga.sszj.words;

import androidx.annotation.NonNull;

public class BaseWord implements Word {
    private final String headword;
    final String gestureFile;

    BaseWord(final String headword, final String gestureFile) {
        this.headword = headword;
        this.gestureFile = gestureFile;
    }

    @NonNull
    @Override
    public String getHeadword() {
        return headword;
    }

    @NonNull
    @Override
    public String[] getGestureFiles() {
        return new String[]{gestureFile};
    }
}
