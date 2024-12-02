package com.feke.kamisado;

import java.io.Serializable;

/**
 * Represents a coordinate on the board with x and y values.
 * Implements Serializable to allow for saving game state.
 */
class Coordinate implements Serializable {
    private int x;
    private int y;

    /**
     * Constructs a Coordinate with the specified x and y values.
     *
     * @param x The x-coordinate value.
     * @param y The y-coordinate value.
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the coordinate.
     *
     * @return A string in the format "x: [x value] y: [y value]".
     */
    public String toString() {
        return "x: " + this.x + " y: " + this.y;
    }

    /**
     * Gets the x-coordinate value.
     *
     * @return The x-coordinate value.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate value.
     *
     * @return The y-coordinate value.
     */
    public int getY() {
        return y;
    }
}