package peterfajdiga.sszj.pojo;

import android.text.Spanned;

public class Word {
    public String word;
    public String[] base;

    public Word(String word, String... base) {
        this.word = word;
        this.base = base;
    }
}
