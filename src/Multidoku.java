import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Un Multidoku est un puzzle constitué de plusieurs sudokus qui peuvent se superposer les uns sur les autres
 */
public class Multidoku implements Puzzle {
    /**
     * La liste des sudokus qui constituent le multidoku, qui ont la même taille, mais pas forcément les mêmes formes de blocs
     */
    private final ArrayList<PlacedSudoku> sudokus;
    /**
     * La taille des sudokus
     */
    private final int sizeSudokus;
    /**
     * La taille de la grille de multidoku où sont placés les sudokus
     */
    private final int sizeMultidokuGrid;
    /**
     * Les symboles utilisés pour remplacer les nombres lors de l'affichage du multidoku, s'ils sont définis
     */
    private HashMap<Integer, String> symbols;

    /**
     * Constructeur de la classe, sans les symboles
     * @param sudokus La liste des sudokus constituant le multidoku
     * @throws IllegalArgumentException Si les sudokus ne font pas la même taille
     */
    public Multidoku(ArrayList<PlacedSudoku> sudokus) throws IllegalArgumentException {
        int size = sudokus.getFirst().sudoku().getSize();
        int gridSize = size;
        for (PlacedSudoku sudoku : sudokus) {
            if (size != sudoku.sudoku().getSize()) throw new IllegalArgumentException("Les sudokus ne font pas tous la même taille");
            if (sudoku.line() + size > gridSize) gridSize = sudoku.line() + size;
            if (sudoku.column() + size > gridSize) gridSize = sudoku.column() + size;
        }

        this.sudokus = sudokus;
        this.sizeSudokus = size;
        this.sizeMultidokuGrid = gridSize;
        this.symbols = null;
    }

    /**
     * Constructeur de la classe, avec les symboles
     * @param sudokus La liste des sudokus constituant le multidoku
     * @param symbols Les symboles utilisés lors de l'affichage du multidoku
     * @throws IllegalArgumentException Si les sudokus ne font pas la même taille
     */
    public Multidoku(ArrayList<PlacedSudoku> sudokus, HashMap<Integer, String> symbols) throws IllegalArgumentException {
        this(sudokus);
        if (symbols != null) {
            this.setSymbols(symbols);
        }
    }

    /**
     * Indique au multidoku quels symboles utiliser, qui doivent être numérotés de 0 à (taille des sudokus - 1)
     * @param symbols Les nouveaux symboles à utiliser
     * @throws IllegalArgumentException Si les arguments passés ne créent pas une liste de symboles valide
     */
    public void setSymbols(HashMap<Integer, String> symbols) throws IllegalArgumentException {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < this.sizeSudokus; i++) {
            set.add(i);
        }

        if (symbols.keySet().equals(set)) {
            this.symbols = symbols;
        }
        else {
            throw new IllegalArgumentException("Les symboles n'ont pas les bons numéros");
        }
    }

    /**
     * Getter des sudokus constitutifs du multidoku
     * @return Les sudokus constitutifs du multidoku
     */
    public ArrayList<PlacedSudoku> getSudokus() {
        return this.sudokus;
    }

    /**
     * Getter d'un sudoku du multidoku en fonction de sa position dans la grille du multidoku
     * @param line La ligne du sudoku dans la grille du multidoku
     * @param column La colonne du sudoku dans la grille du multidoku
     * @return Le sudoku demandé, s'il existe
     * Si les arguments passés n'obtiennent pas un sudoku valide
     */
    public Sudoku getSudoku(int line, int column) throws IllegalArgumentException {
        for (PlacedSudoku sudoku : this.sudokus) {
            if (sudoku.line() == line && sudoku.column() == column) {
                return sudoku.sudoku();
            }
        }
        throw new IllegalArgumentException("Le multidoku ne contient pas de sudoku à la ligne " + line + " et colonne " + column);
    }

    /**
     * Getter de la taille des sudokus
     * @return La taille des sudokus
     */
    public int getSizeSudokus() {
        return this.sizeSudokus;
    }

    /**
     * Getter de la taille de la grille de multidoku
     * @return La taille de la grille de multidoku
     */
    public int getSizeMultidokuGrid() {
        return this.sizeMultidokuGrid;
    }

    /**
     * Getter des symboles utilisés lors de l'affichage du multidoku
     * @return Les symboles utilisés lors de l'affichage du multidoku
     */
    public HashMap<Integer, String> getSymbols() {
        return this.symbols;
    }

    /**
     * Retourne les contraintes internes entre les cases du multidoku
     * @return Une liste de contraintes entre cases
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints() {
        //TODO
        return null;
    }

    /**
     * Crée un string qui affiche le multidoku comme une grille, avec les valeurs des cases déjà remplies et des couleurs différentes pour chaque bloc
     * @return Un string qui permet d'afficher le sudoku
     */
    @Override
    public String toString() {
        //TODO
        return "";
    }
}
