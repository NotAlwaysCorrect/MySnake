import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Snakes don't even eat apples

public class Main {
    JPanel startMenu, gameScreen, pauseMenu, restartMenu;
    JFrame frame;
    JLabel startLabel, pauseLabel, restartLabel;
    ArrayList<Integer> currentGrassLocations;
    int columns = 3, rows = 3;
    int snakeHeadXLocationStart = 1, snakeHeadYLocationStart = 1;
    int snakeHeadXLocation = snakeHeadXLocationStart, snakeHeadYLocation = snakeHeadYLocationStart;

    int appleLocationInArray;
    int appleLocationInGridY;
    int appleLocationInGridX;
    private final JLabel[][] t = new JLabel[rows][columns];
    public boolean gameScreenIsFocused = false;
    public char direction = 'U';
    ArrayList<Integer> snakeBodyLocation = new ArrayList<>();
    BufferedImage importedTriangle = ImageIO.read(new File("src/images/Black_triangle.png"));
    Image scaledSnakeHead = importedTriangle.getScaledInstance(100,100,
            Image.SCALE_SMOOTH);
    BufferedImage importedApple = ImageIO.read(new File("src/images/Apple_JE3_BE3.png"));
    Image scaledApple = importedApple.getScaledInstance(100,100,
            Image.SCALE_SMOOTH);
    BufferedImage importedGrass = ImageIO.read(new File("src/images/grass.png"));
    Image scaledGrass = importedGrass.getScaledInstance(100,100,
            Image.SCALE_SMOOTH);



