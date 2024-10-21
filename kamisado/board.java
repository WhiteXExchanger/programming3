package kamisado;

import java.io.Serializable;

class Board implements Serializable {
    private Tile[][] tileMatrix;
    private TeamEnum actualPlayer;

    Board(boolean isBottomBlack) {
        actualPlayer = isBottomBlack ? TeamEnum.BLACK : TeamEnum.WHITE; // if this is false -> ai is present
        tileMatrix = new Tile[8][8];
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

        switch (actualPlayer) {
            case WHITE -> {
                placeBlack(topIndex);
                placeWhite(bottomIndex);
            }
            case BLACK -> {
                placeWhite(topIndex);
                placeBlack(bottomIndex);
            }
        }
    }

    void placeBlack(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.BLACK, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    void placeWhite(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.WHITE, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    public void changeActivePlayer() {
        if (actualPlayer == TeamEnum.BLACK)
            actualPlayer = TeamEnum.WHITE;
        else 
            actualPlayer = TeamEnum.BLACK;
        unflagBoard();
    }

    public void unflagBoard() {
        for (Tile[] tiles : tileMatrix) {
            for (Tile tile : tiles) {
                tile.unflag();
            }
        }
    }

    public Tile[][] getTileMatrix() {
        return tileMatrix;
    }

    public boolean isOccupied(Position positon) {
        return tileMatrix[positon.getY()][positon.getX()].isOccupied();
    }
    
    public boolean movePiece(Position from, Position to) {
        Piece piece = tileMatrix[from.getY()][from.getX()].getPiece();
        if (piece == null) return false;
        Tile tile = tileMatrix[to.getY()][to.getX()];
        
        System.out.println(from);
        System.out.println(to);
        if (tile.isFlagged()) {
            int x = from.getX();
            int y = from.getY();
            tile.setPiece(new Piece(piece));
            tileMatrix[y][x].clearPiece();
            System.out.println("moved");
            return true;
        }
        System.out.println("stand still");
        return false;
    }

    public Position getNextPiecePosition(Position actualPostion) {
        ColorEnum nextColor = tileMatrix[actualPostion.getY()][actualPostion.getX()].getColor();
        
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                Piece piece = tileMatrix[i][j].getPiece();
                if (piece == null) continue;
                if (piece.getTeam() == actualPlayer && piece.getColor() == nextColor) {
                    return new Position(j, i);
                }
            }
        }
        return null;
    }

    public boolean flagTiles(Position position) {
        int length = tileMatrix[position.getY()][position.getX()].getPiece().getMovementLength();
        int x = position.getX();
        int y = position.getY();
        int direction = 0; // -1 for downward, 1 for upward
        if (actualPlayer == TeamEnum.BLACK)
            direction = 1;
        else
            direction = -1;
    
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
        
        /*  debug
        for ( Tile[] tiles : tileMatrix) {
            for (int i = 0; i < tiles.length; i++) {
                System.out.print(tiles[i].isFlagged() + "\t");
            }
            System.out.println("");
        } */
    }

    public boolean isSameTeam(Position from, Position to) {
        if (from == null || to == null) return false;
        Piece piece = tileMatrix[from.getY()][from.getX()].getPiece();
        Piece otherPiece = tileMatrix[to.getY()][to.getY()].getPiece();
        return (piece != null && otherPiece != null && piece.getTeam() == otherPiece.getTeam());
    }

    public int isGameOver(Position to) {
        if (actualPlayer == TeamEnum.BLACK && to.getY() == 0) {
            tileMatrix[to.getY()][to.getX()].getPiece().increaseDragonTeeth();
            return 1;
        }
        else if (actualPlayer == TeamEnum.WHITE && to.getY() == 7) {
            tileMatrix[to.getY()][to.getX()].getPiece().increaseDragonTeeth();
            return -1;
        }
        return 0;
    }

}
