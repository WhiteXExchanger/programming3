package com.feke.kamisado;

import java.io.File;
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
        load(); // Trying to load save, on success it gets rendered
    }

    // Manages if the game should be played or exited
    void touchTile(Coordinate to) {
        board.interact(to);
        updateGame();
        if (isGameOver()) {
            remove();
            view.renderMenu();
        }
    }

    // Only saves if a game already has begun
    public void save() {
        if (board == null) return;
        try {
            FileOutputStream fileOut = new FileOutputStream("board.save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            System.out.println("Saving");
            out.writeObject(board);
            out.writeInt(pointsNeeded);
            out.close();
        } catch (Exception e) {
            System.out.println("Was not able to save.");
            e.printStackTrace();
        }
    }

    // Puts the board as an object, and pointsNeeded as an int into "board.save" file.
    // AKA saving the game state
    private void load() {
        try {
            FileInputStream fileIn = new FileInputStream("board.save");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            pointsNeeded = (int) in.readInt();
            in.close();
        } catch (Exception e) {
            System.out.println("Save file wasn't found. Loading normally");
        }
        if (board != null) {
            updateGame();
        }
    }

    // Removes the save file, it should run only if the game have ended
    private void remove() {
        try {
            File fileToRemove = new File("board.save");
            fileToRemove.delete();
        } catch (Exception e) {
            System.out.println("Save file couldn't be removed.");
        }
    }
    
    // Checks if a player enough points to win, if so returns with true, otherwise false
    private boolean isGameOver() {
        int[] points = board.getPoints();
        boolean gameState = (points[0] >= pointsNeeded || points[1] >= pointsNeeded);
        if (gameState) board = null;
        return gameState;
    }
    
    // Board gets created, pointsNeeded gets assigned, calls for game rendering
    public void startGame(boolean isNormalMode, boolean isBotPlaying) {
        board = new Board(isBotPlaying);
        pointsNeeded = isNormalMode ? 15 : 1;
        updateGame();
    }

    // Calls for game rendering for refreshing the game state
    private void updateGame() {
        view.renderGame(board.getMap());
    }
}
