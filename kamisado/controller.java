package kamisado;

import java.io.Serializable;

class Controller implements Serializable {
    Board board;
    boolean blacksTurn;

    Controller() {
        blacksTurn = true;
        board = new Board();
    }

    public Piece[] getPieces() {
        return board.getPieces();
    }


}
