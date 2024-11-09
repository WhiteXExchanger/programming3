package com.feke.kamisado;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    private static final Ai ai = new Ai();

    Board board;
    View view;
    Coordinate from = null;
    boolean isFirstMove = true;
    boolean isBotPlaying = false;
    int pointsNeeded = 1;

    Controller() {
        view = new View(this);
        view.renderMenu();
        load();
    }

    void touchTile(Coordinate to) {
        if (board.isOncomingPlayer(to) && isFirstMove) {
            from = to;
            board.selectTile(from);
        } else if (board.isOncomingPlayer(from) && board.move(from, to)) {
            isFirstMove = false;
            if(board.isTurnOver()) {
                if (isGameOver()) return;
                isFirstMove = true;
                from = null;
                updateGame();
                return;
            }
            
            from = board.getNextPiecePosition(to);
            if (isBotPlaying && board.getMap()[from.getY()][from.getX()].getPiece().getTeam() == TeamEnum.WHITE) {
                to = ai.getBestMove(from, board.getMap());
                if (board.move(from, to)) {
                    from = board.getNextPiecePosition(to);
                    if(board.isTurnOver()) {
                        if (isGameOver()) {
                            return;
                        }
                        board.changeActivePlayer(TeamEnum.BLACK);
                        isFirstMove = true;
                        from = null;
                        updateGame();
                        return;
                    }
                }
            }
        }
        updateGame();
    }

    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("board.save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(board);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            FileInputStream fileIn = new FileInputStream("board.save");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (board != null) {
            from = board.getSelected();
            updateGame();
        }
    }
    
    private boolean isGameOver() {
        boolean isGameOver = false;
        int[] points = board.getPoints();
        if (points[0] == pointsNeeded) {
            logger.info("Black won");
            isGameOver = true;
        } else if (points[1] == pointsNeeded) {
            logger.info("White won");
            isGameOver = true;
        }
        if (isGameOver) view.renderMenu();
        return isGameOver;
    }
    
    public void startGame(boolean isNormalMode, boolean isBotPlaying) {
        board = new Board(isBotPlaying);
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
