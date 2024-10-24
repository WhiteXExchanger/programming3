package com.feke.kamisado;

import java.util.logging.Logger;

class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    Board board;
    View view;
    Position from = null;
    boolean isFirstMove = true;
    boolean isBotPlaying = false;
    int pointsNeeded = 1;

    Controller() {
        view = new View(this);
        view.renderMenu();
    }

    void touchTile(Position to) {
        if (board.isOncomingPlayer(to) && isFirstMove) {
            from = to;
            board.selectTile(from);
        } else if (board.isOncomingPlayer(from) && board.move(from, to)) {
            isFirstMove = false;
            if(board.isTurnOver()) {
                if(isGameOver()) {
                    view.renderMenu();
                    return;
                }
                isFirstMove = true;
                from = null;
                updateGame();
                return;
            }
            
            from = board.getNextPiecePosition(to);
        }
        updateGame();
    }
    
    private boolean isGameOver() {
        int[] points = board.getPoints();
        if (points[0] == pointsNeeded) {
            logger.info("Black won");
            return true;
        } else if (points[1] == pointsNeeded) {
            logger.info("White won");
            return true;
        }
        return false;
    }
    
    public void startGame(boolean isNormalMode, boolean isBotPlaying) {
        board = new Board(isNormalMode);
        this.isBotPlaying = isBotPlaying;
        pointsNeeded = isNormalMode ? 15 : 1;
        isFirstMove = true;
        from = null;
        updateGame();
    }

    private void updateGame() {
        view.renderGame(board.getMap());
    }
}
