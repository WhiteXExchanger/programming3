package kamisado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.*;
import kamisado.Board;
import kamisado.Tile;

class View {
    private final JFrame frame;
    private final JPanel panel;

    View(Board board) { // board shouldn't be passed like this
        frame = new JFrame();
        panel = new JPanel();
        
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(8,8,2,2));
        
        frame.setTitle("App");
        frame.setMinimumSize(new Dimension(450, 450));
        frame.setLayout(new GridLayout(0,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gamePanel(board);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    void gamePanel(Board board) {
        panel.removeAll();

        Tile[][] tiles = board.getTileMatrix();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                JButton button = new JButton();
                button.setBackground(getColor(tile.getColor()));
                button.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.DARK_GRAY));
                panel.add(button);
            }
        }

        panel.setVisible(true);
    }

    Color getColor(ColorEnum colorEnum) {
        switch (colorEnum) {
            case ColorEnum.ORANGE -> {return new Color(255,165,0);}
            case ColorEnum.RED -> {return Color.RED;}
            case ColorEnum.BLUE -> {return Color.BLUE;}
            case ColorEnum.YELLOW -> {return Color.YELLOW;}
            case ColorEnum.PINK -> {return new Color(255,105,180);}
            case ColorEnum.PURPLE -> {return new Color(128,0,128);}
            case ColorEnum.BROWN -> {return new Color(139,69,19);}
            case ColorEnum.GREEN-> {return Color.GREEN;}
        
            default -> {return Color.WHITE;}
        }
    }
}
