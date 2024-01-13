import java.util.Random;
import java.util.Scanner;

public class Main {
    private char[][] gameBoard;
    private char[][] mines;
    private int rows;
    private int cols;
    private int minesCount;

    public Main(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.gameBoard = new char[rows][cols];
        this.mines = new char[rows][cols];
        this.minesCount = (rows * cols) / 4;
        initializeGameBoard();
        placeMines();
        updateGameBoard();
    }

    private void initializeGameBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gameBoard[i][j] = '-';
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int count = 0;
        while (count < minesCount) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (mines[row][col] != '*') {
                mines[row][col] = '*';
                count++;
            }
        }
    }

    private void updateGameBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gameBoard[i][j] != '-') continue;
                int count = countAdjacentMines(i, j);
                if (count > 0) {
                    gameBoard[i][j] = (char) (count + '0');
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (row + i >= 0 && row + i < rows && col + j >= 0 && col + j < cols) {
                    if (mines[row + i][col + j] == '*') {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayGameBoard();
            System.out.print("Satır Ve Sütun Giriniz. (e.g., 1 2): ");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            if (row < 0 || row >= rows || col < 0 || col >= cols) {
                System.out.println("Geçersiz Sütur Veya Satır! Tekrar Deneyiniz.");
                continue;
            }

            if (gameBoard[row][col] != '-') {
                System.out.println("Bu hücre zaten seçildi! Başka birini seç.");
                continue;
            }

            if (mines[row][col] == '*') {
                System.out.println("Mayına Bastınız. Oyun Bitti!");
                displayMines();
                break;
            } else {
                revealCell(row, col);
                if (checkWin()) {
                    System.out.println("Tebrikler. Bütün Mayınları Temizlediniz! Sen Kazandın");
                    break;
                }
            }
        }
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols || gameBoard[row][col] != '-') {
            return;
        }
        int count = countAdjacentMines(row, col);
        gameBoard[row][col] = (count == 0) ? '0' : (char) (count + '0');
        if (count == 0) {
            revealCell(row - 1, col - 1);
            revealCell(row - 1, col);
            revealCell(row - 1, col + 1);
            revealCell(row, col - 1);
            revealCell(row, col + 1);
            revealCell(row + 1, col - 1);
            revealCell(row + 1, col);
            revealCell(row + 1, col + 1);
        }
    }

    private boolean checkWin() {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gameBoard[i][j] == '-') {
                    count++;
                }
            }
        }
        return count == minesCount;
    }

    private void displayGameBoard() {
        System.out.println("Game Board:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void displayMines() {
        System.out.println("Mines:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(mines[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows, cols;
        while (true) {
            System.out.print("Lütfen Bir Satır Sayısı Giriniz. (minimum 2): ");
            rows = scanner.nextInt();
            System.out.print("Lütfen Sütur Sayısı Giriniz.(minimum 2): ");
            cols = scanner.nextInt();
            if (rows < 2 || cols < 2) {
                System.out.println("Geçersiz Bir Sayı Girdiniz Lütfen Tekrar Deneyiniz");
            } else {
                break;
            }
        }
        Main game = new Main(rows, cols);
        game.play();
    }
}