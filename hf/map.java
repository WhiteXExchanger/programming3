package hf;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

class Map {
    private static final double CHANCE_OF_MINE = 0.6;
    private static Random rnd = new Random();
    private int mineCount;
    Mine[][][] mineMap;

    /*
     * This could be refactored to be more efficent,
     * there could be a list which keeps track of the mines,
     * that would be the controll data, so whenever generating a map it should be easier
     * to iterate over it and bring the counter tile around it higher.
     * 
     * from (n^3)*27 to k*27, which is the number of mines, k*27 < n^3*27
     * 
     * for this to work Mine as a class has to keep track of the position its in,
     * likewise other tiles have to keep their places
     */

    Map(int size) {
        mineMap = new Mine[size][size][size];
        mineCount = 0;
        runOnMap(this::generateMine);
        countMine();
    }

    private void runOnMap(Consumer<int[]> func) {
        for (int x = 0; x < mineMap.length; x++) {
            for (int y = 0; y < mineMap.length; y++) {
                for (int z = 0; z < mineMap.length; z++) {
                    int[] vector = {x,y,z};
                    func.accept(vector);
                }
            }
        }
    }

    private ArrayList<Mine> findNeighbours(int x, int y, int z) {
        ArrayList<Mine> neighbours = new ArrayList<>();

        for (int i = -1; i < 2; i++) {
            int xPositon = x + i;
            if (xPositon < 0 || xPositon > mineMap.length) continue;

            for (int j = -1; j < 2; j++) {    
                int yPositon = y + j;
                if(yPositon < 0 || yPositon > mineMap.length) continue;

                for (int k = -1; k < 2; k++) {
                    int zPosition = z + k;
                    if(zPosition < 0 || zPosition > mineMap.length || i == 0 && j == 0 && k == 0) continue;

                    neighbours.add(mineMap[xPositon][yPositon][zPosition]); // add each neighbour in a 3x3x3 cube
                }
            }
        }

        return neighbours;
    }

    private void openTile(int x, int y, int z) {
        Mine tile = mineMap[x][y][z]; 
        tile.setVisual(VisualEnum.OPEN);

        if (tile.getType() == TypeEnum.COUNTER) return;
        
        tile.setVisual(VisualEnum.OPEN);
        ArrayList<Mine> neighbours = findNeighbours(x,y,z);
        neighbours.forEach(n -> openTile(x,y,z));

        //rekurzió
        // szomszédok -> 3x3x3 vagyis mindegyik axiesen -1 és +1 távolságra lévő mezők
        // ha ezek már láthatók (meglátogatott mezők a rekurzió által) akkor vissza lépünk
        // ha eddig még nem láttuk őket mostmár felfedhetők,
        // ha ez egy számláló mező nem keressük meg a szomszédjait
        // ha ez egy üres mező, megkeressük a szomszédokat és rajtuk folytatjuk a műveletet

    }

    public void sweepTile(int x, int y, int z) {
        Mine clickedTile = mineMap[x][y][z];
        if (clickedTile.getType() == TypeEnum.MINE) {
            endGame();
        } else {
            openTile(x,y,z);
        }
    }

    public void flagTile(int x, int y, int z) {
        Mine clickedTile = mineMap[x][y][z];
        switch (clickedTile.getVisual()) {
            case VisualEnum.CLOSED:
                clickedTile.setVisual(VisualEnum.FLAGGED);
                break;

            case VisualEnum.FLAGGED:
                clickedTile.setVisual(VisualEnum.CLOSED);
                break;
        
            default:
                break;
        }
    }

    private void generateMine(int[] vector) {
        Mine mine = mineMap[vector[0]][vector[1]][vector[2]]; 
        if (rnd.nextDouble() > CHANCE_OF_MINE) {
            mine.setCount(27);
            mineCount++;
        }
        
        ArrayList<Mine> neighbours = findNeighbours(vector[0], vector[1], vector[2]);
        neighbours.forEach(Mine::setCount);
    }
    
    private void countMine() {
        // TODO Counting the mines around tiles
    }

    private int countCorrectlyGuessedMines() {
        int minesCount = 0;

        for (Mine[][] xMines : mineMap) {
            for (Mine[] yMines : xMines) {
                for (Mine zMine : yMines) {
                    if (zMine.getType() == TypeEnum.MINE && zMine.getVisual() == VisualEnum.FLAGGED) minesCount++;
                }
            }
        }
        return minesCount;
    }

    private void endGame() {
        if (mineCount > countCorrectlyGuessedMines()) {
            // YOU LOSE
        } else {
            // YOU WIN
        }
    }
}