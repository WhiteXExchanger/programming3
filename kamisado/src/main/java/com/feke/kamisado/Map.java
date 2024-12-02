package com.feke.kamisado;

import java.io.Serializable;

/**
 * Represents the game map, which includes the board's tiles and manages the pieces.
 * Implements Serializable to allow the map to be saved and restored.
 */
public class Map implements Serializable {

    private Tile[][] tileMatrix = new Tile[8][8];
    private boolean blackOnBottom = true;
    private boolean isBotPlaying = false;

    /**
     * Constructs a Map object with the specified setting for bot involvement.
     *
     * @param isBotPlaying boolean indicating whether a bot is involved in the game.
     */
    Map(boolean isBotPlaying) {
        this.isBotPlaying = isBotPlaying;
        placeTiles();
        placePieces();
    }

    /**
     * Constructs a Map object from a given tile matrix.
     *
     * @param tileMatrix the matrix of tiles to use for this Map.
     */
    Map(Tile[][] tileMatrix) {
        this.tileMatrix = tileMatrix;
    }

    /**
     * Places tiles on the board in their appropriate positions.
     */
    private void placeTiles() {
        // The specific tile arrangement is based on certain color patterns.
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
    }

    /**
     * Places the pieces on the board according to their respective positions.
     * White pieces are placed on one side, black pieces on the other.
     */
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

    /**
     * Gets the coordinate of the currently selected tile.
     *
     * @return The coordinate of the selected tile, or null if no tile is selected.
     */
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

