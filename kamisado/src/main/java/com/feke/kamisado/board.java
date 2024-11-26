package com.feke.kamisado;

import java.io.Serializable;

public class Board implements Serializable {

    private static final Ai ai = new Ai();
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

    // When interacting with our piece, we change the selected piece
    // Otherwise we try to move the piece to the incoming coordinate
    public void interact(Coordinate coord) {
        if (isOncomingsPiece(coord) && isFirstMove) {
            activeCoordinate = coord;
            map.selectTile(activeCoordinate, currentPlayer);

        } else if (activeCoordinate != null && map.movePiece(activeCoordinate, coord)) {
            isFirstMove = false;

            if(map.isTurnOver()) {
                endOfTurn(coord);
                changeActivePlayer();
                return;
            }

            changeActivePlayer();
            activeCoordinate = map.getNextPieceCoordinate(coord);

            // Only runs if AI is present
            if (isBotPlaying) useBot(activeCoordinate);
        }
    }

    private void useBot(Coordinate coord) {
        coord = ai.getBestMove(activeCoordinate, getMap());
        if (map.movePiece(activeCoordinate, coord)) {
            activeCoordinate = map.getNextPieceCoordinate(coord);
            if(map.isTurnOver()) {
                endOfTurn(coord);
                changeActivePlayer(TeamEnum.BLACK);
            }
        }
    }

    private void endOfTurn(Coordinate coord) {
        increasePoints(coord);
        isFirstMove = true;
        activeCoordinate = null;
    }

    public int[] getPoints() {
        return points;
    }

    public void changeActivePlayer() {
        if (currentPlayer == TeamEnum.BLACK)
            currentPlayer = TeamEnum.WHITE;
        else 
            currentPlayer = TeamEnum.BLACK;
    }

    private void changeActivePlayer(TeamEnum team) {
        currentPlayer = team;
    }

    public Tile[][] getMap() {
        return map.getMap();
    }

    public Coordinate getSelected() {
        return map.getSelected();
    }

    private void increasePoints(Coordinate coord) {
        if (currentPlayer == TeamEnum.BLACK) {
            points[0] += map.getDragonTeeth(coord) + 1;
        } else {
            points[1] += map.getDragonTeeth(coord) + 1;
        }
    }

    public boolean isOncomingsPiece(Coordinate coord) {
        return map.getTeam(coord) == currentPlayer;
    }
}
