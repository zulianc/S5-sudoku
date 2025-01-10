public class PlacedSudoku {
    private Sudoku sudoku;
    private int row;
    private int col;

    public PlacedSudoku(Sudoku sudoku, int row, int col) {
        this.sudoku = sudoku;
        this.row = row;
        this.col = col;
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
