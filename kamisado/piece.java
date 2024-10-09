package kamisado;

class Piece {
    Positon positon;
    ColorEnum color;
    TeamEnum team;

    int dragonTeeth; // TODO dragonTeeth implementation

    boolean move(int x, int y) {
        // move if possible, its not possible if tile is occupied
        return false;
    }
}
