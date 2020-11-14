package peterfajdiga.sszj.logic.words;

import java.io.Serializable;

public class Set implements Serializable {
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
