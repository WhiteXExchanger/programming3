package kamisado;

import java.io.Serializable;

class Piece implements Serializable {
    private Positon positon;
    private ColorEnum color;
    private TeamEnum team;

    int dragonTeeth; // TODO dragonTeeth implementation

    public void setTeam(TeamEnum team) {
        this.team = team;
    }

    public TeamEnum getTeam() {
        return team;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
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
