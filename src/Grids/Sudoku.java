package Grids;

import Constraints.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Un Sudoku représente une grille de sudoku, c'est-à-dire une grille carrée de cases et ayant des blocs de la même taille que la grille
 */
public class Sudoku implements Puzzle {
    /**
     * Les cases qui composent la grille, qui doit être carrée
     */
    private final Case[][] cases;
    /**
     * Les blocs qui composent la grille, les blocs doivent être de même taille que la grille, mais pas forcément rectangulaires
     */
    private final Bloc[] blocs;
    /**
     * La taille de la grille, c'est-à-dire le nombre de cases dans une ligne, une colonne, ou un bloc
     */
    private final int size;
    /**
     * Les symboles utilisés pour remplacer les nombres lors de l'affichage de la grille, s'ils sont définis
     */
    private HashMap<Integer, String> symbols;
    /**
     * Indique si les blocs utilisent la forme par défaut ou non, si c'est le cas ils formeront un rectangle le plus carré possible, avec une hauteur plus grand ou égale à la largeur
     */
    private final Boolean useDefaultPlacements;
    /**
     * Une liste de contraintes spécifiées sur le sudoku en plus des contraintes de base (lignes, colonnes, blocs)
     */
    private ArrayList<SudokuConstraint> addedConstraints;

