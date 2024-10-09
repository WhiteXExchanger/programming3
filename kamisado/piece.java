package kamisado;

import java.io.Serializable;

class Piece implements Serializable {
    private Positon positon;
    private ColorEnum color;
    private TeamEnum team;
    int dragonTeeth; // TODO dragonTeeth implementation

    Piece() {
        this.color = ColorEnum.WHITE;
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

    public int getDragonTeeth() {
        return dragonTeeth;
    }

    public TeamEnum getTeam() {
        return team;
    }

    public ColorEnum getColor() {
        return color;
    }

    public void setPositon(Positon positon) {
        this.positon = positon;
    }

    public Positon getPositon() {
        return positon;
    }
}
