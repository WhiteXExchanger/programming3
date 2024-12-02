package com.feke.kamisado;

import java.io.Serializable;

public class Map implements Serializable {

    private Tile[][] tileMatrix = new Tile[8][8];;
    private boolean blackOnBottom = true;
    private boolean isBotPlaying = false;

    Map(boolean isBotPlaying) {
        this.isBotPlaying = isBotPlaying;
        placeTiles();
        placePieces();
    }

    Map(Tile[][] tileMatrix) {
        this.tileMatrix = tileMatrix;
    }

    private void placeTiles() {

        // ---------- www.github.com/mandriv/kamisado/blob/master/src/kamisado/logic/Board.java ----------
        int[] counters = {0, 1, 2, 3, 4, 5, 6, 7};
        for (Tile[] rows : tileMatrix) {
            rows[counters[0]] = new Tile(ColorEnum.ORANGE);
            counters[0] = (counters[0] + 1) % 8;
            rows[counters[1]] = new Tile(ColorEnum.BLUE);
            counters[1] = (counters[1] + 11) % 8;
            rows[counters[2]] = new Tile(ColorEnum.PURPLE);
            counters[2] = (counters[2] + 5) % 8;
            rows[counters[3]] = new Tile(ColorEnum.PINK);
            counters[3] = (counters[3] + 7) % 8;
            rows[counters[4]] = new Tile(ColorEnum.YELLOW);
            counters[4] = (counters[4] + 1) % 8;
            rows[counters[5]] = new Tile(ColorEnum.RED);
            counters[5] = (counters[5] + 3) % 8;
            rows[counters[6]] = new Tile(ColorEnum.GREEN);
            counters[6] = (counters[6] + 5) % 8;
            rows[counters[7]] = new Tile(ColorEnum.BROWN);
            counters[7] = (counters[7] + 7) % 8;
        }
        // -----------------------------------------------------------------------------------------------
    }

    private void placePieces() {
        int topIndex = 0;
        int bottomIndex = 7;

        if (blackOnBottom) {
            placeWhite(topIndex);
            placeBlack(bottomIndex);
        } else {
            placeBlack(topIndex);
            placeWhite(bottomIndex);
        }
    }

