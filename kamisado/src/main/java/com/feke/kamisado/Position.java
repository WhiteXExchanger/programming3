package com.feke.kamisado;

import java.util.ArrayList;

public class Position {
    private Map simulatedMap;
    private Coordinate coordinate;

    Position(Tile[][] matrix, Coordinate coordinate) {
        simulatedMap = new Map(matrix);
        this.coordinate = coordinate;
    }

    Position(Tile[][] matrix, Coordinate coord1, Coordinate coord2) {
        matrix[coord2.getY()][coord2.getX()].setPiece(matrix[coord1.getY()][coord1.getX()].getPiece());
        matrix[coord1.getY()][coord1.getX()].clearPiece();
        simulatedMap = new Map(matrix);
        coordinate = getNextPieceCoord(coord2);
    }

    public String toString() {
        return coordinate+" ";
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }


    public int getEvaluation() {
        int evalOfPosition = 0;

        Tile[][] matrix = simulatedMap.getMap();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                Piece piece = matrix[i][j].getPiece();
                if (piece == null) {
                    continue;
                }
                if (piece.getTeam() == TeamEnum.WHITE) {
                    evalOfPosition += calculateEvaluationForPiecePosition(1, new Coordinate(j, i));
                } else {
                    evalOfPosition += calculateEvaluationForPiecePosition(-1, new Coordinate(j, i));
                }
            }
        }

        return evalOfPosition;
    }

    /*  Assings value to the postion provided. Modifier makes it negative/positive.
        Coordinate is provided to determine what is the value of the individual piece in the current postion. 
    */
    private int calculateEvaluationForPiecePosition(int modifier, Coordinate coord) {
        int value = 0;
        if (inEndingPosition(coord)) 
            value += 1000 * modifier * simulatedMap.getDragonTeeth(coord) + 1;
        if (inEndingMovePosition(coord))
            value += 80 * modifier;
        if (blockingEnemyPiece(coord))
            value += 10 * modifier;
        return value;
    }

    // Checks if this position's piece is in a "game ending" postion or not
    public boolean isEndOfGame() {
        return inEndingPosition(coordinate);
    }

    // Checks if this position's piece is in coordinate where he blocks one, or more of the enemy's pieces
    private boolean blockingEnemyPiece(Coordinate coord) {
        return false;
    }

    // Checks if the piece on the provided coordinate is in a "game ending" postion or not
    private boolean inEndingPosition(Coordinate coord) {
        return simulatedMap.getTeam(coord) == TeamEnum.BLACK && coord.getY() == 0 || 
            simulatedMap.getTeam(coord) == TeamEnum.WHITE && coord.getY() == 7;
    }

    // Checks if the piece on the provided coordinate is in a "game ending move" postion or not
    private boolean inEndingMovePosition(Coordinate coord) {
        ArrayList<Coordinate> coords = getValidMovements(coord);
        boolean isEndingMove = false;
        boolean isWhite = false;
        if (simulatedMap.getTeam(coord) == TeamEnum.WHITE) {
            isWhite = true;
        } 

        for (Coordinate c : coords) {
            if (isWhite) {
                if (c.getY() == 7) {
                    isEndingMove = true;
                }
            } else {
                if (c.getY() == 0) {
                    isEndingMove = true;
                }
            }
        }

        return isEndingMove;
    }

    // Returns a list which has all the positions which are available from this position
    public ArrayList<Position> getPossiblePositions() {
        ArrayList<Position> positions = new ArrayList<>();
        ArrayList<Coordinate> movements = getValidMovements(coordinate);
        for (Coordinate coord : movements) {
            Tile[][] matrix = new Tile[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    matrix[i][j] = new Tile(simulatedMap.getMap()[i][j]);
                }
            }
            positions.add(new Position(matrix, coordinate, coord));
        }

        return positions;
    }

    // Returns a list which has all the coordinates which are available from this position
    public ArrayList<Coordinate> getValidMovements(Coordinate coord) {
        ArrayList<Coordinate> coords = new ArrayList<>();

        int length = simulatedMap.getMovementLength(coord);

        int direction;
        if (simulatedMap.getTeam(coord) == TeamEnum.WHITE) {
            direction = -1;
        } else {
            direction = 1;
        }
        int x = coord.getX();
        int y = coord.getY();

        // Diagonal (right)
        for (int i = 1; x + i * direction < 8 && y - i * direction < 8
        && x + i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x + direction * i];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x + direction * i, y - direction * i));
        }
    
        // Vertical
        for (int i = 1; y - i * direction < 8 && y - i * direction >= 0
        && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x, y - direction * i));
        }
    
        // Diagonal (left)
        for (int i = 1; x - i * direction < 8 && y - i * direction < 8
        && x - i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x - direction * i];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x - direction * i, y - direction * i));
        }

        return coords;
    }

    // Returns the coordinate of the next piece, which has the same color of the tile thats on the provided coordinate 
    private Coordinate getNextPieceCoord(Coordinate coord) {
        TeamEnum oposedTeam = simulatedMap.getTeam(coord);
        ColorEnum color = simulatedMap.getTileColor(coord);

        for (int i = 0; i < simulatedMap.getMap().length; i++) {
            for (int j = 0; j < simulatedMap.getMap().length; j++) {
                Tile tile = simulatedMap.getMap()[i][j];
                if (tile.getPiece() != null && tile.getPiece().getTeam() != oposedTeam && tile.getPiece().getColor() == color) {
                    return new Coordinate(j, i);
                }
            }
        }

        return new Coordinate(-1,-1);
    }
}