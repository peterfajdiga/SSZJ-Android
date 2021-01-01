package peterfajdiga.sszj.sets;

public class Set {
    public final String label;
    public final int imageResource;
    public final String[] words;

    public Set(final String label, final int imageResource, final String... words) {
        this.label = label;
        this.imageResource = imageResource;
        this.words = words;
    }

    public String getKeyword() {
        return label.toLowerCase();
    }
}
