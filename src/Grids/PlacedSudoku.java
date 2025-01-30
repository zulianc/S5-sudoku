package Grids;

/**
 * Un record ayant pour but de lier un sudoku constitutif d'un multidoku avec son placement sur la grille du multidoku
 * @param sudoku Le sudoku
 * @param line Sa ligne sur la grille du multidoku, égal à celle de sa première ligne
 * @param column Sa colonne sur la grille du multidoku, égal à celle de sa première colonne
 */
public record PlacedSudoku(Sudoku sudoku, int line, int column) {
    /**
     * Constructeur
     * @param sudoku Le sudoku
     * @param line Sa ligne sur la grille du multidoku, égal à celle de sa première ligne
     * @param column Sa colonne sur la grille du multidoku, égal à celle de sa première colonne
     */
    public PlacedSudoku {
    }

    /**
     * Getter du sudoku
     * @return Le sudoku
     */
    @Override
    public Sudoku sudoku() {
        return this.sudoku;
    }

    /**
     * Getter de la ligne du sudoku
     * @return La ligne du sudoku
     */
    @Override
    public int line() {
        return this.line;
    }

    /**
     * Getter de la colonne du sudoku
     * @return La colonne du sudoku
     */
    @Override
    public int column() {
        return this.column;
    }
}