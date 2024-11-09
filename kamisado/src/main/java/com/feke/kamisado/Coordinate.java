package com.feke.kamisado;

import java.io.Serializable;

class Coordinate implements Serializable {
    private int x;
    private int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "x:" + this.x + " y:" + this.y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
}
