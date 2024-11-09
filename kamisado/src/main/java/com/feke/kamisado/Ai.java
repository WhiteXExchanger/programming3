package com.feke.kamisado;

import java.util.ArrayList;

public class Ai {
    public Coordinate getBestMove(Coordinate coord, Tile[][] matrix) {
        Position position = new Position(matrix, coord);
        ArrayList<Coordinate> movements = position.getValidMovements(coord);
        ArrayList<Position> positions = new ArrayList<>();
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

        for (int i = 0; i < positions.size(); i++) {
            values[i] = minimax(positions.get(i), 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        }

        int index = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > values[index]) {
                index = i;
            }
        }
        return movements.get(index);
    }

    private int minimax(Position position, int depth, int alpha, int beta, boolean maximizingPlayer) {
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
                    break;
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
                    break;
                }
            }
            return minEval;
        }
    }
}