    /**
     * Constructeur de la classe, sans symboles
     * @param size La taille du sudoku
     * @param placements Les blocs auxquels appartiennent les cases, s'il est null alors les blocs auront les formes par défaut
     * @throws IllegalArgumentException Si les arguments passés ne créent pas un sudoku valide
     */
    public Sudoku(int size, int[][] placements) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("Le sudoku doit avoir une taille supérieur à 0");
        }

        this.size = size;
        this.cases = new Case[size][size];
        this.blocs = new Bloc[size];
        this.symbols = null;
        this.useDefaultPlacements = (placements == null);
        this.addedConstraints = new ArrayList<>();

        if (this.useDefaultPlacements) {
            placements = new int[size][size];
            // on cherche le rapport entier le plus proche d'un carré
            int width = (int) Math.sqrt(size);
            while (size % width != 0 && width > 1) {
                width--;
            }
            int height = size / width;
            // on attribue les blocs par défaut
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    placements[i][j] = ((i/height) * height) + (j/width);
                }
            }
        }

        // création des cases de la grille
        Case[][] blocs = new Case[size][size]; // la liste des blocs de la grille
        int[] placedInBloc = new int[size]; // le nombre de cases dans chaque bloc
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int bloc = placements[i][j];
                if (bloc < 0 || bloc >= size) {
                    throw new IllegalArgumentException("Le bloc " + (bloc + 1) + " n'existe pas");
                }

                // on doit créer un nouveau set à chaque fois sinon modifier celui d'une case modifiera celui de toutes les cases
                HashSet<Integer> possibleValues = new HashSet<>();
                for (int k = 0; k < size; k++) {
                    possibleValues.add(k);
                }

                // on doit faire attention à ce que les blocs et la grille référencent bien les mêmes cases
                this.cases[i][j] = new Case(possibleValues, i, j, bloc);
                blocs[bloc][placedInBloc[bloc]] = this.cases[i][j];

                placedInBloc[bloc]++;
                if (placedInBloc[bloc] > size) {
                    throw new IllegalArgumentException("Il y a trop de cases dans le bloc " + bloc);
                }
            }
        }

        // on attribue les blocs
        for (int i = 0; i < size; i++) {
            this.blocs[i] = new Bloc(blocs[i]);
        }
    }

    /**
     * Constructeur de la classe, avec symboles
     * @param size La taille du sudoku
     * @param placements Les blocs auxquels appartiennent les cases, s'il est null alors les blocs auront les formes par défaut
     * @param symbols Les symboles utilisés lors de l'affichage de la grille
     * @throws IllegalArgumentException Si les arguments passés ne créent pas un sudoku valide, ou si les symboles ne sont pas correctes
     */
    public Sudoku(int size, int[][] placements, HashMap<Integer, String> symbols) throws IllegalArgumentException {
        this(size, placements);
        if (symbols != null) {
            this.setSymbols(symbols);
        }
    }

    /**
     * Indique au sudoku quels symboles utiliser, qui doivent être numérotés de 0 à (taille du sudoku - 1)
     * @param symbols Les nouveaux symboles à utiliser
     * @throws IllegalArgumentException Si les arguments passés ne créent pas une liste de symboles valide
     */
    public void setSymbols(HashMap<Integer, String> symbols) throws IllegalArgumentException{
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < this.size; i++) {
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
     * Ajoute au sudoku des nouvelles contraintes
     * @param constraints Les contraintes à ajouter
     * @throws IllegalArgumentException Si les contraintes ne s'appliquent pas sur ce sudoku
     */
    public void setAddedConstraints(ArrayList<SudokuConstraint> constraints) throws IllegalArgumentException {
        for (SudokuConstraint constraint : constraints) {
            if (!constraint.isConstraintOnPuzzle(this)) {
                throw new IllegalArgumentException("La contrainte ne s'applique pas sur ce sudoku !");
            }
        }
        this.addedConstraints = constraints;
    }

    /**
     * Getter des cases du sudoku
     * @return Les cases du sudoku
     */
    public Case[][] getCases() {
        return this.cases;
    }

    /**
     * Getter d'une ligne du sudoku
     * @param line La ligne du sudoku à récupérer
     * @return Une ligne du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case[] getLine(int line) throws IllegalArgumentException {
        if (line < 0 || line >= this.size) {
            throw new IllegalArgumentException("La ligne " + (line + 1) + " n'existe pas");
        }
        return this.cases[line];
    }

    /**
     * Getter d'une colonne du sudoku
     * @param column La colonne du sudoku à récupérer
     * @return Une colonne du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case[] getColumn(int column) throws IllegalArgumentException {
        if (column < 0 || column >= this.size) {
            throw new IllegalArgumentException("La colonne " + (column + 1) + " n'existe pas");
        }
        Case[] col = new Case[this.size];
        for (int i = 0; i < this.size; i++) {
            col[i] = this.cases[i][column];
        }
        return col;
    }

    /**
     * Getter d'une case du sudoku
     * @param line La ligne de la case
     * @param column La colonne de la case
     * @return Une case du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case getCase(int line, int column) throws IllegalArgumentException {
        if (line < 0 || line >= this.size || column < 0 || column >= this.size) {
            throw new IllegalArgumentException("Le sudoku ne contient pas de case était à la ligne " + (line + 1) + " et colonne " + (column + 1));
        }
        return this.cases[line][column];
    }

    /**
     * Getter des blocs du sudoku
     * @return Les blocs du sudoku
     */
    public Bloc[] getBlocs() {
        return this.blocs;
    }

    /**
     * Getter d'un bloc du sudoku
     * @param bloc Le numéro du bloc
     * @return Un bloc du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas un bloc valide
     */
    public Bloc getBloc(int bloc) {
        if (bloc < 0 || bloc >= this.size) {
            throw new IllegalArgumentException("Le bloc " + (bloc + 1) + " n'existe pas");
        }
        return this.blocs[bloc];
    }

    /**
     * Getter de la taille du sudoku
     * @return La taille du sudoku
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Getter des symboles utilisés lors de l'affichage du sudoku
     * @return Les symboles utilisés lors de l'affichage du sudoku
     */
    public HashMap<Integer, String> getSymbols() {
        return this.symbols;
    }

    /**
     * Indique si le sudoku utilise les placements de blocs par défaut
     * @return Si le sudoku utilise les placements de blocs par défaut
     */
    public boolean isUsingDefaultPlacements() {
        return this.useDefaultPlacements;
    }

    /**
     * Getter des contraintes additionnelles du sudoku
     * @return Les contraintes additionnelles du sudoku
     */
    public ArrayList<SudokuConstraint> getAddedConstraints() {
        return this.addedConstraints;
    }

    /**
     * Retourne les contraintes internes entre les cases du sudoku
     * @return Une liste de contraintes entre cases
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints() throws RuntimeException {
        try {
            ArrayList<SudokuConstraint> constraints = new ArrayList<>();
            NotEqualConstraint newConstraint;
            ArrayList<Case> toInsert;
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    // contrainte entre la case j de la ligne i et la ligne i
                    toInsert = new ArrayList<>(Arrays.asList(this.getLine(i)).subList(0, this.size));
                    toInsert.remove(this.getLine(i)[j]);
                    newConstraint = new NotEqualConstraint(this.getLine(i)[j], toInsert, this);
                    constraints.add(newConstraint);

                    // contrainte entre la case j de la colonne i et la colonne i
                    toInsert = new ArrayList<>(Arrays.asList(this.getColumn(i)).subList(0, this.size));
                    toInsert.remove(this.getColumn(i)[j]);
                    newConstraint = new NotEqualConstraint(this.getColumn(i)[j], toInsert, this);
                    constraints.add(newConstraint);

                    // contrainte entre la case j du bloc i et le bloc i
                    toInsert = new ArrayList<>(Arrays.asList(this.getBloc(i).cases()).subList(0, this.size));
                    toInsert.remove(this.getBloc(i).getCase(j));
                    newConstraint = new NotEqualConstraint(this.getBloc(i).getCase(j), toInsert, this);
                    constraints.add(newConstraint);
                }
            }
            // contraintes supplémentaires
            constraints.addAll(this.addedConstraints);
            return constraints;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Retourne les contraintes internes appliquées sur une case du sudoku
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<SudokuConstraint> constraintsOnCase(Case c) throws RuntimeException {
        try {
            ArrayList<SudokuConstraint> constraints = new ArrayList<>();
            NotEqualConstraint newConstraint;
            ArrayList<Case> toInsert;

            // contrainte entre la case et sa ligne
            toInsert = new ArrayList<>(Arrays.asList(this.getLine(c.getLine())).subList(0, this.size));
            toInsert.remove(c);
            newConstraint = new NotEqualConstraint(c, toInsert, this);
            constraints.add(newConstraint);

            // contrainte entre la case et la colonne j
            toInsert = new ArrayList<>(Arrays.asList(this.getColumn(c.getColumn())).subList(0, this.size));
            toInsert.remove(c);
            newConstraint = new NotEqualConstraint(c, toInsert, this);
            constraints.add(newConstraint);

            // contrainte entre la case et le bloc k
            toInsert = new ArrayList<>(Arrays.asList(this.getBloc(c.getBlocIndex()).cases()).subList(0, this.size));
            toInsert.remove(c);
            newConstraint = new NotEqualConstraint(c, toInsert, this);
            constraints.add(newConstraint);

            // contraintes supplémentaires s'appliquant sur cette case
            for (SudokuConstraint constraint : this.addedConstraints) {
                if (constraint.isConstraintOnCase(c)) {
                    constraints.add(constraint);
                }
            }

            return constraints;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Retourne la liste des cases constituant le sudoku, toujours dans le même ordre
     * @return La liste des cases constituant le sudoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public ArrayList<Case> casesList() throws RuntimeException {
        try {
            ArrayList<Case> cases = new ArrayList<>();
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    cases.add(this.getCase(i, j));
                }
            }
            return cases;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Crée une copie du sudoku, qui ne comporte aucune référence vers le sudoku originel
     * @return Une copie du sudoku
     * @throws RuntimeException Si une erreur interne arrive
     */
    @Override
    public Sudoku copy() throws RuntimeException {
        try {
            // on copie les placements
            int[][] placements = new int[this.size][this.size];
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    placements[i][j] = this.cases[i][j].getBlocIndex();
                }
            }

            // on copie les symboles
            HashMap<Integer, String> symbols = null;
            if (this.symbols != null) {
                symbols = new HashMap<>(this.symbols);
            }

            Sudoku newSudoku = new Sudoku(this.size, placements, symbols);

            // on copie les valeurs des cases
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (this.cases[i][j].getValue() != -1) {
                        newSudoku.getCase(i, j).setValue(this.cases[i][j].getValue());
                    }
                }
            }

            // on copie les contraintes additionnelles
            ArrayList<SudokuConstraint> newConstraints = new ArrayList<>();
            for (SudokuConstraint constraint : this.addedConstraints) {
                newConstraints.add(constraint.copy(newSudoku));
            }
            newSudoku.setAddedConstraints(newConstraints);

            return newSudoku;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur interne : " + e);
        }
    }

    /**
     * Crée un string qui affiche le sudoku comme une grille, avec les valeurs des cases déjà remplies et des couleurs différentes pour chaque bloc
     * @return Un string qui permet d'afficher le sudoku
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final int colorDivision = (255 * 3) / this.size;

        // on change les symboles utilisés pour qu'ils soient tous de la même taille
        HashMap<Integer, String> symbolsToPrint = new HashMap<>();
        if (this.symbols != null) {
            symbolsToPrint.putAll(this.symbols);
        }
        else {
            for (int i = 0; i < this.size; i++) {
                symbolsToPrint.put(i, Integer.toString(i + 1));
            }
        }
        symbolsToPrint.put(-1, " ");
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

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // calcul de la couleur de la case en fonction du bloc
                int bloc = this.cases[i][j].getBlocIndex();
                int colorTotal = colorDivision * bloc;
                int red, green, blue;
                if (colorTotal <= 255) {
                    red = 255 - colorTotal;
                    green = colorTotal;
                    blue = 0;
                } else if (colorTotal <= (255 * 2)) {
                    colorTotal = colorTotal - 255;

                    red = 0;
                    green = 255 - colorTotal;
                    blue = colorTotal;
                } else {
                    colorTotal = colorTotal - (255 * 2);

                    red = colorTotal;
                    green = 0;
                    blue = 255 - colorTotal;
                }
                sb.append("\033[38;2;").append(red).append(";").append(green).append(";").append(blue).append("m");

                // insère la case
                sb.append("[");
                sb.append(symbolsToPrint.get(this.cases[i][j].getValue()));
                sb.append("]");
            }
            if (!(i == this.size - 1))
                sb.append("\n");
        }
        // remet la couleur normale
        sb.append("\033[38;2;255;255;255m");
        return sb.toString();
    }
}
