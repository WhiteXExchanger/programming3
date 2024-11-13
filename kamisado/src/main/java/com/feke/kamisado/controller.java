package com.feke.kamisado;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Controller {

    Board board;
    View view;
    int pointsNeeded = 1;

    Controller() {
        view = new View(this);
        view.renderMenu();
        load();
    }

    void touchTile(Coordinate to) {
        if (!isGameOver()) {
            board.tryMoving(to);
            updateGame();
        } else {
            view.renderMenu();
        }
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
        int[] points = board.getPoints();
        if (points[0] == pointsNeeded || points[1] == pointsNeeded) {
            view.renderMenu();
            return true;
        }
        return false;
    }
    
    public void startGame(boolean isNormalMode, boolean isBotPlaying) {
        board = new Board(isBotPlaying);
        pointsNeeded = isNormalMode ? 15 : 1;
        updateGame();
    }

    private void updateGame() {
        view.renderGame(board.getMap());
    }
}
