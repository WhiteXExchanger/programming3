package lab05;

import java.io.Serializable;

/**
 * beer
 */
public class Beer implements Serializable {
    private String name;
    private String style;
    private Double strength;
    
    Beer(String name, String style, Double strength) {
        this.name = name;
        this.style = style;
        this.strength = strength;
    }

    public String toString() {
        return (name + "," + style + "," + strength);
    }

    public String getName() {
        return name;
    }

    public Double getStrength() {
        return strength;
    }

    public String getStyle() {
        return style;
    }
    
}