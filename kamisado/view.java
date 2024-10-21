package kamisado;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;


class View {
    private final JFrame frame;
    private final JPanel panel;
    private final Controller controller;

    private BufferedImage blueBlack;
    private BufferedImage blueWhite;
    private BufferedImage brownBlack;
    private BufferedImage brownWhite;
    private BufferedImage greenBlack;
    private BufferedImage greenWhite;
    private BufferedImage orangeBlack;
    private BufferedImage orangeWhite;
    private BufferedImage pinkBlack;
    private BufferedImage pinkWhite;
    private BufferedImage purpleBlack;
    private BufferedImage purpleWhite;
    private BufferedImage redBlack;
    private BufferedImage redWhite;
    private BufferedImage yellowBlack;
    private BufferedImage yellowWhite;

    View(Controller controller) {
        this.controller = controller;
        frame = new JFrame();
        panel = new JPanel();

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

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                int y = i;
                int x = j;
                if (tile.isOccupied()) {
                    Piece piece = tile.getPiece();
                    Image resizedImage = getImage(piece.getTeam(), piece.getColor()).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
                    ImageIcon icon = new ImageIcon(resizedImage);
                    JButton button = new JButton(icon);
                    button.addActionListener(e -> controller.touchTile(new Position(x, y)));
                    
                    button.setBackground(getColor(tile.getColor()));
                    button.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.DARK_GRAY));
                    panel.add(button);
                } else {
                    JButton button = new JButton();
                    
                    button.addActionListener(e -> controller.touchTile(new Position(x, y)));
                    button.setBackground(getColor(tile.getColor()));
                    button.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.DARK_GRAY));
                    panel.add(button);
                }
            }
        }

        panel.setVisible(true);
        frame.revalidate();
    }

    public void renderMenu() {
        return;
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

    private BufferedImage getImage(TeamEnum team, ColorEnum color) {
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
            blueBlack = ImageIO.read(new File("kamisado/res/blueTower_black.png"));
            blueWhite = ImageIO.read(new File("kamisado/res/blueTower_white.png"));
            brownBlack = ImageIO.read(new File("kamisado/res/brownTower_black.png"));
            brownWhite = ImageIO.read(new File("kamisado/res/brownTower_white.png"));
            greenBlack = ImageIO.read(new File("kamisado/res/greenTower_black.png"));
            greenWhite = ImageIO.read(new File("kamisado/res/greenTower_white.png"));
            orangeBlack = ImageIO.read(new File("kamisado/res/orangeTower_black.png"));
            orangeWhite = ImageIO.read(new File("kamisado/res/orangeTower_white.png"));
            pinkBlack = ImageIO.read(new File("kamisado/res/pinkTower_black.png"));
            pinkWhite = ImageIO.read(new File("kamisado/res/pinkTower_white.png"));
            purpleBlack = ImageIO.read(new File("kamisado/res/purpleTower_black.png"));
            purpleWhite = ImageIO.read(new File("kamisado/res/purpleTower_white.png"));
            redBlack = ImageIO.read(new File("kamisado/res/redTower_black.png"));
            redWhite = ImageIO.read(new File("kamisado/res/redTower_white.png"));
            yellowBlack = ImageIO.read(new File("kamisado/res/yellowTower_black.png"));
            yellowWhite = ImageIO.read(new File("kamisado/res/yellowTower_white.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }
}
