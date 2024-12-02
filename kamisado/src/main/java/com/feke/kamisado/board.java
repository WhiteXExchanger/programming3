package com.feke.kamisado;

import java.io.Serializable;

public class Board implements Serializable {

    private static final Bot ai = new Bot();
    private final Map map;
    private TeamEnum currentPlayer = TeamEnum.BLACK;
    private Coordinate activeCoordinate = null;
    private boolean isBotPlaying = false;
    private boolean isFirstMove = true;
    private int[] points = {0, 0};

    Board(boolean isBotPlaying) {
        this.map = new Map(isBotPlaying);
        this.isBotPlaying = isBotPlaying;
    }

    // if we interacting with one of our piece, we change the current selected piece coordinate to the other piece coordinate
    // otherwise we try to move the piece to this new coordinate
    public void interact(Coordinate coord) {
        if (currentPlayer == map.getTeam(coord) && isFirstMove) {
            activeCoordinate = coord;
            map.selectTile(activeCoordinate);

        } else if (activeCoordinate != null && map.movePiece(activeCoordinate, coord)) {
            isFirstMove = false;

            if(map.isTurnOver()) {
                endOfTurn(coord);
                return;
            }

            activeCoordinate = map.getNextPieceCoordinate(coord);
            currentPlayer = map.getTeam(activeCoordinate);

            // Bot is gonna be used when it is present in the game
            if (isBotPlaying && currentPlayer == TeamEnum.WHITE) useBot(activeCoordinate);
        }
    }

    // Runs bot's algorithm on the current state of the map for a move to make
    private void useBot(Coordinate coord) {
        coord = ai.getMovement(activeCoordinate, getMap());
        if (map.movePiece(activeCoordinate, coord)) {
            activeCoordinate = map.getNextPieceCoordinate(coord);
            if(map.isTurnOver()) {
                endOfTurn(coord);
            }
        }
    }

    // This resets the map, variables, and gives points to the player who won, should run on entering enemies base line
    private void endOfTurn(Coordinate coord) {
        increasePoints(coord);
        if (isBotPlaying) currentPlayer = TeamEnum.BLACK;
        else currentPlayer = map.getTeam(coord) == TeamEnum.BLACK ? TeamEnum.WHITE : TeamEnum.BLACK;
        isFirstMove = true;
        activeCoordinate = null;
        map.resetMap();
    }

    // getter for points
    public int[] getPoints() {
        return points;
    }

    // getter for map (used to draw the map)
    public Tile[][] getMap() {
        return map.getMap();
    }

    // getter for selected tile (used to visualize)
    public Coordinate getSelected() {
        return map.getSelected();
    }

    // gives point to the player on the corresponding coordinate
    private void increasePoints(Coordinate coord) {
        if (currentPlayer == TeamEnum.BLACK) {
            points[0] += map.getDragonTeeth(coord) + 1;
        } else {
            points[1] += map.getDragonTeeth(coord) + 1;
        }
    }
}
