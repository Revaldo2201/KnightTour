import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class GameKudaAlpro extends JFrame {
    private final int size = 8;
    private int[][] board;
    private final int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};
    private JButton[][] guiBoard;
    private boolean isStartingPositionSelected = false;
    private int startX, startY;

    public GameKudaAlpro() {
        setTitle("Knight's Tour Solver (Custom Start)");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(size, size));
        setLocationRelativeTo(null);

        board = new int[size][size];
        for (int[] row : board) Arrays.fill(row, -1);

        guiBoard = new JButton[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                int x = i;
                int y = j;
                button.addActionListener(e -> handleCellClick(x, y));
                guiBoard[i][j] = button;
                add(button);
            }
        }
    }

    private void handleCellClick(int x, int y) {
        if (!isStartingPositionSelected) {
            startX = x;
            startY = y;
            isStartingPositionSelected = true;
            guiBoard[x][y].setBackground(Color.YELLOW);
            startTour();
        }
    }

    private void startTour() {
        if (solveTour(startX, startY, 1)) {
            updateBoard();
            JOptionPane.showMessageDialog(this, "Solved!");
        } else {
            JOptionPane.showMessageDialog(this, "No solution found.");
        }
    }

    private boolean solveTour(int x, int y, int moveCount) {
        board[x][y] = moveCount;
        if (moveCount == size * size) {
            return true;
        }

        int[] nextMoves = nextMovesOrder(x, y);
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[nextMoves[i]];
            int ny = y + dy[nextMoves[i]];

            if (isValid(nx, ny)) {
                if (solveTour(nx, ny, moveCount + 1)) {
                    return true;
                }
            }
        }

        board[x][y] = -1; // backtrack
        return false;
    }

    private int[] nextMovesOrder(int x, int y) {
        int[] order = new int[8];
        int[] degree = new int[8];
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny)) {
                degree[i] = countNextMoves(nx, ny);
            } else {
                degree[i] = 9;
            }
            order[i] = i;
        }

        // Sort berdasarkan degree terkecil (Warnsdorff rule)
        for (int i = 0; i < 8 - 1; i++) {
            for (int j = i + 1; j < 8; j++) {
                if (degree[order[i]] > degree[order[j]]) {
                    int tmp = order[i];
                    order[i] = order[j];
                    order[j] = tmp;
                }
            }
        }
        return order;
    }

    private int countNextMoves(int x, int y) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny)) count++;
        }
        return count;
    }

    private boolean isValid(int x, int y) {
        return (x >= 0 && y >= 0 && x < size && y < size && board[x][y] == -1);
    }

    private void updateBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                guiBoard[i][j].setText(String.valueOf(board[i][j]));
                guiBoard[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameKudaAlpro solver = new GameKudaAlpro();
            solver.setVisible(true);
        });
    }
}
