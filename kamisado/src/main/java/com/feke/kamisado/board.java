package com.feke.kamisado;

import java.io.Serializable;

public class Board implements Serializable {

    private static final Ai ai = new Ai();
    private Map map;
    private TeamEnum activePlayer = TeamEnum.BLACK;
    private Coordinate activeCoordinate = null;
    private boolean isBotPlaying = false;
    private boolean isFirstMove = true;
    private int[] points = {0, 0};

    Board(boolean isBotPlaying) {
        this.map = new Map(isBotPlaying);
        this.isBotPlaying = isBotPlaying;
    }

    public void tryMoving(Coordinate to) {
        if (isOncomingPlayer(activeCoordinate) && isFirstMove) {
            activeCoordinate = to;
            selectTile(activeCoordinate);
        } else if (isOncomingPlayer(activeCoordinate) && move(activeCoordinate, to)) {
            isFirstMove = false;
            if(isTurnOver()) {
                isFirstMove = true;
                activeCoordinate = null;
                return;
            }
        
            activeCoordinate = map.getNextPiecePosition(to);
            if (isBotPlaying && map.getPiece(activeCoordinate).getTeam() == TeamEnum.WHITE) {
                to = ai.getBestMove(activeCoordinate, getMap());
                if (move(activeCoordinate, to)) {
                    activeCoordinate = map.getNextPiecePosition(to);
                    if(isTurnOver()) {
                        changeActivePlayer(TeamEnum.BLACK);
                        isFirstMove = true;
                        activeCoordinate = null;
                    }
                }
            }
        }
    }

    public int[] getPoints() {
        return points;
    }

    public void changeActivePlayer() {
        if (activePlayer == TeamEnum.BLACK)
            activePlayer = TeamEnum.WHITE;
        else 
            activePlayer = TeamEnum.BLACK;
        map.unflagTiles();
    }

    public void changeActivePlayer(TeamEnum team) {
        activePlayer = team;
        map.unflagTiles();
    }

    public Tile[][] getMap() {
        return map.getMap();
    }
    
    public boolean move(Coordinate position, Coordinate nextPosition) {
        boolean canMove = map.move(position, nextPosition);
        if (canMove) {
            if (nextPosition.getY() == 0 || nextPosition.getY() == 7) {
                increasePoints(nextPosition);
                isTurnOver = true; // reset needed
            }
            changeActivePlayer();
        }
        return canMove;
    }

    public Coordinate getSelected() {
        return map.getSelected();
    }

    private void increasePoints(Coordinate position) {
        Piece piece = map.getPiece(position);
        if (piece.getTeam() == TeamEnum.BLACK) {
            points[0] += piece.getDragonTeeth() + 1;
        } else {
            points[1] += piece.getDragonTeeth() + 1;
        }
    }

    public boolean selectTile(Coordinate position) {
        return map.flagTiles(position);
    }

    public boolean isOncomingPlayer(Coordinate position) {
        if(position == null) return false;
        Piece piece = map.getPiece(position);
        if (piece == null) return false;
        return piece.getTeam() == activePlayer;
    }
}
