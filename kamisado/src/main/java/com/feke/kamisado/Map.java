package com.feke.kamisado;

import java.util.logging.Logger;

public class Map {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private Tile[][] tileMatrix;
    private boolean orientation;

    Map(boolean orientation) {
        tileMatrix = new Tile[8][8];
        this.orientation = orientation;
        placeTiles();
        placePieces(orientation);
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

    void placePieces(boolean orientation) {
        int topIndex = 0;
        int bottomIndex = 7;

        if (orientation) {
            placeWhite(topIndex);
            placeBlack(bottomIndex);
        } else {
            placeBlack(topIndex);
            placeWhite(bottomIndex);
        }
    }

    void placeBlack(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.BLACK, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    public Tile[][] getMap() {
        return tileMatrix;
    }

    void placeWhite(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.WHITE, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    public boolean moveOnMap(Position position, Position nextPosition) {
        Piece piece = tileMatrix[position.getY()][position.getX()].getPiece();
        if (piece == null) return false;
        Tile tile = tileMatrix[nextPosition.getY()][nextPosition.getX()];
        
        System.out.println(position);
        System.out.println(nextPosition);
        if (tile.isFlagged()) {
            int x = position.getX();
            int y = position.getY();
            tile.setPiece(piece);
            tileMatrix[y][x].clearPiece();
            System.out.println("moved");
            return true;
        }
        System.out.println("stand still");
        return false;
    }

    public void ended(Position position, boolean orientation) {
        this.orientation = orientation;
        tileMatrix[position.getY()][position.getX()].getPiece().increaseDragonTeeth();
        resetTiles(orientation);
    }

    private void resetTiles(boolean orientation) {
        Piece[] pieces = getAllPieces();
        placeTiles();
        placePieces(orientation);
        for (Tile tile : tileMatrix[0]) {
            for (Piece piece : pieces) {
                if (isSamePiece(tile.getPiece(), piece)) {
                    logger.info(""+piece);
                    tile.setPiece(piece);
                }
            }
        }
        for (Tile tile : tileMatrix[7]) {
            for (Piece piece : pieces) {
                if (isSamePiece(tile.getPiece(), piece)) {
                    logger.info(""+piece);
                    tile.setPiece(piece);
                }
            }
        }
    }

    private boolean isSamePiece(Piece p1, Piece p2) {
        if (p1 == null) return false;
        return p1.getTeam() == p2.getTeam() && p1.getColor() == p2.getColor();
    }

    private Piece[] getAllPieces() {
        Piece[] pieces = new Piece[16];
        int i = 0;
        for (Tile[] tiles : tileMatrix) {
            for (Tile tile : tiles) {
                if (tile.isOccupied()) {
                    pieces[i] = tile.getPiece();
                    i++;
                }
            }
        }
        return pieces;
    }

    public boolean flagTiles(Position position, boolean isFirstPlayer) {
        Tile startingTile = tileMatrix[position.getY()][position.getX()];
        Piece piece = startingTile.getPiece();
        if (piece == null) return false;
        startingTile.select();
        int length = piece.getMovementLength();
        int x = position.getX();
        int y = position.getY();
        int direction = isFirstPlayer ? 1 : -1; // -1 for downward, 1 for upward
    
        int tileCounter = 0;
        // Diagonal (right)
        for (int i = 1; x + i * direction < 8 && y - i * direction < 8
        && x + i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x + direction * i];
            if (tile.isOccupied()) break;
            tile.flag();
            tileCounter++;
        }
    
        // Vertical
        for (int i = 1; y - i * direction < 8 && y - i * direction >= 0
        && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x];
            if (tile.isOccupied()) break; 
            tile.flag();
            tileCounter++;
        }
    
        // Diagonal (left)
        for (int i = 1; x - i * direction < 8 && y - i * direction < 8
        && x - i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x - direction * i];
            if (tile.isOccupied()) break; 
            tile.flag();
            tileCounter++;
        }

        return (tileCounter > 0);
    }
    
    public void unflagTiles() {
        for (Tile[] tiles : tileMatrix) {
            for (Tile tile : tiles) {
                tile.unflag();
            }
        }
    }

    public Piece getPiece(Position pos) {
        return tileMatrix[pos.getY()][pos.getX()].getPiece();
    }

    public ColorEnum getColor(Position pos) {
        return tileMatrix[pos.getY()][pos.getX()].getColor();
    }
}
