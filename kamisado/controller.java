package kamisado;

import java.io.Serializable;
import java.util.ArrayList;

class Controller implements Serializable {
    Board board;
    boolean blacksTurn;

    Controller(boolean isPlayerFirst) {
        blacksTurn = true;
        board = new Board(isPlayerFirst);
    }

    public ArrayList<Piece> getPieces() {
        return board.getPieces();
    }


}
