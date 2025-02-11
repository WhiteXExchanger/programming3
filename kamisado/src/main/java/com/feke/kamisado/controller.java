package com.feke.kamisado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The Controller class manages the flow of the game, including user
 * interactions, saving/loading the game, and managing the Board and View.
 */
public class Controller {

    Board board;
    View view;
    int pointsNeeded = 1;

    /**
     * Constructs a Controller and initializes the game UI.
     * Attempts to load a saved game state.
     */
    Controller() {
        view = new View(this);
        view.renderMenu();
        load();
    }

    /**
     * Handles user interaction with a tile.
     *
     * @param to the coordinate of the tile being touched.
     */
    void touchTile(Coordinate to) {
        board.interact(to);
        updateGame();
        if (isGameOver()) {
            remove();
            view.renderMenu();
        }
    }

    /**
     * Saves the current game state to a file.
     */
    public void save() {
        if (board == null) return;
        try {
            FileOutputStream fileOut = new FileOutputStream("game.save");
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

    /**
     * Loads a saved game state from a file, if it exists.
     */
    private void load() {
        try {
            FileInputStream fileIn = new FileInputStream("game.save");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            pointsNeeded = in.readInt();
            in.close();
        } catch (Exception e) {
            System.out.println("Save file wasn't found. Loading normally");
        }
        if (board != null) {
            updateGame();
        }
    }

    /**
     * Deletes the saved game file. Used when the game ends.
     */
    private void remove() {
        try {
            File fileToRemove = new File("game.save");
            fileToRemove.delete();
        } catch (Exception e) {
            System.out.println("Save file couldn't be removed.");
        }
    }

    /**
     * Starts a new game with the specified mode and bot-playing option.
     *
     * @param isNormalMode whether the game is in normal mode (15 points to win).
     * @param isBotPlaying whether a bot will play.
     */
    public void startGame(boolean isNormalMode, boolean isBotPlaying) {
        board = new Board(isBotPlaying);
        pointsNeeded = isNormalMode ? 15 : 1;
        updateGame();
    }

    /**
     * Refreshes the game state and updates the UI.
     */
    private void updateGame() {
        view.renderGame(board.getMap());
    }

    /**
     * Checks if the game is over based on points.
     *
     * @return true if a player has reached the required points, false otherwise.
     */
    private boolean isGameOver() {
        int[] points = board.getPoints();
        boolean gameState = (points[0] >= pointsNeeded || points[1] >= pointsNeeded);
        if (gameState) board = null;
        return gameState;
    }
}
