/**
 * Un record ayant pour but de lier un sudoku constitutif d'un multidoku avec son placement sur la grille du multidoku
 * @param sudoku Le sudoku
 * @param line Sa ligne sur la grille du multidoku, égal à celle de sa première ligne
 * @param column Sa colonne sur la grille du multidoku, égal à celle de sa première colonne
 */
public record PlacedSudoku(Sudoku sudoku, int line, int column) {
    public PlacedSudoku {
    }

    @Override
    public Sudoku sudoku() {
        return this.sudoku;
    }

    @Override
    public int line() {
        return this.line;
    }

    @Override
    public int column() {
        return this.column;
    }
}