package com.feke.kamisado;

import java.io.Serializable;

/**
 * The Board class represents the game board and handles all game logic,
 * including player and bot interactions, scoring, and turn management.
 */
public class Board implements Serializable {

    private static final Bot ai = new Bot();
    private final Map map;
    private TeamEnum currentPlayer = TeamEnum.BLACK;
    private Coordinate activeCoordinate = null;
    private boolean isBotPlaying = false;
    private boolean isFirstMove = true;
    private int[] points = {0, 0};

    /**
     * Constructs a Board with the specified bot-playing option.
     *
     * @param isBotPlaying whether a bot will be playing the game.
     */
    Board(boolean isBotPlaying) {
        this.map = new Map(isBotPlaying);
        this.isBotPlaying = isBotPlaying;
    }

    /**
     * Handles interaction with a tile on the board.
     * Updates the active piece or attempts to move it.
     *
     * @param coord the coordinate of the tile being interacted with.
     */
    public void interact(Coordinate coord) {
        if (currentPlayer == map.getTeam(coord) && isFirstMove) {
            activeCoordinate = coord;
            map.selectTile(activeCoordinate);
        } else if (activeCoordinate != null && map.movePiece(activeCoordinate, coord)) {
            isFirstMove = false;
            if (map.isTurnOver()) {
                endOfTurn(coord);
                return;
            }
            activeCoordinate = map.getNextPieceCoordinate(coord);
            currentPlayer = map.getTeam(activeCoordinate);

            // Trigger bot move if applicable
            if (isBotPlaying && currentPlayer == TeamEnum.WHITE) useBot(activeCoordinate);
        }
    }

    /**
     * Executes the bot's move based on the current game state.
     *
     * @param coord the coordinate to start the bot's move.
     */
    private void useBot(Coordinate coord) {
        coord = ai.getMovement(activeCoordinate, getMap());
        if (map.movePiece(activeCoordinate, coord)) {
            activeCoordinate = map.getNextPieceCoordinate(coord);
            if (map.isTurnOver()) {
                endOfTurn(coord);
            }
        }
    }

    /**
     * Ends the current turn and updates points, turn state, and resets the map.
     *
     * @param coord the coordinate where the turn ended.
     */
    private void endOfTurn(Coordinate coord) {
        increasePoints(coord);
        currentPlayer = isBotPlaying ? TeamEnum.BLACK : map.getTeam(coord) == TeamEnum.BLACK ? TeamEnum.WHITE : TeamEnum.BLACK;
        isFirstMove = true;
        activeCoordinate = null;
        map.resetMap();
    }

    /**
     * Retrieves the current scores for both players.
     *
     * @return an array of scores where index 0 is Black and index 1 is White.
     */
    public int[] getPoints() {
        return points;
    }

    /**
     * Gets the current map representation.
     *
     * @return a 2D array of Tiles representing the map.
     */
    public Tile[][] getMap() {
        return map.getMap();
    }

    /**
     * Gets the currently selected coordinate.
     *
     * @return the coordinate of the selected piece.
     */
    public Coordinate getSelected() {
        return map.getSelected();
    }

    /**
     * Increases the points for the current player based on the given coordinate.
     *
     * @param coord the coordinate used to calculate points.
     */
    private void increasePoints(Coordinate coord) {
        if (currentPlayer == TeamEnum.BLACK) {
            points[0] += map.getDragonTeeth(coord) + 1;
        } else {
            points[1] += map.getDragonTeeth(coord) + 1;
        }
    }
}
