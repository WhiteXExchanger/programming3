package com.feke.kamisado;

import java.io.Serializable;

class Board implements Serializable {

    private Map map = new Map(true);
    private TeamEnum activePlayer = TeamEnum.BLACK;
    private boolean isTurnOver = false; // TODO REMOVE THIS LATER
    private boolean blackOnBottom = true;
    private int[] points = {0, 0};

    private boolean isBotPlaying;

    Board(boolean isBotPlaying) {
        this.isBotPlaying = isBotPlaying;
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
                isTurnOver = true;
                increasePoints(nextPosition);
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

    public Coordinate getNextPiecePosition(Coordinate positon) {
        ColorEnum color = map.getColor(positon);
        
        positon = getPositonByColor(color);
        while (!selectTile(positon)) {
            changeActivePlayer();
            positon = getPositonByColor(map.getColor(positon));
        }
        
        return positon;
    }

    private Coordinate getPositonByColor(ColorEnum color) {
        Tile[][] tileMatrix = map.getMap();
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                Piece piece = tileMatrix[i][j].getPiece();
                if (piece == null) continue;
                if (piece.getTeam() == activePlayer && piece.getColor() == color) {
                    return new Coordinate(j, i);
                }
            }
        }
        return null;
    }

    public boolean selectTile(Coordinate position) {
        map.unflagTiles();
        return map.flagTiles(position, activePlayer == TeamEnum.BLACK &&  blackOnBottom 
                                    || activePlayer == TeamEnum.WHITE && !blackOnBottom);
    }

    public boolean isTurnOver() {
        return isTurnOver;
    }

    public boolean isOncomingPlayer(Coordinate position) {
        if(position == null) return false;
        Piece piece = map.getPiece(position);
        if (piece == null) return false;
        return piece.getTeam() == activePlayer;
    }
}
