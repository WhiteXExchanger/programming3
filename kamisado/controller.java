package kamisado;

import java.io.Serializable;
import java.util.ArrayList;

class Controller implements Serializable {
    Board board;
    boolean blacksTurn;

    Controller() {
        blacksTurn = true;
        board = new Board(true);

        View view = new View(board);
    }

    public ArrayList<Piece> getPieces() {
        return board.getPieces();
    }

    
}
