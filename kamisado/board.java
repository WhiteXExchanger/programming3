package kamisado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

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
        placeTiles();
        placePieces();
    }
    
    void placeTiles() {

        // ---------- www.github.com/mandriv/kamisado/blob/master/src/kamisado/logic/Board.java ----------
        int[] counters = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < tileMatrix.length; i++) {
            tileMatrix[i][counters[0]] = new Tile(ColorEnum.ORANGE);
            counters[0] = (counters[0] + 1) % 8;
            tileMatrix[i][counters[1]] = new Tile(ColorEnum.BLUE);
            counters[1] = (counters[1] + 11) % 8;
            tileMatrix[i][counters[2]] = new Tile(ColorEnum.PURPLE);
            counters[2] = (counters[2] + 5) % 8;
            tileMatrix[i][counters[3]] = new Tile(ColorEnum.PINK);
            counters[3] = (counters[3] + 7) % 8;
            tileMatrix[i][counters[4]] = new Tile(ColorEnum.YELLOW);
            counters[4] = (counters[4] + 1) % 8;
            tileMatrix[i][counters[5]] = new Tile(ColorEnum.RED);
            counters[5] = (counters[5] + 3) % 8;
            tileMatrix[i][counters[6]] = new Tile(ColorEnum.GREEN);
            counters[6] = (counters[6] + 5) % 8;
            tileMatrix[i][counters[7]] = new Tile(ColorEnum.BROWN);
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
        for (int i = 0; i < blackPieces.length; i++) {
            blackPieces[i] = new Piece(TeamEnum.BLACK, tileMatrix[lineIndex][index].getColor());
            blackPieces[i].setPositon(new Positon(lineIndex, index));
            index++;
        }
    }

    void placeWhite(int lineIndex) {
        int index = 0;
        for (int i = 0; i < whitePieces.length; i++) {
            whitePieces[i] = new Piece(TeamEnum.WHITE, tileMatrix[lineIndex][index].getColor());
            whitePieces[i].setPositon(new Positon(lineIndex, index));
            index++;
        }
    }

    public ArrayList<Piece> getPieces() {
        ArrayList<Piece> pieces = new ArrayList<>();
        Collections.addAll(pieces, blackPieces);
        Collections.addAll(pieces, whitePieces);
        return pieces;
    }

    public Tile[][] getTileMatrix() {
        return tileMatrix;
    }

    ArrayList<Positon> getPositonsToGo(Positon positon) {
        Piece piece = searchForPiece(positon);

        if (piece.getTeam() == TeamEnum.BLACK) {
            return getPossiblePositons(positon, isPlayerFirst);
        } else {
            return getPossiblePositons(positon, !isPlayerFirst);
        }
    }

    public ArrayList<Positon> getPossiblePositons(Positon positon, boolean searchUpward) {
        ArrayList<Positon> positons = new ArrayList<>();
        int x = positon.getX();
        int y = positon.getY();

        if (searchUpward) {
            int i = 1;
            while (x-i > 0 && y-i > 0) {
                if (!tileMatrix[y-i][x-i].isOccupied()) {
                    positons.add(new Positon(x-i, y-i));
                } else {
                    break;
                }
                i++;
            }

            i = 1;
            while (y-i > 0) {
                if (!tileMatrix[y-i][x].isOccupied()) {
                    positons.add(new Positon(x, y-i));
                } else {
                    break;
                }
                i++;
            }

            i = 1;
            while (y-i > 0 && x+i < 7) {
                if (!tileMatrix[y-i][x+i].isOccupied()) {
                    positons.add(new Positon(x+i, y-i));
                } else {
                    break;
                }
                i++;
            }

        } else {
            int i = 1;
            while (x+i < 7 && y+i < 7) {
                if (!tileMatrix[y+i][x+i].isOccupied()) {
                    positons.add(new Positon(x+i, y+i));
                } else {
                    break;
                }
                i++;
            }

            i = 1;
            while (y+i < 7) {
                if (!tileMatrix[y+i][x].isOccupied()) {
                    positons.add(new Positon(x, y+i));
                } else {
                    break;
                }
                i++;
            }

            i = 1;
            while (y+i < 7 && x-i > 0) {
                if (!tileMatrix[y+i][x-i].isOccupied()) {
                    positons.add(new Positon(x-i, y+i));
                } else {
                    break;
                }
                i++;
            }
        }

        return positons;
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
