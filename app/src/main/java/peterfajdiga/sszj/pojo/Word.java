package peterfajdiga.sszj.pojo;

import android.graphics.drawable.AnimationDrawable;

public class Word {
    public String word;
    public String definition;
    public AnimationDrawable animation;

    public Word(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public Word(String word, String definition, AnimationDrawable animation) {
        this.word = word;
        this.definition = definition;
        this.animation = animation;
    }
}
