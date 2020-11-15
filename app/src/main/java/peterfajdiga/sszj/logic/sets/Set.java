package peterfajdiga.sszj.logic.sets;

public class Set {
    public String label;
    public int imageResource;

    public Set(String label, int imageResource) {
        this.label = label;
        this.imageResource = imageResource;
    }

    public String getKeyword() {
        return label.toLowerCase();
    }
}