    // Returns the coordiante of the selected Tile if there is one, otherwise it returns null
    public Coordinate getSelected() {
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                if (tileMatrix[i][j].isSelected()) {
                    return new Coordinate(j, i);
                }
            }
        }
        
        return null;
    }

    // Places the black pieces on the inputed row
    private void placeBlack(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.BLACK, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    // Returns the map for visualization
    public Tile[][] getMap() {
        return tileMatrix;
    }
    
    // Places the white pieces on the inputed row
    private void placeWhite(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.WHITE, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    /*  Tests if the inputed currCoord has a piece, and if the nextCoord is flagged for movement,
        if it is moves it to the new coordinate and returns true,
        otherwise it returns false */
    public boolean movePiece(Coordinate currCoord, Coordinate nextCoord) {
        Tile tile = getTile(nextCoord);
        Piece piece = getPiece(currCoord);
        if (!tile.isFlagged() || piece == null) return false;
        
        tile.setPiece(piece);
        tileMatrix[currCoord.getY()][currCoord.getX()].clearPiece();
        return true;
    }

    /*
     * Resets the map, then copies the pieces to the new map
     */
    public void resetMap() {
        Piece[] pieces = getAllPieces();
        
        placeTiles();
        placePieces();

        for (Tile tile : tileMatrix[0]) {
            for (Piece piece : pieces) {
                if (isSamePiece(tile.getPiece(), piece)) {
                    tile.setPiece(piece);
                }
            }
        }
        for (Tile tile : tileMatrix[7]) {
            for (Piece piece : pieces) {
                if (isSamePiece(tile.getPiece(), piece)) {
                    tile.setPiece(piece);
                }
            }
        }
    }

    /* Checks if the provided pieces have the same team and color */
    private boolean isSamePiece(Piece p1, Piece p2) {
        if (p1 == null) return false;
        return p1.getTeam() == p2.getTeam() && p1.getColor() == p2.getColor();
    }

    /* Returns all pieces from the map */
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

    /* Searches the map for the piece which has the provided color and team */
    private Coordinate getPieceCoordinate(ColorEnum color, TeamEnum team) {
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                Piece piece = tileMatrix[i][j].getPiece();
                if (piece == null) { }
                else if (piece.getTeam() == team && piece.getColor() == color) {
                    return new Coordinate(j, i);
                }
            }
        }
        return null;
    }

    /*  Returns the coordinate of the next piece based on the
        - color of the tile 
        - the opposite team of the piece 
        which are on the provided coordinate 
    */
    public Coordinate getNextPieceCoordinate(Coordinate coord) {
        ColorEnum color = getTileColor(coord);
        TeamEnum team = getPiece(coord).getTeam();
        
        team = team == TeamEnum.BLACK ? TeamEnum.WHITE : TeamEnum.BLACK;
        coord = getPieceCoordinate(color, team);
        while (!selectTile(coord)) {
            team = team == TeamEnum.BLACK ? TeamEnum.WHITE : TeamEnum.BLACK;
            color = getTileColor(coord);
            coord = getPieceCoordinate(color, team);
        }
        
        return coord;
    }

    /*
     * This method is resposible for traversing the map and flagging the tiles,
     * which are valid movement options for the provided coordinate's piece.
     * If there are none it returns false, otherwise true.
     */
    public boolean selectTile(Coordinate coord) {
        this.unflagTiles();

        Tile startingTile = tileMatrix[coord.getY()][coord.getX()];
        Piece piece = startingTile.getPiece();
        if (piece == null) return false;
        startingTile.select();
        int length = piece.getMovementLength();
        int x = coord.getX();
        int y = coord.getY();
        int direction; // -1 for downward, 1 for upward            
        if (blackOnBottom) {
            direction = piece.getTeam() == TeamEnum.BLACK ? 1 : -1;
        } else {
            direction = piece.getTeam() == TeamEnum.BLACK ? -1 : 1;
        }
    
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
    
    /* Removes the flagged state from all tiles */
    public void unflagTiles() {
        for (Tile[] tiles : tileMatrix) {
            for (Tile tile : tiles) {
                tile.unflag();
            }
        }
    }

    /* Checks if a piece moved to the enemy's base line */
    public boolean isTurnOver() {
        Piece investigatedPiece;
        TeamEnum team;
        for (int i = 0; i < tileMatrix.length; i++) {
            investigatedPiece = tileMatrix[0][i].getPiece();
            if (investigatedPiece == null) continue;
            team = investigatedPiece.getTeam();
            if (blackOnBottom && team == TeamEnum.BLACK || !blackOnBottom && team == TeamEnum.WHITE) {
                investigatedPiece.increaseDragonTeeth();
                if (!isBotPlaying) blackOnBottom = team != TeamEnum.BLACK;
                return true;
            }
            
            investigatedPiece = tileMatrix[7][i].getPiece();
            if (investigatedPiece == null) continue;
            team = investigatedPiece.getTeam();
            if (blackOnBottom && team == TeamEnum.WHITE || !blackOnBottom && team == TeamEnum.BLACK) {
                investigatedPiece.increaseDragonTeeth();
                if (!isBotPlaying) blackOnBottom = team != TeamEnum.BLACK;
                return true;
            }
        }
        return false;
    }

    /* Returns the tile from the provided coordinate */
    private Tile getTile(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()];
    }

    /* Returns the piece from the provided coordinate */
    private Piece getPiece(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece();
    }

    /* Returns the amount of teeth the piece has at the provided coordinate */
    public int getDragonTeeth(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece().getDragonTeeth();
    }

    /* Returns the value a piece can move at the provided coordinate */
    public int getMovementLength(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece().getMovementLength();
    }

    /* Returns the team of the piece at the provided coordinate */
    public TeamEnum getTeam(Coordinate coord) {
        Piece piece = tileMatrix[coord.getY()][coord.getX()].getPiece();
        if (piece == null) {
            return TeamEnum.NONE;
        }
        return piece.getTeam();
    }

    /* Returns the color of the tile at the provided coordinate */
    public  ColorEnum getTileColor(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getColor();
    }
}
