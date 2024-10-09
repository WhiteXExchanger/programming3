package kamisado;

import java.io.Serializable;
import java.util.ArrayList;

class Board implements Serializable {
    private Tile[][] tileMatrix;
    private Piece[] blackPieces;
    private Piece[] whitePieces;
    private boolean isPlayerFirst;


    Board(boolean isPlayerFirst) {
        this.isPlayerFirst = isPlayerFirst;
        tileMatrix = new Tile[8][8];
        blackPieces = new Piece[8];
        whitePieces = new Piece[8];
        populateTiles();
        placePieces();
    }
    
    void populateTiles() {
        // ---------- www.github.com/mandriv/kamisado/blob/master/src/kamisado/logic/Board.java ----------
        int[] counters = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < tileMatrix.length; i++) {
            tileMatrix[i][counters[0]].setColor(ColorEnum.ORANGE);
            counters[0] = (counters[0] + 1) % 8;
            tileMatrix[i][counters[1]].setColor(ColorEnum.BLUE);
            counters[1] = (counters[1] + 11) % 8;
            tileMatrix[i][counters[2]].setColor(ColorEnum.PURPLE);
            counters[2] = (counters[2] + 5) % 8;
            tileMatrix[i][counters[3]].setColor(ColorEnum.PINK);
            counters[3] = (counters[3] + 7) % 8;
            tileMatrix[i][counters[4]].setColor(ColorEnum.YELLOW);
            counters[4] = (counters[4] + 1) % 8;
            tileMatrix[i][counters[5]].setColor(ColorEnum.RED);
            counters[5] = (counters[5] + 3) % 8;
            tileMatrix[i][counters[6]].setColor(ColorEnum.GREEN);
            counters[6] = (counters[6] + 5) % 8;
            tileMatrix[i][counters[7]].setColor(ColorEnum.BROWN);
            counters[7] = (counters[7] + 7) % 8;
        }
        // -----------------------------------------------------------------------------------------------
    }

    void placePieces() {
        int topIndex = 0;
        int bottomIndex = 7;

        if (isPlayerFirst) {
            placeWhite(topIndex);
            placeBlack(bottomIndex);
        } else {
            placeBlack(topIndex);
            placeWhite(bottomIndex);
        }
    }

    void placeBlack(int lineIndex) {
        int index = 0;
        for (Piece piece : blackPieces) {
            piece.setTeam(TeamEnum.BLACK);
            piece.setColor(tileMatrix[lineIndex][index].getColor());
            piece.setPositon(new Positon(lineIndex, index));
            index++;
        }
    }

    void placeWhite(int lineIndex) {
        int index = 0;
        for (Piece piece : whitePieces) {
            piece.setTeam(TeamEnum.WHITE);
            piece.setColor(tileMatrix[lineIndex][index].getColor());
            piece.setPositon(new Positon(lineIndex, index));
            index++;
        }
    }

    Piece[] getPieces() {
        Piece[] pieces = new Piece[16];
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = blackPieces[i];
            pieces[i+8] = whitePieces[i];
        }
        return pieces;
    }

    ArrayList<Tile> getTilesToGo(Positon positon) {
        Piece piece = searchForPiece(positon);

        if (piece.getTeam() == TeamEnum.BLACK) {
            return searchTiles(positon, isPlayerFirst);
        } else {
            return searchTiles(positon, !isPlayerFirst);
        }
    }

    ArrayList<Tile> searchTiles(Positon positon, boolean upWard) {
        ArrayList<Tile> tiles = new ArrayList<>();
        int x = positon.getX(), y = positon.getY();
        while (x >= 0 && y ) {
            
        }
    }

    Piece searchForPiece(Positon positon) {
        for (Piece piece : blackPieces) {
            if (piece.getPositon() == positon) {
                return piece;
            }
        }
        for (Piece piece : whitePieces) {
            if (piece.getPositon() == positon) {
                return piece;
            }
        }

        return new Piece();
    }
    
}
