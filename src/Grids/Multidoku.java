package Grids;

import Constraints.*;

import java.util.ArrayList;
import java.util.Arrays;
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
     * @return Une liste de contraintes entre cases
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints() {
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();

        // Parcours de chaque Sudoku dans le Multidoku
        for (PlacedSudoku placedSudoku : this.sudokus) {
            // Ajoute les contraintes internes du Sudoku (lignes, colonnes, blocs)
            constraints.addAll(placedSudoku.sudoku().defaultConstraints());



            // Parcours des cases du Sudoku actuel
            for (int i = 0; i < sizeSudokus; i++) {
                for (int j = 0; j < sizeSudokus; j++) {

                    Case currentCase = placedSudoku.sudoku().getCase(i, j);

                    // Compare la case actuelle avec les cases des autres Sudokus
                    for (PlacedSudoku otherSudoku : this.sudokus) {
                        if (otherSudoku != placedSudoku) {
                            for (int a = 0; a < sizeSudokus; a++) {
                                for (int b = 0; b < sizeSudokus; b++) {
                                    if (placedSudoku.line() + i == otherSudoku.line() + a && placedSudoku.column() + j == otherSudoku.column() + b) {
                                        Sudoku other = otherSudoku.sudoku();
                                        Case otherCase = other.getCase(a, b);  // Récupère la case correspondante dans l'autre Sudoku
                                        if (otherCase != null) {
                                            // Crée une contrainte d'égalité entre la case actuelle et la case correspondante
                                            ArrayList<Case> otherCaseList = new ArrayList<>();
                                            otherCaseList.add(otherCase);
                                            SudokuConstraint newConstraint = new EqualConstraint(currentCase, otherCaseList, this);
                                            constraints.add(newConstraint);
                                        }
                                    }
                                }


                            }



                        }
                    }
                }
            }
        }
        return constraints;
    }





    /**
     * Retourne les contraintes internes appliquées sur une case du multidoku
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> constraintsOnCase(Case c) {
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        NotEqualConstraint newConstraint;
        ArrayList<Case> toInsert;

        // Parcours de chaque Sudoku dans le Multidoku
        for (PlacedSudoku placedSudoku : this.sudokus) {

            // Recherche de la case dans chaque Sudoku du Multidoku
            for (int i = 0; i < placedSudoku.sudoku().getSize(); i++) {
                for (int j = 0; j < placedSudoku.sudoku().getSize(); j++) {
                    Case currentCase = placedSudoku.sudoku().getCase(i, j);

                    // Si la case du Sudoku actuel correspond à la case recherchée
                    if (currentCase == c) {

                        // Contrainte entre la case et sa ligne dans le Sudoku actuel
                        toInsert = new ArrayList<>(Arrays.asList(placedSudoku.sudoku().getLine(i)).subList(0, placedSudoku.sudoku().getSize()));
                        toInsert.remove(currentCase);
                        newConstraint = new NotEqualConstraint(currentCase, toInsert, placedSudoku.sudoku());
                        constraints.add(newConstraint);

                        // Contrainte entre la case et sa colonne dans le Sudoku actuel
                        toInsert = new ArrayList<>(Arrays.asList(placedSudoku.sudoku().getColumn(j)).subList(0, placedSudoku.sudoku().getSize()));
                        toInsert.remove(currentCase);
                        newConstraint = new NotEqualConstraint(currentCase, toInsert, placedSudoku.sudoku());
                        constraints.add(newConstraint);

                        // Contrainte entre la case et son bloc dans le Sudoku actuel
                        toInsert = new ArrayList<>(Arrays.asList(placedSudoku.sudoku().getBloc(currentCase.getBlocIndex()).cases()).subList(0, placedSudoku.sudoku().getSize()));
                        toInsert.remove(currentCase);
                        newConstraint = new NotEqualConstraint(currentCase, toInsert, placedSudoku.sudoku());
                        constraints.add(newConstraint);

                        // Contraintes supplémentaires s'appliquant sur cette case dans le Sudoku
                        for (SudokuConstraint constraint : placedSudoku.sudoku().getAddedConstraints()) {
                            if (constraint.isConstraintOnCase(currentCase)) {
                                constraints.add(constraint);
                            }
                        }

                        for (PlacedSudoku otherSudoku : this.sudokus) {
                            if (otherSudoku != placedSudoku) {
                                for (int a = 0; a < sizeSudokus; a++) {
                                    for (int b = 0; b < sizeSudokus; b++) {
                                        if (placedSudoku.line() + i == otherSudoku.line() + a && placedSudoku.column() + j == otherSudoku.column() + b) {
                                            Sudoku other = otherSudoku.sudoku();
                                            Case otherCase = other.getCase(a, b);  // Récupère la case correspondante dans l'autre Sudoku
                                            if (otherCase != null) {
                                                // Crée une contrainte d'égalité entre la case actuelle et la case correspondante
                                                ArrayList<Case> otherCaseList = new ArrayList<>();
                                                otherCaseList.add(otherCase);
                                                SudokuConstraint newConstraint2 = new EqualConstraint(currentCase, otherCaseList, this);
                                                constraints.add(newConstraint2);
                                            }
                                        }
                                    }


                                }



                            }
                        }
                    }
                }
            }
        }

        return constraints;
    }



    /**
     * Retourne la liste des cases constituant le multidoku, toujours dans le même ordre
     * @return La liste des cases constituant le multidoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<Case> casesList() throws RuntimeException {
        ArrayList<Case> cases = new ArrayList<>();
        for (PlacedSudoku placedSudoku : this.sudokus) {
            cases.addAll(placedSudoku.sudoku().casesList());
        }
        return cases;
    }

    /**
     * Crée une copie du multidoku, qui ne comporte aucune référence vers le multidoku originel
     * @return Une copie du multidoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public Puzzle copy() {
        ArrayList<PlacedSudoku> newSudokus = new ArrayList<>();
        for (PlacedSudoku placedSudoku : this.sudokus) {
            newSudokus.add(new PlacedSudoku(placedSudoku.sudoku().copy(), placedSudoku.line(), placedSudoku.column()));
        }
        return new Multidoku(newSudokus);
    }


    /**
     * Crée un string qui affiche le multidoku comme une grille, avec les valeurs des cases déjà remplies et des couleurs différentes pour chaque bloc
     * @return Un string qui permet d'afficher le sudoku
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int gridSize = this.sizeMultidokuGrid;

        // Crée une grille vide avec -2 représentant les cases vides
        int[][] grid = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            Arrays.fill(grid[i], -2);  // Initialisation avec -2 pour les cases vides
        }

        // Ce compteur servira à déterminer les indices de bloc globaux
        for (PlacedSudoku placedSudoku : this.sudokus) {
            Sudoku sudoku = placedSudoku.sudoku();
            int rowStart = placedSudoku.line();
            int colStart = placedSudoku.column();

            System.out.println("Placing Sudoku at position (" + rowStart + ", " + colStart + ")");

            // Place les valeurs dans la grille là où le Sudoku est placé
            for (int i = 0; i < this.sizeSudokus; i++) {
                for (int j = 0; j < this.sizeSudokus; j++) {
                    int value = sudoku.getCase(i, j).getValue();
                    if (grid[rowStart + i][colStart + j] != value) {
                        if (grid[rowStart + i][colStart + j] != -2 && value != -2 && value != -1 && grid[rowStart + i][colStart + j] != -1) {
                            throw new IllegalArgumentException("Les cases ne correspondent pas à (" + (rowStart + i) + ", " + (colStart + j) + ")");
                        } else {
                            if (grid[rowStart + i][colStart + j] == -2 || grid[rowStart + i][colStart + j] == -1) {
                                grid[rowStart + i][colStart + j] = value;
                                System.out.println("Placing value " + value + " at position (" + (rowStart + i) + ", " + (colStart + j) + ")");
                            }
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
                // Trouver le bloc auquel appartient la case et calculer l'index global du bloc
                for (PlacedSudoku placedSudoku : this.sudokus) {

                    if (i >= placedSudoku.line() && i < placedSudoku.line() + sizeSudokus &&
                            j >= placedSudoku.column() && j < placedSudoku.column() + sizeSudokus) {
                        Sudoku sudoku = placedSudoku.sudoku();
                        // L'index du bloc local du Sudoku + l'index global de celui-ci
                        blocIndex = sudoku.getCase(i - placedSudoku.line(), j - placedSudoku.column()).getBlocIndex() + nbr * sizeSudokus;
                        break;
                    }
                    nbr++;
                }



                // Calcul de la couleur en fonction du bloc
                int red = Math.abs((blocIndex * 67 + 123) % 256);
                int green = Math.abs((blocIndex * 151 + 231) % 256);
                int blue = Math.abs((blocIndex * 199 + 87) % 256);

                // Appliquer la couleur pour la case
                sb.append("\033[38;2;")
                        .append(red).append(";")
                        .append(green).append(";")
                        .append(blue).append("m");

                // Vérifie si les symboles sont définis
                HashMap<Integer, String> symbolsToPrint = this.getSymbols();
                String symbolToDisplay = Integer.toString(grid[i][j]);  // Valeur par défaut si pas de symbole
                if (symbolsToPrint != null && symbolsToPrint.containsKey(grid[i][j])) {
                    symbolToDisplay = symbolsToPrint.get(grid[i][j]);  // Si un symbole est défini, on l'affiche
                }

                // Affichage des cases avec leur couleur
                if (grid[i][j] == -2) {
                    sb.append("   ");  // Case vide
                } else if (grid[i][j] == -1) {
                    sb.append("[ ]");  // Case vide à l'intérieur d'un Sudoku
                } else {
                    sb.append("[").append(symbolToDisplay).append("]");  // Case avec valeur ou symbole
                }

                // Réinitialiser la couleur après chaque case
                sb.append("\033[0m");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
