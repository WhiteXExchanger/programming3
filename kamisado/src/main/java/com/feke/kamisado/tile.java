package com.feke.kamisado;

import java.io.Serializable;

/**
 * Represents a tile on the game board.
 * Tracks the state of the tile, including its color, occupation status, and associated piece.
 */
class Tile implements Serializable {
    private ColorEnum color;
    private boolean isOccupied;
    private TileEnum tileEnum;
    private Piece piece;

    /**
     * Constructs a Tile with the specified color.
     *
     * @param color The color of the tile.
     */
    Tile(ColorEnum color) {
        isOccupied = false;
        this.color = color;
    }

    /**
     * Constructs a copy of an existing Tile.
     *
     * @param tile The tile to copy.
     */
    Tile(Tile tile) {
        this.color = tile.color;
        this.isOccupied = tile.isOccupied;
        this.tileEnum = tile.tileEnum;
        this.piece = tile.piece;
    }

    /**
     * Sets the color of the tile.
     *
     * @param color The new color for the tile.
     */
    public void setColor(ColorEnum color) {
        this.color = color;
    }

    /**
     * Gets the color of the tile.
     *
     * @return The color of the tile.
     */
    public ColorEnum getColor() {
        return color;
    }

    /**
     * Clears the piece from the tile.
     */
    public void clearPiece() {
        this.piece = null;
        this.isOccupied = false;
    }

    /**
     * Sets the piece on this tile.
     *
     * @param piece The piece to place on the tile.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        this.isOccupied = true;
    }
    
    /**
     * Marks the tile as selected.
     * This state is used to indicate that the tile has been chosen for a particular action.
     */
    public void select() {
        tileEnum = TileEnum.SELECTED;
    }

    /**
     * Flags the tile as a possible movement option.
     * This state is used to indicate that the tile is a valid target for movement.
     */
    public void flag() {
        tileEnum = TileEnum.FLAGGED;
    }

    /**
     * Unflags the tile, resetting its state to none.
     * This method is used to clear any indication that the tile was selected or flagged.
     */
    public void unflag() {
        tileEnum = TileEnum.NONE;
    }

    /**
     * Checks if the tile is currently selected.
     *
     * @return True if the tile is in the selected state, false otherwise.
     */
    public boolean isSelected() {
        return tileEnum == TileEnum.SELECTED;
    }

    /**
     * Checks if the tile is currently flagged.
     *
     * @return True if the tile is in the flagged state, false otherwise.
     */
    public boolean isFlagged() {
        return tileEnum == TileEnum.FLAGGED;
    }

    /**
     * Gets the piece currently on the tile.
     *
     * @return The piece on the tile, or null if the tile is unoccupied.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Checks if the tile is occupied by a piece.
     *
     * @return True if the tile has a piece, false otherwise.
     */
    public boolean isOccupied() {
        return isOccupied;
    }
}
