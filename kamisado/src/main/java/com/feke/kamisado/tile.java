package com.feke.kamisado;

import java.io.Serializable;

class Tile implements Serializable {
    private ColorEnum color;
    private boolean isOccupied;
    private TileEnum tileEnum;
    private Piece piece;

    Tile(ColorEnum color) {
        isOccupied = false;
        this.color = color;
    }

    Tile(Tile tile) {
        this.color = tile.color;
        this.isOccupied = tile.isOccupied;
        this.tileEnum = tile.tileEnum;
        this.piece = tile.piece;
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

    public void select() {
        tileEnum = TileEnum.SELECTED;
    }

    public void flag() {
        tileEnum = TileEnum.FLAGGED;
    }

    public void unflag() {
        tileEnum = TileEnum.NONE;
    }

    public boolean isSelected() {
        return tileEnum == TileEnum.SELECTED;
    }

    public boolean isFlagged() {
        return tileEnum == TileEnum.FLAGGED;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}