    /**
     * Places black pieces on the specified row.
     *
     * @param lineIndex The index of the row where black pieces should be placed.
     */
    private void placeBlack(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.BLACK, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    /**
     * Gets the map matrix for visualization purposes.
     *
     * @return A 2D array of Tile objects representing the map.
     */
    public Tile[][] getMap() {
        return tileMatrix;
    }

    /**
     * Places white pieces on the specified row.
     *
     * @param lineIndex The index of the row where white pieces should be placed.
     */
    private void placeWhite(int lineIndex) {
        for (int i = 0; i < tileMatrix.length; i++) {
            Piece piece = new Piece(TeamEnum.WHITE, tileMatrix[lineIndex][i].getColor());
            tileMatrix[lineIndex][i].setPiece(piece);
        }
    }

    /**
     * Moves a piece from the current coordinate to the next coordinate if the move is valid.
     *
     * @param currCoord The current coordinate of the piece.
     * @param nextCoord The target coordinate for the move.
     * @return True if the move is successful, false otherwise.
     */
    public boolean movePiece(Coordinate currCoord, Coordinate nextCoord) {
        Tile tile = getTile(nextCoord);
        Piece piece = getPiece(currCoord);
        if (!tile.isFlagged() || piece == null) return false;

        tile.setPiece(piece);
        tileMatrix[currCoord.getY()][currCoord.getX()].clearPiece();
        return true;
    }

    /**
     * Resets the map and repositions pieces.
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

    /**
     * Checks if two pieces have the same team and color.
     *
     * @param p1 The first piece.
     * @param p2 The second piece.
     * @return True if both pieces have the same team and color, false otherwise.
     */
    private boolean isSamePiece(Piece p1, Piece p2) {
        if (p1 == null) return false;
        return p1.getTeam() == p2.getTeam() && p1.getColor() == p2.getColor();
    }

    /**
     * Retrieves all pieces from the map.
     *
     * @return An array containing all the pieces currently on the map.
     */
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

    /**
     * Searches for the piece with the specified color and team.
     *
     * @param color The color of the piece.
     * @param team  The team of the piece.
     * @return The coordinate of the piece, or null if it isn't found.
     */
    private Coordinate getPieceCoordinate(ColorEnum color, TeamEnum team) {
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                Piece piece = tileMatrix[i][j].getPiece();
                if (piece != null && piece.getTeam() == team && piece.getColor() == color) {
                    return new Coordinate(j, i);
                }
            }
        }
        return null;
    }

    /**
     * Returns the next piece's coordinate based on the provided piece's color and opposing team.
     *
     * @param coord The coordinate of the current piece.
     * @return The coordinate of the next piece.
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

    /**
     * Flags the tiles that can be moved to from the provided coordinate.
     *
     * @param coord The coordinate of the piece that is being moved.
     * @return True if valid moves are available, false otherwise.
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
        int direction = blackOnBottom ? (piece.getTeam() == TeamEnum.BLACK ? 1 : -1)
                                      : (piece.getTeam() == TeamEnum.BLACK ? -1 : 1);

        // Evaluate possible movement tiles
        return (flagDiagonalRight(x, y, direction, length) +
                flagVertical(y, x, direction, length) +
                flagDiagonalLeft(x, y, direction, length)) > 0;
    }

    /**
     * Unflags all tiles in the map, resetting their status to unflagged.
     */
    public void unflagTiles() {
        for (Tile[] tiles : tileMatrix) {
            for (Tile tile : tiles) {
                tile.unflag();
            }
        }
    }

    /**
     * Checks whether a piece has reached the enemy's base line to determine if the turn is over.
     *
     * @return True if a piece has reached the enemy's base line, false otherwise.
     */
    public boolean isTurnOver() {
        Piece investigatedPiece;
        TeamEnum team;

        // Check the top row
        for (int i = 0; i < tileMatrix.length; i++) {
            investigatedPiece = tileMatrix[0][i].getPiece();
            if (investigatedPiece == null) continue;
            team = investigatedPiece.getTeam();

            if ((blackOnBottom && team == TeamEnum.BLACK) || (!blackOnBottom && team == TeamEnum.WHITE)) {
                investigatedPiece.increaseDragonTeeth();
                if (!isBotPlaying) blackOnBottom = team != TeamEnum.BLACK;
                return true;
            }
        }

        // Check the bottom row
        for (int i = 0; i < tileMatrix.length; i++) {
            investigatedPiece = tileMatrix[7][i].getPiece();
            if (investigatedPiece == null) continue;
            team = investigatedPiece.getTeam();

            if ((blackOnBottom && team == TeamEnum.WHITE) || (!blackOnBottom && team == TeamEnum.BLACK)) {
                investigatedPiece.increaseDragonTeeth();
                if (!isBotPlaying) blackOnBottom = team != TeamEnum.BLACK;
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the tile at the specified coordinate.
     *
     * @param coord The coordinate of the tile to retrieve.
     * @return The tile located at the specified coordinate.
     */
    private Tile getTile(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()];
    }

    /**
     * Retrieves the piece at the specified coordinate.
     *
     * @param coord The coordinate of the piece to retrieve.
     * @return The piece located at the specified coordinate, or null if no piece is present.
     */
    private Piece getPiece(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece();
    }

    /**
     * Gets the number of dragon teeth collected by the piece at the specified coordinate.
     *
     * @param coord The coordinate of the piece whose dragon teeth count is to be retrieved.
     * @return The number of dragon teeth collected by the piece.
     */
    public int getDragonTeeth(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece().getDragonTeeth();
    }

    /**
     * Gets the movement length of the piece at the specified coordinate.
     *
     * @param coord The coordinate of the piece whose movement length is to be retrieved.
     * @return The movement length of the piece.
     */
    public int getMovementLength(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getPiece().getMovementLength();
    }

    /**
     * Gets the team of the piece at the specified coordinate.
     *
     * @param coord The coordinate of the piece whose team is to be retrieved.
     * @return The team of the piece, or TeamEnum.NONE if no piece is present.
     */
    public TeamEnum getTeam(Coordinate coord) {
        Piece piece = tileMatrix[coord.getY()][coord.getX()].getPiece();
        if (piece == null) {
            return TeamEnum.NONE;
        }
        return piece.getTeam();
    }

    /**
     * Gets the color of the tile at the specified coordinate.
     *
     * @param coord The coordinate of the tile whose color is to be retrieved.
     * @return The color of the tile.
     */
    public ColorEnum getTileColor(Coordinate coord) {
        return tileMatrix[coord.getY()][coord.getX()].getColor();
    }

    /**
     * Flags the diagonal right movement from a given starting coordinate, marking all valid tiles.
     *
     * @param x         The starting x-coordinate.
     * @param y         The starting y-coordinate.
     * @param direction The direction multiplier for movement (-1 or 1).
     * @param length    The maximum movement length for the piece.
     * @return The number of valid tiles flagged.
     */
    private int flagDiagonalRight(int x, int y, int direction, int length) {
        int tileCounter = 0;

        for (int i = 1; x + i * direction < 8 && y - i * direction < 8 &&
                x + i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x + direction * i];
            if (tile.isOccupied()) break;
            tile.flag();
            tileCounter++;
        }

        return tileCounter;
    }

    /**
     * Flags the vertical movement from a given starting coordinate, marking all valid tiles.
     *
     * @param y         The starting y-coordinate.
     * @param x         The x-coordinate of the starting tile.
     * @param direction The direction multiplier for movement (-1 or 1).
     * @param length    The maximum movement length for the piece.
     * @return The number of valid tiles flagged.
     */
    private int flagVertical(int y, int x, int direction, int length) {
        int tileCounter = 0;

        for (int i = 1; y - i * direction < 8 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x];
            if (tile.isOccupied()) break;
            tile.flag();
            tileCounter++;
        }

        return tileCounter;
    }

    /**
     * Flags the diagonal left movement from a given starting coordinate, marking all valid tiles.
     *
     * @param x         The starting x-coordinate.
     * @param y         The starting y-coordinate.
     * @param direction The direction multiplier for movement (-1 or 1).
     * @param length    The maximum movement length for the piece.
     * @return The number of valid tiles flagged.
     */
    private int flagDiagonalLeft(int x, int y, int direction, int length) {
        int tileCounter = 0;

        for (int i = 1; x - i * direction < 8 && y - i * direction < 8 &&
                x - i * direction >= 0 && y - i * direction >= 0 && i <= length; i++) {
            Tile tile = tileMatrix[y - direction * i][x - direction * i];
            if (tile.isOccupied()) break;
            tile.flag();
            tileCounter++;
        }

        return tileCounter;
    }
}
