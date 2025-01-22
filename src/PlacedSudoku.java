public class PlacedSudoku {
    private final Sudoku sudoku;
    private final int row;
    private final int col;

    public PlacedSudoku(Sudoku sudoku, int row, int col) {
        this.sudoku = sudoku;
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
