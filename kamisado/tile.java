package kamisado;

import java.io.Serializable;

class Tile implements Serializable {
    private ColorEnum color;
    private boolean isOccupied;

    Tile(ColorEnum color) {
        isOccupied = false;
        this.color = color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public ColorEnum getColor() {
        return color;
    }
    
    public void setOccupation() {
        isOccupied = !isOccupied;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}