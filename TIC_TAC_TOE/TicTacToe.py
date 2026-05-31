from enum import Enum


class Symbol(Enum):
    X = "X"
    O = "O"


class Player:
    def __init__(self, name: str, symbol: Symbol):
        self.name = name
        self.symbol = symbol


class Board:
    def __init__(self, size: int):
        self.size = size
        self.grid = [[None] * size for _ in range(size)]
        self.filled_cells = 0

    def isValidMove(self, row: int, col: int) -> bool:
        return (0 <= row < self.size and
                0 <= col < self.size and
                self.grid[row][col] is None)

    def placeMove(self, row: int, col: int, symbol: Symbol) -> bool:
        if not self.isValidMove(row, col):
            return False

        self.grid[row][col] = symbol
        self.filled_cells += 1
        return True

    def isFull(self) -> bool:
        return self.filled_cells == self.size * self.size


class TicTacToe:
    def __init__(self, size: int, player1: Player, player2: Player):
        self.board = Board(size)
        self.player1 = player1
        self.player2 = player2

        self.current_player = player1
        self.winner = None
        self.game_over = False

    def makeMove(self, row: int, col: int) -> bool:

        if self.game_over:
            return False

        if not self.board.placeMove(
                row,
                col,
                self.current_player.symbol):
            return False

        if self._checkWinner(row, col):
            self.winner = self.current_player
            self.game_over = True
            return True

        if self.board.isFull():
            self.game_over = True
            return True

        self.current_player = (
            self.player2
            if self.current_player == self.player1
            else self.player1
        )

        return True

    def _checkWinner(self, row: int, col: int) -> bool:

        symbol = self.current_player.symbol
        n = self.board.size

        row_win = all(
            self.board.grid[row][j] == symbol
            for j in range(n)
        )

        col_win = all(
            self.board.grid[i][col] == symbol
            for i in range(n)
        )

        diag_win = (
            row == col and
            all(
                self.board.grid[i][i] == symbol
                for i in range(n)
            )
        )

        anti_diag_win = (
            row + col == n - 1 and
            all(
                self.board.grid[i][n - 1 - i] == symbol
                for i in range(n)
            )
        )

        return row_win or col_win or diag_win or anti_diag_win

    def getWinner(self):
        return self.winner

    def isGameOver(self) -> bool:
        return self.game_over

    def getCurrentPlayer(self):
        return self.current_player