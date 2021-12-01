package models;

import main.Move;
// by Salah LAMHAYA

public class Board {

    public Token[][] boardArray;
    public int size;

    public Board(int size) {
        this.boardArray = new Token[size][size];
        this.size = size;
        setupBoard();
    }

    public Board(Board board) {
        this.boardArray = board.boardArray;
        this.size = board.size;
    }

    public void setupBoard() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (y < 3 && checkerboardSpace(x, y)) {
                    this.boardArray[y][x] = new Token(x, y, true);
                } else if (y >= size - 3 && checkerboardSpace(x, y)) {
                    this.boardArray[y][x] = new Token(x, y, false);
                }
            }
        }
    }

    public void applyMoveToBoard(Move move, Token Token) {
        int[] moveStartingPos = Token.getCoordinates();
        int[] moveEndingPos = move.getEndingPosition();
        Token[] jumpedTokens = move.getJumpedTokens(this);
        if (jumpedTokens != null) {
            for (int i = 0; i < jumpedTokens.length; i++) {
                if (jumpedTokens[i] != null) {
                    this.setValue(jumpedTokens[i].getCoordinates()[0], jumpedTokens[i].getCoordinates()[1], null);
                }
            }
        }
        this.setValue(moveStartingPos[0], moveStartingPos[1], null);
        Token.moveTo(moveEndingPos[0], moveEndingPos[1]);
        Token.checkIfShouldBeKing(this);
        this.setValue(moveEndingPos[0], moveEndingPos[1], Token);
    }

    private void setValue(int x, int y, Token Token) {
        this.boardArray[y][x] = Token;
    }

    public Token getValue(int x, int y) {
        return this.boardArray[y][x];
    }

    public Token getValue(int position) {
        int[] coords = getCoordinatesFromPosition(position);
        return this.getValue(coords[0], coords[1]);
    }

    public int[] getCoordinatesFromPosition(int position) {
        int[] coords = new int[2];

        coords[0] = position % this.size;
        coords[1] = position / this.size;
        return coords;
    }

    public int getPositionFromCoordinates(int x, int y) {
        return this.size * y + x;
    }

    public boolean checkerboardSpace(int x, int y) {
        return x % 2 == y % 2;
    }

    public boolean outOfBoard(int x, int y) {
        return (x < 0 || x >= this.size ||
                y < 0 || y >= this.size);
    }

    public boolean outOfBoard(int position) {
        int[] coords = getCoordinatesFromPosition(position);
        return this.outOfBoard(coords[0], coords[1]);
    }

    public Board newGame() {
        Board newBoard = new Board(this);
        for (int y = 0; y < newBoard.size; y++) {
            for (int x = 0; x < newBoard.size; x++) {
                Token oldToken = this.getValue(this.size - 1 - x, this.size - 1 - y);

                if (oldToken != null) {
                    newBoard.setValue(x, y, new Token(x, y, oldToken.isWhite));
                } else {
                    newBoard.setValue(x, y, null);
                }
            }
        }

        return newBoard;
    }
}