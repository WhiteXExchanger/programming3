package com.feke.kamisado;

import java.util.ArrayList;

/**
 * Represents a position on the game board.
 * Evaluates and manages the position of pieces for gameplay purposes.
 */
public class Position {
    private Map simulatedMap;
    private Coordinate coordinate;

    /**
     * Constructs a Position object using the given matrix and coordinate.
     *
     * @param matrix     The tile matrix representing the board state.
     * @param coordinate The coordinate of the position.
     */
    Position(Tile[][] matrix, Coordinate coordinate) {
        simulatedMap = new Map(matrix);
        this.coordinate = coordinate;
    }

    /**
     * Constructs a Position object by moving a piece from one coordinate to another.
     *
     * @param matrix The tile matrix representing the board state.
     * @param coord1 The original coordinate of the piece.
     * @param coord2 The destination coordinate for the piece.
     */
    Position(Tile[][] matrix, Coordinate coord1, Coordinate coord2) {
        matrix[coord2.getY()][coord2.getX()].setPiece(matrix[coord1.getY()][coord1.getX()].getPiece());
        matrix[coord1.getY()][coord1.getX()].clearPiece();
        simulatedMap = new Map(matrix);
        coordinate = getNextPieceCoord(coord2);
    }

    /**
     * Returns a string representation of the position.
     *
     * @return A string representation of the coordinate.
     */
    public String toString() {
        return coordinate + " ";
    }

    /**
     * Gets the coordinate of this position.
     *
     * @return The coordinate of this position.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Evaluates the position to determine its value for decision-making in gameplay.
     *
     * @return The evaluation score of this position.
     */
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

    /**
     * Assigns a value to the given piece position based on its state.
     *
     * @param modifier Modifier to adjust the score (positive or negative).
     * @param coord    The coordinate of the piece being evaluated.
     * @return The calculated value for the given piece position.
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

    /**
     * Checks if this position is a game-ending position for a piece.
     *
     * @return True if the piece is in a game-ending position, false otherwise.
     */
    public boolean isEndOfGame() {
        return inEndingPosition(coordinate);
    }

    /**
     * Checks if the given coordinate blocks an enemy piece.
     *
     * @param coord The coordinate to check.
     * @return True if the piece blocks an enemy piece, false otherwise.
     */
    private boolean blockingEnemyPiece(Coordinate coord) {
        return false; // Placeholder implementation.
    }

    /**
     * Checks if the piece on the provided coordinate is in a game-ending position.
     *
     * @param coord The coordinate of the piece.
     * @return True if the piece is in a game-ending position, false otherwise.
     */
    private boolean inEndingPosition(Coordinate coord) {
        return simulatedMap.getTeam(coord) == TeamEnum.BLACK && coord.getY() == 0 ||
               simulatedMap.getTeam(coord) == TeamEnum.WHITE && coord.getY() == 7;
    }

    /**
     * Checks if the piece on the provided coordinate is in a position where it can make a game-ending move.
     *
     * @param coord The coordinate of the piece.
     * @return True if the piece can make a game-ending move, false otherwise.
     */
    private boolean inEndingMovePosition(Coordinate coord) {
        ArrayList<Coordinate> coords = getValidMovements(coord);
        boolean isEndingMove = false;
        boolean isWhite = simulatedMap.getTeam(coord) == TeamEnum.WHITE;

        for (Coordinate c : coords) {
            if ((isWhite && c.getY() == 7) || (!isWhite && c.getY() == 0)) {
                isEndingMove = true;
                break;
            }
        }

        return isEndingMove;
    }

    /**
     * Gets all possible positions that can be reached from the current position.
     *
     * @return A list of possible positions from this position.
     */
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

    /**
     * Gets all valid movements from the given coordinate.
     *
     * @param coord The coordinate of the piece.
     * @return A list of coordinates representing valid movements.
     */
    public ArrayList<Coordinate> getValidMovements(Coordinate coord) {
        ArrayList<Coordinate> coords = new ArrayList<>();
        int length = simulatedMap.getMovementLength(coord);
        int direction = simulatedMap.getTeam(coord) == TeamEnum.WHITE ? -1 : 1;
        int x = coord.getX();
        int y = coord.getY();

        // Diagonal (right)
        for (int i = 1; x + i * direction < 8 && y - i * direction < 8 &&
             x + i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x + direction * i];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x + direction * i, y - direction * i));
        }

        // Vertical
        for (int i = 1; y - i * direction < 8 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x, y - direction * i));
        }

        // Diagonal (left)
        for (int i = 1; x - i * direction < 8 && y - i * direction < 8 &&
             x - i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = simulatedMap.getMap()[y - direction * i][x - direction * i];
            if (tile.isOccupied()) break;
            coords.add(new Coordinate(x - direction * i, y - direction * i));
        }

        return coords;
    }

    /**
     * Gets the next piece coordinate with the same color but from the opposite team as the given coordinate.
     *
     * @param coord The current coordinate of the piece.
     * @return The coordinate of the next piece, or (-1, -1) if none found.
     */
    private Coordinate getNextPieceCoord(Coordinate coord) {
        TeamEnum opposedTeam = simulatedMap.getTeam(coord) == TeamEnum.BLACK ? TeamEnum.WHITE : TeamEnum.BLACK;
        ColorEnum color = simulatedMap.getTileColor(coord);

        for (int i = 0; i < simulatedMap.getMap().length; i++) {
            for (int j = 0; j < simulatedMap.getMap().length; j++) {
                Tile tile = simulatedMap.getMap()[i][j];
                Piece piece = tile.getPiece();
                if (piece != null && piece.getTeam() == opposedTeam && piece.getColor() == color) {
                    return new Coordinate(j, i);
                }
            }
        }

        return new Coordinate(-1, -1);
    }
}
