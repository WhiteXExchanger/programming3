package com.feke.kamisado;

import java.util.ArrayList;

/**
 * Represents the bot logic for the game. The bot uses the minimax algorithm 
 * to determine the optimal move based on the current board state.
 */
public class Bot {
    
    /**
     * Calculates the best possible movement for the bot using the minimax algorithm.
     * Takes the current coordinate of a piece and the game board state as input.
     *
     * @param coord  The current coordinate of the piece being moved by the bot.
     * @param matrix The current state of the board represented as a 2D array of Tiles.
     * @return The coordinate of the optimal move for the bot to make.
     */
    public Coordinate getMovement(Coordinate coord, Tile[][] matrix) {
        Position position = new Position(matrix, coord);
        ArrayList<Coordinate> movements = position.getValidMovements(coord);
        ArrayList<Position> positions = new ArrayList<>();
        
        // Generate all possible positions based on the valid movements.
        for (Coordinate coordinate : movements) {
            Tile[][] matrixCopy = new Tile[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    matrixCopy[i][j] = new Tile(matrix[i][j]);
                }
            }
            positions.add(new Position(matrixCopy, coord, coordinate));
        }
        
        int[] values = new int[positions.size()];

        // Use the minimax algorithm to evaluate each possible position.
        for (int i = 0; i < positions.size(); i++) {
            values[i] = minimax(positions.get(i), 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        }

        // Find the position with the highest evaluation score.
        int index = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > values[index]) {
                index = i;
            }
        }
        return movements.get(index);
    }

    /**
     * Implements the minimax algorithm with alpha-beta pruning to determine the value of a given position.
     *
     * @param position          The current position being evaluated.
     * @param depth             The depth to which the algorithm should evaluate moves.
     * @param alpha             The alpha value for alpha-beta pruning.
     * @param beta              The beta value for alpha-beta pruning.
     * @param maximizingPlayer  Indicates whether the current layer is maximizing or minimizing.
     * @return The evaluation value of the given position.
     */
    private int minimax(Position position, int depth, int alpha, int beta, boolean maximizingPlayer) {
        // Base case: If we reach the maximum depth or an end-of-game position.
        if (depth == 0 || position.isEndOfGame()) {
            return position.getEvaluation();
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Position pos : position.getPossiblePositions()) {
                int eval = minimax(pos, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Position pos : position.getPossiblePositions()) {
                int eval = minimax(pos, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }
}
