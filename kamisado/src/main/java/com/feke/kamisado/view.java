package com.feke.kamisado;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class View extends JFrame {
    private final JFrame frame = this;
    private final Controller controller;
    private JPanel panel;

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

    
    private Image black1Sumo;
    private Image black2Sumo;
    private Image black3Sumo;
    private Image white1Sumo;
    private Image white2Sumo;
    private Image white3Sumo;

    private Image selectedTile;
    private Image flaggedTile;

    View(Controller controller) {
        this.controller = controller;
        panel = new JPanel();

        classLoader = getClass().getClassLoader();
        imageLoader();
        setupPanel();
        
        frame.setTitle("Kamisado");
        frame.setMinimumSize(new Dimension(410, 410));
        frame.setResizable(false);
        frame.setLayout(new GridLayout(0,1));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { /* do noting */ }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.save();
                    renderMenu();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { /* do noting */ }
        });
    }

    // Saves the game state on closing the app
    @Override
    public void dispose() {
        //controller.save(); commented for debugging purpuses
        super.dispose();
    }

    // Calls for further rendering jobs
    public void renderGame(Tile[][] tiles) {
        frame.getContentPane().removeAll();
        setupPanel();

        panel.setLayout(new GridLayout(8,8,1,1));
        renderTiles(tiles);

        panel.revalidate();
    }

    // Renders the menu
    public void renderMenu() {
        frame.getContentPane().removeAll();
        setupPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create buttons
        JButton playerVsPlayer = new JButton("Player vs Player");
        JButton playerVsBot = new JButton("Player vs Bot");
        JButton quit = new JButton("Quit");

        playerVsPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerVsBot.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        playerVsPlayer.addActionListener(e -> renderGameOptions(false));
        playerVsBot.addActionListener(e -> renderGameOptions(true));
        quit.addActionListener(e -> exit());

        // Add buttons to the panel
        panel.add(playerVsPlayer);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(playerVsBot);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(quit);

        // Set the frame to be visible
        panel.revalidate();
    }

    // Renders the game options
    public void renderGameOptions(boolean isBotPlaying) {
        panel.removeAll();
        setupPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create buttons
        JButton normalMode = new JButton("Normal mode");
        JButton quickMode = new JButton("Quick mode");
        JButton back = new JButton("Back");

        normalMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        quickMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        normalMode.addActionListener(e -> controller.startGame(true, isBotPlaying));
        quickMode.addActionListener(e -> controller.startGame(false, isBotPlaying));
        back.addActionListener(e -> renderMenu());

        // Add buttons to the panel
        panel.add(normalMode);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(quickMode);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(back);

        // Set the frame to be visible
        panel.revalidate();
    }

    // Renders the map tiles, calls for individual rendering
    private void renderTiles(Tile[][] tiles) {
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                renderTile(tiles[i][j], j, i);
            }
        }
    }

    // Renders individual tiles with their respective states
    private void renderTile(Tile tile, int x, int y) {
        if (tile.isOccupied()) {
            
            ImageIcon icon = getPiecedTileGraphics(tile);
            JButton button = new JButton(icon);
            button.addActionListener(e -> controller.touchTile(new Coordinate(x, y)));
            
            button.setBackground(getColor(tile.getColor()));
            panel.add(button);
        } else {
            ImageIcon icon = new ImageIcon();
            if (tile.isFlagged()) {
                icon = new ImageIcon(flaggedTile);
            }
            
            JButton button = new JButton(icon);
            button.addActionListener(e -> controller.touchTile(new Coordinate(x, y)));
            button.setBackground(getColor(tile.getColor()));
            panel.add(button);
        }
    }

    // Renders individual tiles with their respective pieces on top
    private ImageIcon getPiecedTileGraphics(Tile tile) {
        Piece piece = tile.getPiece();

        Image background = getImage(piece.getTeam(), piece.getColor()); // getting the base

        BufferedImage combinedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(background, 0, 0, null);
        if (piece.getDragonTeeth() != 0) {
            g.drawImage(getImage(piece.getTeam(), piece.getDragonTeeth()), 0, 0, null);
        }
        if (tile.isSelected())
            g.drawImage(selectedTile, 0, 0, null);
        g.dispose();

        return new ImageIcon(combinedImage);
    }

    // Assings colors for the tiles
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

    // Returns the "piece image" in respect to the team, and color provided, defaults to no image 
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

    // Returns the "teeth image" in respect to the team, and teeth count provided, defaults to no image 
    private Image getImage(TeamEnum team, int teeth) {
        if (team == TeamEnum.BLACK) {
            switch (teeth) {
                case 1 -> { return black1Sumo; }
                case 2 -> { return black2Sumo; }
                case 3 -> { return black3Sumo; }
                default -> { return new BufferedImage(0, 0, 0, null); }
            }
        } else {
            switch (teeth) {
                case 1 -> { return white1Sumo; }
                case 2 -> { return white2Sumo; }
                case 3 -> { return white3Sumo; }
                default -> { return new BufferedImage(0, 0, 0, null); }
            }
        }
    }

    // Sets up the panel to render objects on it
    private void setupPanel() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        frame.setContentPane(panel);
        frame.pack();
    }

    // Closes the window
    private void exit() {                                  
        frame.dispose();
    }
    
    // Loads the images into variables
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
            black1Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo1_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            black2Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo2_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            black3Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo3_black.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            white1Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo1_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            white2Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo2_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            white3Sumo = ImageIO.read(classLoader.getResourceAsStream("pieces/sumo/sumo3_white.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
