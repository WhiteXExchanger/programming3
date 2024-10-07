package hf;

import java.util.ArrayList;
import java.util.function.Function;

class Map {
    Mine[][][] mineMap;

    Map(int size) {
        mineMap = new Mine[size][size][size];
    }

    private <R> void runOnMap(Function<Mine, R> func) {
        for (Mine[][] mines_x : mineMap) {
            for (Mine[] mines_y : mines_x) {
                for (Mine mines_z : mines_y) {
                    func.apply(mines_z);
                }
            }
        }
    }

    private ArrayList<Mine> findNeighbours(int x, int y, int z) {
        ArrayList<Mine> neighbours = new ArrayList<>();
        neighbours.add(new Mine()); // add each neighbour in a 3x3x3 cube
        return neighbours;
    }

    private void openTile(int x, int y, int z) {
        Mine tile = mineMap[x][y][z]; 
        tile.setVisual(VisualEnum.OPEN);

        if (tile.getType() == TypeEnum.COUNTER) return;
        
        tile.setVisual(VisualEnum.OPEN);
        ArrayList<Mine> neighbours = findNeighbours(x,y,z);
        neighbours.forEach((n) -> openTile(x,y,z));

        //rekurzió
        // szomszédok -> 3x3x3 vagyis mindegyik axiesen -1 és +1 távolságra lévő mezők
        // ha ezek már láthatók (meglátogatott mezők a rekurzió által) akkor vissza lépünk
        // ha eddig még nem láttuk őket mostmár felfedhetők,
        // ha ez egy számláló mező nem keressük meg a szomszédjait
        // ha ez egy üres mező, megkeressük a szomszédokat és rajtuk folytatjuk a műveletet

    }

    public void sweepTile(int x, int y, int z) {
        switch (mineMap[x][y][z].getType()) {
            case TypeEnum.MINE:
                endGame();
                break;
            default:
                openTile(x,y,z);
                break;
        }
    }

    public void flagTile(int x, int y, int z) {
        Mine clickedTile = mineMap[x][y][z];
        VisualEnum visual = clickedTile.getVisual();

        switch (visual) {
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

    private void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }



}