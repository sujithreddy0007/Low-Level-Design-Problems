import java.util.*;

enum Symbol {
    X,
    O
}

class Player {
    String name;
    Symbol symbol;

    public Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

class Board {
    int size;
    Symbol[][] grid;
    int filledCells;

    public Board(int size) {
        this.size = size;
        this.grid = new Symbol[size][size];
        this.filledCells = 0;
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size &&
               col >= 0 && col < size &&
               grid[row][col] == null;
    }

    public boolean placeMove(int row, int col, Symbol symbol) {
        if(!isValidMove(row, col))
            return false;

        grid[row][col] = symbol;
        filledCells++;
        return true;
    }

    public boolean isFull() {
        return filledCells == size * size;
    }
}

class TicTacToe {

    Board board;
    Player player1;
    Player player2;
    Player currentPlayer;
    Player winner;
    boolean gameOver;

    public TicTacToe(int size, Player player1, Player player2) {
        board = new Board(size);
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        winner = null;
        gameOver = false;
    }

    public boolean makeMove(int row, int col) {

        if(gameOver)
            return false;

        if(!board.placeMove(row, col, currentPlayer.symbol))
            return false;

        if(checkWinner(row, col)) {
            winner = currentPlayer;
            gameOver = true;
            return true;
        }

        if(board.isFull()) {
            gameOver = true;
            return true;
        }

        currentPlayer = (currentPlayer == player1)
                        ? player2
                        : player1;

        return true;
    }

    private boolean checkWinner(int row, int col) {

        Symbol symbol = currentPlayer.symbol;
        int n = board.size;

        boolean rowWin = true;
        for(int j = 0; j < n; j++) {
            if(board.grid[row][j] != symbol) {
                rowWin = false;
                break;
            }
        }

        boolean colWin = true;
        for(int i = 0; i < n; i++) {
            if(board.grid[i][col] != symbol) {
                colWin = false;
                break;
            }
        }

        boolean diagWin = true;
        if(row == col) {
            for(int i = 0; i < n; i++) {
                if(board.grid[i][i] != symbol) {
                    diagWin = false;
                    break;
                }
            }
        } else {
            diagWin = false;
        }

        boolean antiDiagWin = true;
        if(row + col == n - 1) {
            for(int i = 0; i < n; i++) {
                if(board.grid[i][n - 1 - i] != symbol) {
                    antiDiagWin = false;
                    break;
                }
            }
        } else {
            antiDiagWin = false;
        }

        return rowWin || colWin || diagWin || antiDiagWin;
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}