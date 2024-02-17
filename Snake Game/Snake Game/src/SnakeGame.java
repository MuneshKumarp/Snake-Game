import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;

    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        snake = new ArrayList<>();
        initializeSnake();

        randomFood();

        addKeyListener(this);
        setFocusable(true);

        JPanel gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        add(gamePanel);
    }

    private void initializeSnake() {
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
    }

    public void startGame() {
        running = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
            repaint();
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.GRAY);
            for (int x = 0; x < WIDTH; x += UNIT_SIZE) {
                g.drawLine(x, 0, x, HEIGHT);
            }
            for (int y = 0; y < HEIGHT; y += UNIT_SIZE) {
                g.drawLine(0, y, WIDTH, y);
            }

            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.RED);
            g.fillRect(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
    }
    public void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case 'U':
                newHead.y--;
                break;
            case 'D':
                newHead.y++;
                break;
            case 'L':
                newHead.x--;
                break;
            case 'R':
                newHead.x++;
                break;
        }

        newHead.x = (newHead.x + WIDTH / UNIT_SIZE) % (WIDTH / UNIT_SIZE);
        newHead.y = (newHead.y + HEIGHT / UNIT_SIZE) % (HEIGHT / UNIT_SIZE);

        snake.add(0, newHead);
        if (!head.equals(food)) {
            snake.remove(snake.size() - 1);
        } else {
            randomFood();
        }
    }

    public void checkFood() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            snake.add(new Point(-1, -1));
            randomFood();
        }
    }

    public void checkCollisions() {
        Point head = snake.get(0);

        // Check collision with borders
        if (head.x < 0 || head.x >= WIDTH / UNIT_SIZE || head.y < 0 || head.y >= HEIGHT / UNIT_SIZE) {
            running = false;
            gameOver();
            return;
        }

        // Check self-collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                gameOver();
                return;
            }
        }
    }

    public void gameOver() {
        System.out.println("Game Over");
    }

    public void randomFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(WIDTH / UNIT_SIZE);
            y = random.nextInt(HEIGHT / UNIT_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
            game.startGame();
        });
    }
}
