package peterfajdiga.sszj.pojo;

import android.text.Spanned;

public class Word {
    public String word;
    public Spanned definition;
    public String[] base;

    public Word(String word, Spanned definition, String... base) {
        this.word = word;
        this.definition = definition;
        this.base = base;
    }
}
