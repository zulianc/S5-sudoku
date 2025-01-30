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
            if (constraint.copy(this) != constraint) {
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
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints() {
        //TODO
        return null;
    }

    /**
     * Retourne les contraintes internes appliquées sur une case du multidoku
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     */
    @Override
    public ArrayList<SudokuConstraint> constraintsOnCase(Case c) {
        //TODO
        return null;
    }

    /**
     * Retourne la liste des cases constituant le multidoku, toujours dans le même ordre
     * @return La liste des cases constituant le multidoku
     */
    @Override
    public ArrayList<Case> casesList() {
        //TODO
        return null;
    }

    /**
     * Crée une copie du multidoku, qui ne comporte aucune référence vers le multidoku originel
     * @return Une copie du multidoku
     */
    @Override
    public Puzzle copy() {
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
