package kamisado;

import java.io.Serializable;

class Tile implements Serializable {
    ColorEnum color;
    boolean isOccupied;

    Tile() {
        isOccupied = false;
        color = ColorEnum.WHITE;
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