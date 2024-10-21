package kamisado;

import java.io.Serializable;

class Piece implements Serializable {
    private ColorEnum color;
    private TeamEnum team;
    private int dragonTeeth;

    Piece() {
        this.color = ColorEnum.WHITE;
    }

    Piece(Piece other) {
        this.color = other.getColor();
        this.team = other.getTeam();
    }

    public String toString() {
        return "color: " + this.color;
    }

    Piece(TeamEnum team, ColorEnum color) {
        this.team = team;
        this.color = color;
        this.dragonTeeth = 0;
    }

    Piece(TeamEnum team, ColorEnum color, int dragonTeeth) {
        this.team = team;
        this.color = color;
        this.dragonTeeth = dragonTeeth;
    }

    public int getMovementLength() {
        return 7-dragonTeeth*2;
    }

    public int getDragonTeeth() {
        return dragonTeeth;
    }

    public void increaseDragonTeeth() {
        if (dragonTeeth<3) dragonTeeth++;
    }

    public TeamEnum getTeam() {
        return team;
    }

    public ColorEnum getColor() {
        return color;
    }
}
