package com.feke.kamisado;

import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;


class View {
    private final JFrame frame;
    private final JPanel panel;
    private final Controller controller;

    private ClassLoader classLoader;
    private Image blueBlack;
    private Image blueWhite;
    private Image brownBlack;
    private Image brownWhite;
    private Image greenBlack;
    private Image greenWhite;
    private Image orangeBlack;
    private Image orangeWhite;
    private Image pinkBlack;
    private Image pinkWhite;
    private Image purpleBlack;
    private Image purpleWhite;
    private Image redBlack;
    private Image redWhite;
    private Image yellowBlack;
    private Image yellowWhite;

    private Image selectedTile;
    private Image flaggedTile;

    View(Controller controller) {
        this.controller = controller;
        frame = new JFrame();
        panel = new JPanel();

        classLoader = getClass().getClassLoader();
        imageLoader();
        
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(8,8,2,2));
        
        frame.setTitle("Kamisado");
        frame.setMinimumSize(new Dimension(450, 450));
        frame.setLayout(new GridLayout(0,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void renderGame(Tile[][] tiles) {
        panel.removeAll();

        renderTiles(tiles);

        panel.setVisible(true);
        frame.revalidate();
    }

    public void renderMenu() {
        return;
    }

    private void renderTiles(Tile[][] tiles) {
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                renderTile(tiles[i][j], j, i);
            }
        }
    }

    private void renderTile(Tile tile, int x, int y) {
        if (tile.isOccupied()) {
            
            ImageIcon icon = getPiecedTileGraphics(tile);
            JButton button = new JButton(icon);
            button.addActionListener(e -> controller.touchTile(new Position(x, y)));
            
            button.setBackground(getColor(tile.getColor()));
            button.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.DARK_GRAY));
            panel.add(button);
        } else {
            ImageIcon icon = new ImageIcon();
            if (tile.isFlagged()) {
                icon = new ImageIcon(flaggedTile);
            }
            
            JButton button = new JButton(icon);
            button.addActionListener(e -> controller.touchTile(new Position(x, y)));
            button.setBackground(getColor(tile.getColor()));
            button.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.DARK_GRAY));
            panel.add(button);
        }
    }

    private ImageIcon getPiecedTileGraphics(Tile tile) {
        Piece piece = tile.getPiece();

        Image background = getImage(piece.getTeam(), piece.getColor()); // getting the base

        BufferedImage combinedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(background, 0, 0, null);
        if (tile.isSelected())
            g.drawImage(selectedTile, 0, 0, null);
        g.dispose();

        return new ImageIcon(combinedImage);
    }

    Color getColor(ColorEnum colorEnum) {
        switch (colorEnum) {
            case ORANGE -> {return new Color(255,165,0);}
            case RED -> {return Color.RED;}
            case BLUE -> {return Color.BLUE;}
            case YELLOW -> {return Color.YELLOW;}
            case PINK -> {return new Color(255,105,180);}
            case PURPLE -> {return new Color(128,0,128);}
            case BROWN -> {return new Color(139,69,19);}
            case GREEN-> {return Color.GREEN;}
            
            default -> {return Color.WHITE;}
        }
    }

    private Image getImage(TeamEnum team, ColorEnum color) {
        if (team == TeamEnum.BLACK) {
            switch (color) {
                case BLUE -> { return blueBlack; }
                case ORANGE -> { return orangeBlack; }
                case RED -> { return redBlack; }
                case GREEN -> { return greenBlack; }
                case PINK -> { return pinkBlack; }
                case YELLOW -> { return yellowBlack; }
                case PURPLE -> { return purpleBlack; }
                case BROWN -> { return brownBlack; }
                default -> { return new BufferedImage(0, 0, 0, null); }
            }
        } else {
            switch (color) {
                case BLUE -> { return blueWhite; }
                case ORANGE -> { return orangeWhite; }
                case RED -> { return redWhite; }
                case GREEN -> { return greenWhite; }
                case PINK -> { return pinkWhite; }
                case YELLOW -> { return yellowWhite; }
                case PURPLE -> { return purpleWhite; }
                case BROWN -> { return brownWhite; }
                default -> { return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB); }
            }
        }
    }
    
    private void imageLoader() {
        try {
            blueBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/blueTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            blueWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/blueTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            brownBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/brownTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            brownWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/brownTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            greenBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/greenTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            greenWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/greenTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            orangeBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/orangeTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            orangeWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/orangeTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            pinkBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/pinkTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            pinkWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/pinkTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            purpleBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/purpleTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            purpleWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/purpleTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            redBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/redTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            redWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/redTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            yellowBlack = ImageIO.read(classLoader.getResourceAsStream("pieces/yellowTower_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            yellowWhite = ImageIO.read(classLoader.getResourceAsStream("pieces/yellowTower_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            flaggedTile = ImageIO.read(classLoader.getResourceAsStream("tile_overlays/possibleTileOverlay.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            selectedTile = ImageIO.read(classLoader.getResourceAsStream("tile_overlays/focusedTileOverlay.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }
}
