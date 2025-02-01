package Grids;

import Constraints.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
     * Une liste de contraintes spécifiées sur le sudoku en plus des contraintes de base (lignes, colonnes, blocs)
     */
    private ArrayList<SudokuConstraint> addedConstraints;

    /**
     * Constructeur de la classe, sans les symboles
     * @param sudokus La liste des sudokus constituant le multidoku
     * @throws IllegalArgumentException Si les sudokus donnés ne permettent pas de créer un multidoku valide
     */
    public Multidoku(ArrayList<PlacedSudoku> sudokus) throws IllegalArgumentException {
        if (sudokus == null || sudokus.isEmpty()) {
            throw new IllegalArgumentException("Il doit y avoir au moins un sudoku dans un multidoku");
        }

        int size = sudokus.getFirst().sudoku().getSize();
        int gridSize = size;
        ArrayList<PlacedSudoku> testedSudokus = new ArrayList<>();
        for (PlacedSudoku sudoku : sudokus) {
            // on teste la taille du sudoku
            if (size != sudoku.sudoku().getSize()) throw new IllegalArgumentException("Les sudokus ne font pas tous la même taille");
            // on teste le placement du sudoku
            for (PlacedSudoku testedSudoku : testedSudokus) {
                if (testedSudoku.line() == sudoku.line() && testedSudoku.column() == sudoku.column()) {
                    throw new IllegalArgumentException("Les sudokus ne peuvent pas être placés au même endroit");
                }
            }
            testedSudokus.add(sudoku);
            // on met à jour la taille de la grille
            if (sudoku.line() + size > gridSize) gridSize = sudoku.line() + size;
            if (sudoku.column() + size > gridSize) gridSize = sudoku.column() + size;
        }

        this.sudokus = sudokus;
        this.sizeSudokus = size;
        this.sizeMultidokuGrid = gridSize;
        this.symbols = null;
        this.addedConstraints = new ArrayList<>();
    }

    /**
     * Constructeur de la classe, avec les symboles
     * @param sudokus La liste des sudokus constituant le multidoku
     * @param symbols Les symboles utilisés lors de l'affichage du multidoku
     * @throws IllegalArgumentException Si les sudokus donnés ne permettent pas de créer un multidoku valide, ou si les symboles ne sont pas corrects
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
        HashSet<Integer> set = new HashSet<>();
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
     * Ajoute au multidoku des nouvelles contraintes
     * @param constraints Les contraintes à ajouter
     * @throws IllegalArgumentException Si les contraintes ne s'appliquent pas sur ce multidoku
     */
    public void setAddedConstraints(ArrayList<SudokuConstraint> constraints) throws IllegalArgumentException {
        for (SudokuConstraint constraint : constraints) {
            if (!constraint.isConstraintOnPuzzle(this)) {
                throw new IllegalArgumentException("La contrainte ne s'applique pas sur ce multidoku !");
            }
        }
        this.addedConstraints = constraints;
    }

    /**
     * Getter des sudokus constitutifs du multidoku
     * @return Les sudokus constitutifs du multidoku
     */
    public ArrayList<PlacedSudoku> getSudokus() {
        return this.sudokus;
    }

    /**
     * Getter d'un sudoku du multidoku en fonction d'une case qui y appartient
     * @param c La case censée appartenir au sudoku
     * @return Le sudoku auquel appartient la case, s'il existe
     * @throws IllegalArgumentException Si la case n'appartient pas au multidoku
     */
    public PlacedSudoku getSudoku(Case c) throws IllegalArgumentException{
        for (PlacedSudoku sudoku : this.sudokus) {
            if (sudoku.sudoku().getCase(c.getLine(), c.getColumn()) == c) {
                return sudoku;
            }
        }
        throw new IllegalArgumentException("La case n'appartient pas au multidoku");
    }

    /**
     * Getter d'un sudoku du multidoku en fonction de sa position dans la grille du multidoku
     * @param line La ligne du sudoku dans la grille du multidoku
     * @param column La colonne du sudoku dans la grille du multidoku
     * @return Le sudoku demandé, s'il existe
     * Si les arguments passés n'obtiennent pas un sudoku valide
     */
    public PlacedSudoku getSudoku(int line, int column) throws IllegalArgumentException {
        for (PlacedSudoku sudoku : this.sudokus) {
            if (sudoku.line() == line && sudoku.column() == column) {
                return sudoku;
            }
        }
        throw new IllegalArgumentException("Le multidoku ne contient pas de sudoku à la ligne " + (line + 1) + " et colonne " + (column + 1));
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
     * Getter des contraintes additionnelles du multidoku
     * @return Les contraintes additionnelles du multidoku
     */
    public ArrayList<SudokuConstraint> getAddedConstraints() {
        return this.addedConstraints;
    }

    /**
     * Retourne les contraintes internes entre les cases du multidoku
     * @param puzzle Si on veut que les contraintes pointent sur un autre puzzle
     * @return Une liste de contraintes entre cases
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints(Puzzle puzzle) throws RuntimeException {
        try {
            // on set le puzzle sur lequel pointent les contraintes
            if (puzzle == null) {
                puzzle = this;
            }

            ArrayList<SudokuConstraint> constraints = new ArrayList<>();
            // on parcourt chaque sudoku dans le multidoku
            for (PlacedSudoku placedSudoku : this.sudokus) {
                // on ajoute les contraintes internes du sudoku (lignes, colonnes, blocs)
                constraints.addAll(placedSudoku.sudoku().defaultConstraints(this));

                // puis, on parcourt les cases du sudoku actuel
                for (int i = 0; i < this.sizeSudokus; i++) {
                    for (int j = 0; j < this.sizeSudokus; j++) {
                        Case currentCase = placedSudoku.sudoku().getCase(i, j);

                        // on compare la case actuelle avec les cases des autres sudokus
                        for (PlacedSudoku otherSudoku : this.sudokus) {
                            if (otherSudoku == placedSudoku) continue;
                            // si la case overlap avec ce sudoku
                            if (placedSudoku.line() + i >= otherSudoku.line() && placedSudoku.line() + i < otherSudoku.line() + this.sizeSudokus) {
                                if (placedSudoku.column() + j >= otherSudoku.column() && placedSudoku.column() + j < otherSudoku.column() + this.sizeSudokus) {
                                    // on récupère la case qui overlap
                                    Case otherCase = otherSudoku.sudoku().getCase(placedSudoku.line() + i - otherSudoku.line(), placedSudoku.column() + j - otherSudoku.column());
                                    // on crée une contrainte d'égalité entre la case actuelle et la case correspondante
                                    ArrayList<Case> otherCaseList = new ArrayList<>();
                                    otherCaseList.add(otherCase);
                                    SudokuConstraint newConstraint = new EqualConstraint(currentCase, otherCaseList, puzzle);
                                    constraints.add(newConstraint);
                                }
                            }
                        }
                    }
                }
            }
            return constraints;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Retourne les contraintes internes appliquées sur une case du multidoku
     * @param c La case sur laquelle les contraintes sont appliquées
     * @param puzzle Si on veut que les contraintes pointent sur un autre puzzle
     * @return La liste des contraintes appliquées sur cette case
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> constraintsOnCase(Case c, Puzzle puzzle) throws RuntimeException {
        try {
            // on set le puzzle sur lequel pointent les contraintes
            if (puzzle == null) {
                puzzle = this;
            }

            // on rajoute les contraintes de la case dans son sudoku
            PlacedSudoku caseSudoku = this.getSudoku(c);
            ArrayList<SudokuConstraint> constraints = new ArrayList<>(caseSudoku.sudoku().constraintsOnCase(c, this));

            // ensuite, on parcourt de chaque Sudoku dans le Multidoku
            for (PlacedSudoku otherSudoku : this.sudokus) {
                if (caseSudoku == otherSudoku) continue;
                // si la case overlap avec ce sudoku
                if (caseSudoku.line() + c.getLine() >= otherSudoku.line() && caseSudoku.line() + c.getLine() < otherSudoku.line() + this.sizeSudokus) {
                    if (caseSudoku.column() + c.getColumn() >= otherSudoku.column() && caseSudoku.column() + c.getColumn() < otherSudoku.column() + this.sizeSudokus) {
                        // on récupère la case qui overlap
                        Case otherCase = otherSudoku.sudoku().getCase(otherSudoku.line() + c.getLine() - otherSudoku.line(), otherSudoku.column() + c.getColumn() - otherSudoku.column());
                        // on crée une contrainte d'égalité entre la case en paramètre et la case qui overlap
                        ArrayList<Case> otherCaseList = new ArrayList<>();
                        otherCaseList.add(otherCase);
                        SudokuConstraint newConstraint = new EqualConstraint(c, otherCaseList, puzzle);
                        constraints.add(newConstraint);
                    }
                }
            }
            return constraints;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Retourne la liste des cases constituant le multidoku, toujours dans le même ordre
     * @return La liste des cases constituant le multidoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<Case> casesList() throws RuntimeException {
        try {
            ArrayList<Case> cases = new ArrayList<>();
            for (PlacedSudoku placedSudoku : this.sudokus) {
                cases.addAll(placedSudoku.sudoku().casesList());
            }
            return cases;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Crée une copie du multidoku, qui ne comporte aucune référence vers le multidoku originel
     * @return Une copie du multidoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public Puzzle copy() {
        try {
            // on copie les sudokus
            ArrayList<PlacedSudoku> newSudokus = new ArrayList<>();
            for (PlacedSudoku placedSudoku : this.sudokus) {
                newSudokus.add(new PlacedSudoku(placedSudoku.sudoku().copy(), placedSudoku.line(), placedSudoku.column()));
            }

            // on copie les symboles
            HashMap<Integer, String> symbols = null;
            if (this.symbols != null) {
                symbols = new HashMap<>(this.symbols);
            }

            return new Multidoku(newSudokus, symbols);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Crée un string qui affiche le multidoku comme une grille, avec les valeurs des cases déjà remplies et des couleurs différentes pour chaque bloc
     * @return Un string qui permet d'afficher le sudoku
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int gridSize = this.sizeMultidokuGrid;

        // on change les symboles utilisés pour qu'ils soient tous de la même taille
        HashMap<Integer, String> symbolsToPrint = new HashMap<>();
        if (this.symbols != null) {
            symbolsToPrint.putAll(this.symbols);
        }
        else {
            for (int i = 0; i < this.sizeSudokus; i++) {
                symbolsToPrint.put(i, Integer.toString(i + 1));
            }
        }
        symbolsToPrint.put(-1, " ");
        symbolsToPrint.put(-2, " ");
        int maxSize = 0;
        for (Integer key : symbolsToPrint.keySet()) {
            if (symbolsToPrint.get(key).length() > maxSize) {
                maxSize = symbolsToPrint.get(key).length();
            }
        }
        for (Integer key : symbolsToPrint.keySet()) {
            for (int i = symbolsToPrint.get(key).length(); i < maxSize; i++) {
                symbolsToPrint.replace(key, " " + symbolsToPrint.get(key));
            }
        }

        // on crée une grille vide avec -2 représentant les endroits qui n'ont pas de cases
        int[][] grid = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = -2;
            }
        }

        // ce compteur servira à déterminer les indices de bloc globaux
        for (PlacedSudoku placedSudoku : this.sudokus) {
            Sudoku sudoku = placedSudoku.sudoku();
            int rowStart = placedSudoku.line();
            int colStart = placedSudoku.column();

            // on place les valeurs dans la grille là où le sudoku est placé
            for (int i = 0; i < this.sizeSudokus; i++) {
                for (int j = 0; j < this.sizeSudokus; j++) {
                    int value = sudoku.getCases()[i][j].getValue();
                    if (grid[rowStart + i][colStart + j] != value) {
                        if (grid[rowStart + i][colStart + j] == -2 || grid[rowStart + i][colStart + j] == -1) {
                            grid[rowStart + i][colStart + j] = value;
                        }
                    }
                }
            }
        }

        // Affichage de la grille avec couleurs par bloc spécifique au Sudoku
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int blocIndex = -1;
                int nbr = 0;

                // on trouve le bloc auquel appartient la case et on calcule l'index global du bloc
                for (PlacedSudoku placedSudoku : this.sudokus) {
                    if (i >= placedSudoku.line() && i < placedSudoku.line() + this.sizeSudokus && j >= placedSudoku.column() && j < placedSudoku.column() + this.sizeSudokus) {
                        Sudoku sudoku = placedSudoku.sudoku();
                        // l'index du bloc local du Sudoku + l'index global de celui-ci
                        blocIndex = sudoku.getCase(i - placedSudoku.line(), j - placedSudoku.column()).getBlocIndex() + nbr * this.sizeSudokus;
                        break;
                    }
                    nbr++;
                }

                // calcul de la couleur en fonction du bloc
                int red = Math.abs((blocIndex * 67 + 123) % 256);
                int green = Math.abs((blocIndex * 151 + 231) % 256);
                int blue = Math.abs((blocIndex * 199 + 87) % 256);

                // applique la couleur pour la case
                sb.append("\033[38;2;").append(red).append(";").append(green).append(";").append(blue).append("m");

                // affichage de la case
                if (grid[i][j] == -2) {
                    sb.append(" ");
                }
                else {
                    sb.append("[");
                }
                sb.append(symbolsToPrint.get(grid[i][j]));
                if (grid[i][j] == -2) {
                    sb.append(" ");
                }
                else {
                    sb.append("]");
                }
            }
            if (!(i == gridSize - 1))
                sb.append("\n");
        }
        // remet la couleur normale
        sb.append("\033[38;2;255;255;255m");

        return sb.toString();
    }
}
