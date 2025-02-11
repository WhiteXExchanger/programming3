package com.feke.kamisado;

import java.io.Serializable;

/**
 * Represents a piece on the game board.
 * Tracks the team, color, and abilities of the piece.
 */
public class Piece implements Serializable {
    private ColorEnum color;
    private TeamEnum team;
    private int dragonTeeth;

    /**
     * Constructs a Piece with the specified team and color.
     *
     * @param team  The team to which the piece belongs.
     * @param color The color of the piece.
     */
    Piece(TeamEnum team, ColorEnum color) {
        this.team = team;
        this.color = color;
        this.dragonTeeth = 0;
    }

    /**
     * Constructs a copy of another Piece.
     *
     * @param other The piece to copy.
     */
    Piece(Piece other) {
        this.color = other.color;
        this.team = other.team;
        this.dragonTeeth = other.dragonTeeth;
    }

    /**
     * Gets the movement length of the piece, which is calculated based on the number of dragon teeth.
     *
     * @return The movement length of the piece.
     */
    public int getMovementLength() {
        return 7 - dragonTeeth * 2;
    }

    /**
     * Gets the number of dragon teeth collected by the piece.
     *
     * @return The number of dragon teeth.
     */
    public int getDragonTeeth() {
        return dragonTeeth;
    }

    /**
     * Increases the dragon teeth count by 1, with a maximum value of 3.
     */
    public void increaseDragonTeeth() {
        if (dragonTeeth < 3) dragonTeeth++;
    }

    /**
     * Gets the team of the piece.
     *
     * @return The team of the piece.
     */
    public TeamEnum getTeam() {
        return team;
    }

    /**
     * Gets the color of the piece.
     *
     * @return The color of the piece.
     */
    public ColorEnum getColor() {
        return color;
    }
}
