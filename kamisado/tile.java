package kamisado;

class Tile {
    ColorEnum color;
    boolean isOccupied;

    public void setOccupation() {
        isOccupied = !isOccupied;
    }

    public boolean getOccupation() {
        return isOccupied;
    }

    public ColorEnum getColor() {
        return color;
    }
}
