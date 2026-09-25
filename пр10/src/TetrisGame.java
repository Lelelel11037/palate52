import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisGame extends JFrame {

    private GamePanel gamePanel;
    private JLabel scoreLabel;
    private Timer timer;
    private int delay = 500;
    private boolean isRunning = false;

    private int[][] board;
    private int currentPiece, nextPiece;
    private int currentX, currentY;
    private int score, level, linesCleared;

    private int cols = 10, rows = 20, unitSize = 25;
    private Random random = new Random();

    // 7 стандартных фигур
    private final int[][][] PIECES = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}}, // I
            {{1,1}, {1,1}},                                 // O
            {{0,1,0}, {1,1,1}, {0,0,0}},                    // T
            {{0,1,1}, {1,1,0}, {0,0,0}},                    // S
            {{1,1,0}, {0,1,1}, {0,0,0}},                    // Z
            {{1,0,0}, {1,1,1}, {0,0,0}},                    // L
            {{0,0,1}, {1,1,1}, {0,0,0}}                     // J
    };

    private final Color[] PIECE_COLORS = {
            Color.CYAN, Color.YELLOW, Color.MAGENTA,
            Color.GREEN, Color.RED, Color.ORANGE, Color.BLUE
    };

    private final int[] PIECE_SIZE = {4, 2, 3, 3, 3, 3, 3};

    public TetrisGame() {
        setTitle("Тетрис");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель управления
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ряд 1: кнопки выбора размера (одинаковый размер)
        gbc.gridy = 0;
        gbc.gridx = 0;

        JButton smallBtn = new JButton("8x14");
        JButton standardBtn = new JButton("10x20");
        JButton largeBtn = new JButton("12x24");

        Font btnFont = new Font("Arial", Font.BOLD, 12);
        smallBtn.setFont(btnFont);
        standardBtn.setFont(btnFont);
        largeBtn.setFont(btnFont);

        // Одинаковый размер кнопок
        Dimension btnSize = new Dimension(100, 30);
        smallBtn.setPreferredSize(btnSize);
        standardBtn.setPreferredSize(btnSize);
        largeBtn.setPreferredSize(btnSize);

        smallBtn.addActionListener(e -> startGame(8, 14, 30, 600));
        standardBtn.addActionListener(e -> startGame(10, 20, 25, 500));
        largeBtn.addActionListener(e -> startGame(12, 24, 20, 400));

        controlPanel.add(smallBtn, gbc);
        gbc.gridx = 1;
        controlPanel.add(standardBtn, gbc);
        gbc.gridx = 2;
        controlPanel.add(largeBtn, gbc);

        // Ряд 2: счёт, уровень и кнопка перезапуска
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        bottomPanel.setBackground(new Color(240, 240, 240));

        scoreLabel = new JLabel("Счёт: 0 | Уровень: 1");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(0, 100, 0));

        JButton restartBtn = new JButton("Новая игра");
        restartBtn.setFont(new Font("Arial", Font.BOLD, 14));
        restartBtn.setBackground(new Color(200, 200, 200));
        restartBtn.setFocusPainted(false);
        restartBtn.addActionListener(e -> startGame(cols, rows, unitSize, delay));

        bottomPanel.add(scoreLabel);
        bottomPanel.add(restartBtn);
        controlPanel.add(bottomPanel, gbc);

        add(controlPanel, BorderLayout.NORTH);

        // Игровая панель
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setFocusable(true);
        gamePanel.setPreferredSize(new Dimension(500, 500));
        add(gamePanel, BorderLayout.CENTER);

        // Управление
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isRunning) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: moveLeft(); break;
                    case KeyEvent.VK_RIGHT: moveRight(); break;
                    case KeyEvent.VK_DOWN: moveDown(); break;
                    case KeyEvent.VK_UP: rotatePiece(); break;
                    case KeyEvent.VK_SPACE: hardDrop(); break;
                }
                gamePanel.repaint();
            }
        });

        setVisible(true);
        startGame(10, 20, 25, 500);
    }

    // Запуск игры
    private void startGame(int c, int r, int u, int d) {
        this.cols = c;
        this.rows = r;
        this.unitSize = u;
        this.delay = d;

        board = new int[rows][cols];
        score = 0;
        level = 1;
        linesCleared = 0;
        isRunning = true;

        currentPiece = random.nextInt(7);
        nextPiece = random.nextInt(7);
        spawnPiece();

        if (timer != null) timer.stop();
        timer = new Timer(delay, e -> gameLoop());
        timer.start();

        updateScore();

        int panelWidth = cols * unitSize + 120;
        int panelHeight = rows * unitSize;
        gamePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        gamePanel.requestFocus();
    }

    // Появление новой фигуры
    private void spawnPiece() {
        currentPiece = nextPiece;
        nextPiece = random.nextInt(7);
        int size = PIECE_SIZE[currentPiece];
        currentX = cols / 2 - size / 2;
        currentY = 0;
        if (!isValidPosition(currentPiece, currentX, currentY)) {
            gameOver();
        }
    }

    // Проверка позиции
    private boolean isValidPosition(int piece, int x, int y) {
        int size = PIECE_SIZE[piece];
        int[][] shape = PIECES[piece];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (shape[r][c] != 0) {
                    int nx = x + c, ny = y + r;
                    if (nx < 0 || nx >= cols || ny >= rows) return false;
                    if (ny >= 0 && board[ny][nx] != 0) return false;
                }
            }
        }
        return true;
    }

    // Закрепление фигуры на поле
    private void lockPiece() {
        int size = PIECE_SIZE[currentPiece];
        int[][] shape = PIECES[currentPiece];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (shape[r][c] != 0) {
                    int nx = currentX + c, ny = currentY + r;
                    if (ny >= 0) board[ny][nx] = currentPiece + 1;
                }
            }
        }
        checkLines();
        spawnPiece();
        if (!isValidPosition(currentPiece, currentX, currentY)) {
            gameOver();
        }
    }

    // Проверка заполненных линий
    private void checkLines() {
        int lines = 0;
        for (int r = rows - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 0) { full = false; break; }
            }
            if (full) {
                for (int i = r; i > 0; i--) board[i] = board[i - 1].clone();
                board[0] = new int[cols];
                lines++;
                r++;
            }
        }
        if (lines > 0) {
            linesCleared += lines;
            score += lines * 100 + (lines - 1) * 100 * lines;
            updateScore();
            if (linesCleared >= level * 3) {
                level++;
                delay = Math.max(100, delay - 50);
                timer.setDelay(delay);
            }
        }
    }

    // Движение
    private void moveLeft() { if (isValidPosition(currentPiece, currentX - 1, currentY)) currentX--; }
    private void moveRight() { if (isValidPosition(currentPiece, currentX + 1, currentY)) currentX++; }
    private void moveDown() {
        if (isValidPosition(currentPiece, currentX, currentY + 1)) currentY++;
        else { lockPiece(); }
    }

    // Поворот
    private void rotatePiece() {
        int size = PIECE_SIZE[currentPiece];
        int[][] shape = PIECES[currentPiece];
        int[][] rotated = new int[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                rotated[c][size - 1 - r] = shape[r][c];
            }
        }
        int[][] temp = PIECES[currentPiece];
        PIECES[currentPiece] = rotated;
        if (!isValidPosition(currentPiece, currentX, currentY)) {
            PIECES[currentPiece] = temp;
        }
    }

    // Быстрое падение
    private void hardDrop() {
        while (isValidPosition(currentPiece, currentX, currentY + 1)) currentY++;
        lockPiece();
    }

    // Игровой цикл
    private void gameLoop() {
        if (!isRunning) return;
        if (isValidPosition(currentPiece, currentX, currentY + 1)) currentY++;
        else lockPiece();
        gamePanel.repaint();
    }

    // Конец игры
    private void gameOver() {
        isRunning = false;
        timer.stop();
        gamePanel.repaint();
    }

    // Обновление счёта
    private void updateScore() {
        scoreLabel.setText("Счёт: " + score + " | Уровень: " + level);
    }

    // Игровая панель
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth();
            int h = getHeight();

            // Поле
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (board[r][c] != 0) {
                        g.setColor(PIECE_COLORS[board[r][c] - 1]);
                        g.fillRect(c * unitSize, r * unitSize, unitSize - 1, unitSize - 1);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(c * unitSize, r * unitSize, unitSize - 1, unitSize - 1);
                    }
                }
            }

            // Текущая фигура
            if (isRunning) {
                int size = PIECE_SIZE[currentPiece];
                int[][] shape = PIECES[currentPiece];
                g.setColor(PIECE_COLORS[currentPiece]);
                for (int r = 0; r < size; r++) {
                    for (int c = 0; c < size; c++) {
                        if (shape[r][c] != 0 && currentY + r >= 0) {
                            g.fillRect((currentX + c) * unitSize, (currentY + r) * unitSize, unitSize - 1, unitSize - 1);
                        }
                    }
                }

                // Следующая фигура
                int nextSize = PIECE_SIZE[nextPiece];
                int[][] nextShape = PIECES[nextPiece];
                int offsetX = cols * unitSize + 20;
                int offsetY = 50;

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("Следующая:", offsetX, 30);

                for (int r = 0; r < nextSize; r++) {
                    for (int c = 0; c < nextSize; c++) {
                        if (nextShape[r][c] != 0) {
                            g.setColor(PIECE_COLORS[nextPiece]);
                            g.fillRect(offsetX + c * 25, offsetY + r * 25, 24, 24);
                        }
                    }
                }
            }

            // Экран окончания игры
            if (!isRunning) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, w, h);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                String text = "Игра окончена!";
                String scoreText = "Счёт: " + score;
                int tw = g.getFontMetrics().stringWidth(text);
                int sw = g.getFontMetrics().stringWidth(scoreText);
                g.drawString(text, (w - tw) / 2, h / 2 - 20);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString(scoreText, (w - sw) / 2, h / 2 + 40);
            }
        }
    }
}