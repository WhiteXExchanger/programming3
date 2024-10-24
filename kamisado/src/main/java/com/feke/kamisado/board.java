package com.feke.kamisado;

import java.io.Serializable;
import java.util.logging.Logger;

class Board implements Serializable {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private Map map;
    private TeamEnum activePlayer;
    private boolean orientation;
    private boolean isTurnOver;
    private int[] points = {0, 0};

    Board(boolean isBottomBlack) {
        activePlayer = isBottomBlack ? TeamEnum.BLACK : TeamEnum.WHITE; // if this is false -> ai is present
        map = new Map(isBottomBlack);
        orientation = isBottomBlack;
        isTurnOver = false;
    }

    public void changeActivePlayer() {
        if (activePlayer == TeamEnum.BLACK)
            activePlayer = TeamEnum.WHITE;
        else 
            activePlayer = TeamEnum.BLACK;
        
        logger.info("actualPlayer changed: " + activePlayer);
        map.unflagTiles();
    }

    public Tile[][] getMap() {
        return map.getMap();
    }
    
    public boolean move(Position position, Position nextPosition) {
        boolean canMove = map.moveOnMap(position, nextPosition);
        if (canMove) {
            isTurnOver = isEndingMove(nextPosition);
            changeActivePlayer();
            return true;
        }
        return false;
    }

    boolean isEndingMove(Position position) {
        boolean isThereAWinner = false;
        if (orientation) {
            if (activePlayer == TeamEnum.BLACK && position.getY() == 0) {
                orientation = !orientation;
                isThereAWinner = true;
            } else if (activePlayer == TeamEnum.WHITE && position.getY() == 7) {
                isThereAWinner = true;
            }
        } else {
            if (activePlayer == TeamEnum.BLACK && position.getY() == 7) {
                isThereAWinner = true;
            } else if (activePlayer == TeamEnum.WHITE && position.getY() == 0) {
                orientation = !orientation;
                isThereAWinner = true;
            }
        }
        if (isThereAWinner) {
            increasePoints(position);
            map.ended(position, orientation);
            logger.info("black points: " + points[0] + " white points: " + points[1]);
        }
        return isThereAWinner;
    }

    private void increasePoints(Position position) {
        Piece piece = map.getPiece(position);
        if (piece.getTeam() == TeamEnum.BLACK) {
            points[0] += piece.getDragonTeeth() + 1;
        } else {
            points[1] += piece.getDragonTeeth() + 1;
        }
    }

    public int[] getPoints() {
        return points;
    }

    public Position getNextPiecePosition(Position positon) {
        ColorEnum color = map.getColor(positon);
        
        positon = getPositonByColor(color);
        while (!selectTile(positon)) {
            changeActivePlayer();
            positon = getPositonByColor(map.getColor(positon));
        }
        
        return positon;
    }

    private Position getPositonByColor(ColorEnum color) {
        Tile[][] tileMatrix = map.getMap();
        for (int i = 0; i < tileMatrix.length; i++) {
            for (int j = 0; j < tileMatrix.length; j++) {
                Piece piece = tileMatrix[i][j].getPiece();
                if (piece == null) continue;
                if (piece.getTeam() == activePlayer && piece.getColor() == color) {
                    return new Position(j, i);
                }
            }
        }
        return null;
    }

    public boolean selectTile(Position position) {
        map.unflagTiles();
        if (activePlayer == TeamEnum.BLACK && orientation || activePlayer == TeamEnum.WHITE && !orientation) {
            return map.flagTiles(position, true);
        } else {
            return map.flagTiles(position, false);
        }
    }

    public boolean isTurnOver() {
        return isTurnOver;
    }

    public boolean isOncomingPlayer(Position position) {
        if(position == null) return false;
        Piece piece = map.getPiece(position);
        if (piece == null) return false;
        return piece.getTeam() == activePlayer;
    }
}
