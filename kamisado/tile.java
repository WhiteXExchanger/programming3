package kamisado;

import java.io.Serializable;

class Tile implements Serializable {
    private ColorEnum color;
    private boolean isOccupied;
    private boolean isFlagged;
    private Piece piece;

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

    public void clearPiece() {
        this.piece = null;
        this.isOccupied = false;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        this.isOccupied = true;
    }

    public void flag() {
        isFlagged = true;
    }

    public void unflag() {
        isFlagged = false;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}