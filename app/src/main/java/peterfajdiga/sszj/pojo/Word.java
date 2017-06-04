package peterfajdiga.sszj.pojo;

public class Word {
    public String word;
    public String[] base;

    public Word(String word, String... base) {
        this.word = word;
        this.base = base;
    }
}
