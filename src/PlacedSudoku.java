public record PlacedSudoku(Sudoku sudoku, int row, int col) {
    public PlacedSudoku(Sudoku sudoku, int row, int col) {
        this.sudoku = sudoku;
        int sizeSudoku = sudoku.getTaille();
        if (row < 0 || row >= sizeSudoku || col < 0 || col >= sizeSudoku) {
            throw new IllegalArgumentException("Row or col out of bounds");
        }
        this.row = row;
        this.col = col;
    }

    public Sudoku getSudoku() {
        return this.sudoku;
    }

    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }
}