    public Main() throws IOException {



        frame =  new JFrame();
        startMenu = new JPanel();
        startLabel = new JLabel("Click to Start");

        gameScreen = new JPanel();

        startMenu.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        startMenu.setLayout(new GridLayout(0,1));

        startMenu.add(startLabel);
        addFrameKeyboardBindings(frame.getRootPane(), "F11", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int state = frame.getExtendedState();

                if (state == JFrame.MAXIMIZED_BOTH) {
                    state = JFrame.NORMAL;
                } else {
                    state = JFrame.MAXIMIZED_BOTH;
                }
                frame.setExtendedState(state);
            }
        });

        pauseLabel = new JLabel("Paused");
        pauseMenu = new JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                g.setColor( getBackground() );
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        pauseMenu.add(pauseLabel);
        pauseMenu.setOpaque(false); // background of parent will be painted first
        pauseMenu.setBackground( new Color(255, 255, 255, 80) );

        restartMenu = new JPanel() {
            protected void paintComponent(Graphics g)
            {
                g.setColor( getBackground() );
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        restartLabel = new JLabel("Game Over");
        restartMenu.setOpaque(false); // background of parent will be painted first
        restartMenu.setBackground( new Color(255, 255, 255, 80) );

        restartMenu.add(restartLabel);

        gameScreen.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        gameScreen.setLayout(new GridLayout(rows,columns));

        buildGrass();


//        Placing the apple
        currentGrassLocations = new ArrayList<>();
        for (int i = 0; i < columns * rows; i++)
            currentGrassLocations.add(i);
//        Removing the Snake Location before randomising the apple
        currentGrassLocations.remove(snakeHeadYLocation * columns + snakeHeadXLocation);

        appleLocationInArray = (int) (Math.random() * currentGrassLocations.size());
        appleLocationInGridY = currentGrassLocations.get(appleLocationInArray) / columns;
        appleLocationInGridX = currentGrassLocations.get(appleLocationInArray) % columns;

        t[appleLocationInGridY][appleLocationInGridX].setIcon(new ImageIcon(scaledApple));

//        Setting the snake beginning location
        snakeBodyLocation.add(snakeHeadYLocation * columns + snakeHeadXLocation);
        t[snakeHeadYLocation][snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));

//        convert appleLocation into gameScreen label location
        addFrameKeyboardBindings(frame.getRootPane(), "ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(startMenu);
                frame.add(gameScreen);
                gameScreenIsFocused = gameScreen.requestFocusInWindow();

                frame.revalidate();
                frame.repaint();
            }
        });

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.remove(startMenu);
                frame.add(gameScreen);
                gameScreenIsFocused = gameScreen.requestFocusInWindow();

                frame.revalidate();
                frame.repaint();
            }
        });

        frame.add(startMenu, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");
        frame.pack();
        frame.setVisible(true);

        addFrameKeyboardBindings(gameScreen, "UP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameScreenIsFocused)
                    direction = 'U';
            }
        });
        addFrameKeyboardBindings(gameScreen, "DOWN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameScreenIsFocused)
                    direction = 'D';
            }
        });
        addFrameKeyboardBindings(gameScreen, "LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameScreenIsFocused)
                    direction = 'L';
            }
        });
        addFrameKeyboardBindings(gameScreen, "RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameScreenIsFocused)
                    direction = 'R';
            }
        });

        addFrameKeyboardBindings(gameScreen, "ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.add(pauseMenu);
                pauseMenu.grabFocus();
                gameScreenIsFocused = false;

                frame.revalidate();
                frame.repaint();

//                No idea why I need to sleep for 1 millisecond to make the program work
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                frame.add(gameScreen);
                frame.revalidate();
                frame.repaint();
            }
        });

        addFrameKeyboardBindings(pauseMenu, "ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(pauseMenu);
                gameScreen.grabFocus();
                gameScreenIsFocused = true;
                frame.revalidate();
                frame.repaint();
            }
        });

        addFrameKeyboardBindings(restartMenu, "ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.removeAll();
                try {
                    buildGrass();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

//                Resetting the snake body
                snakeBodyLocation.clear();
                snakeHeadXLocation = snakeHeadXLocationStart;
                snakeHeadYLocation = snakeHeadYLocationStart;

//                Resetting the direction
                direction = 'U';

//                Placing the apple
                currentGrassLocations = new ArrayList<>();
                for (int i = 0; i < columns * rows; i++)
                    currentGrassLocations.add(i);

//              Removing the Snake Location before randomising the apple
                currentGrassLocations.remove(snakeHeadYLocation * columns + snakeHeadXLocation);
                appleLocationInArray = (int) (Math.random() * currentGrassLocations.size());
                appleLocationInGridY = currentGrassLocations.get(appleLocationInArray) / columns;
                appleLocationInGridX = currentGrassLocations.get(appleLocationInArray) % columns;

                t[appleLocationInGridY][appleLocationInGridX].setIcon(new ImageIcon(scaledApple));

//        Setting the snake beginning location
                snakeBodyLocation.add(snakeHeadYLocation * columns + snakeHeadXLocation);
                t[snakeHeadYLocation][snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));

                frame.remove(restartMenu);
                gameScreenIsFocused = true;
                frame.revalidate();
                frame.repaint();
            }
        });

        snakeGameLoop();
    }

    public boolean snakeHitBody() {
        for (int i = 0; i < snakeBodyLocation.size() - 1; i++) {
            if (snakeBodyLocation.get(i) == snakeHeadYLocation * columns + snakeHeadXLocation) {
                return true;
            }
        }

        return false;
    }

    public static class JavaApplication {
        public static void main(String[] args) {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
        }
    }

    public static void addFrameKeyboardBindings(JComponent c, String key, final Action action) {
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        c.getActionMap().put(key, action);
        c.setFocusable(true);
    }

    public void buildGrass() throws IOException {

        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            {
                t[r][c] = new JLabel(new ImageIcon(scaledGrass));
                t[r][c].setBackground(Color.BLACK);
                t[r][c].setForeground(Color.BLACK);

                gameScreen.add(t[r][c]);
            }
        }
    }

    public void snakeGameLoop() {
            int snakeMoveSpeed = 0;

            while(true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (gameScreenIsFocused)
                    snakeMoveSpeed++;

                try {
                    if (gameScreenIsFocused && snakeMoveSpeed == 12) {
                        snakeMoveSpeed = 0;
                        if (direction == 'U') {
                            t[--snakeHeadYLocation][snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));
                        } else if (direction == 'D') {
                            t[++snakeHeadYLocation][snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));
                        } else if (direction == 'L') {
                            t[snakeHeadYLocation][--snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));
                        } else if (direction == 'R') {
                            t[snakeHeadYLocation][++snakeHeadXLocation].setIcon(new ImageIcon(scaledSnakeHead));
                        }

                        snakeBodyLocation.add(snakeHeadYLocation * columns + snakeHeadXLocation);
                        currentGrassLocations.remove(Integer.valueOf(snakeHeadYLocation * columns + snakeHeadXLocation));

                        if (snakeHeadYLocation == appleLocationInGridY && snakeHeadXLocation == appleLocationInGridX) {
                            appleLocationInArray = (int) (Math.random() * currentGrassLocations.size());
                            appleLocationInGridY = currentGrassLocations.get(appleLocationInArray) / columns;
                            appleLocationInGridX = currentGrassLocations.get(appleLocationInArray) % columns;
                            t[appleLocationInGridY][appleLocationInGridX].setIcon(new ImageIcon(scaledApple));
                        } else if (snakeHitBody()) {
                            frame.add(restartMenu);
                            restartMenu.grabFocus();
                            gameScreenIsFocused = false;

                            frame.revalidate();
                            frame.repaint();

//                No idea why I need to sleep for 1 millisecond to make the program work
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            frame.add(gameScreen);
                            frame.revalidate();
                            frame.repaint();

                            gameScreenIsFocused = false;

                        } else {
                            t[(snakeBodyLocation.get(0)) / columns][(snakeBodyLocation.get(0)) % columns].setIcon(new ImageIcon(scaledGrass));

                            currentGrassLocations.add(snakeBodyLocation.get(0));
                            snakeBodyLocation.remove(0);
                        }
                    }
                    frame.revalidate();
                    frame.repaint();
                } catch (Exception e) {
                    frame.add(restartMenu);

                    restartMenu.grabFocus();
                    gameScreenIsFocused = false;
                    frame.revalidate();
                    frame.repaint();

//                    No idea why I need to sleep for 1 millisecond to make the program work
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    frame.add(gameScreen);
                    frame.revalidate();
                    frame.repaint();

                }
            }
        }



    public static void main(String[] args) throws IOException {
        new Main();
    }
}