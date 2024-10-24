package lab_04;

import java.io.Serializable;
import java.util.Comparator;

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

class SortByName implements Comparator<Beer> {
    public int compare(Beer a,  Beer b) {
        return a.getName().compareTo(b.getName());
    }
}

class SortByStyle implements Comparator<Beer> {
    public int compare(Beer a,  Beer b) {
        return a.getStyle().compareTo(b.getStyle()) ;
    }
}

class SortByStrength implements Comparator<Beer> {
    public int compare(Beer a,  Beer b) {
        return (int)( a.getStrength() - b.getStrength() );
    }
}