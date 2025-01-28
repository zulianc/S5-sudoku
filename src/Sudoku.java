import java.util.*;

/**
 * Un Sudoku représente une grille de Sudoku, c'est-à-dire une grille carrée de cases et ayant des blocs de la même taille que la grille
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
     * La taille de la grille
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
                    throw new IllegalArgumentException("Le bloc " + bloc + " n'existe pas");
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
     * @throws IllegalArgumentException Si les arguments passés ne créent pas un sudoku valide
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
        Set<Integer> set = new HashSet<>();
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
     * Getter des cases du sudoku
     * @return Les cases du sudoku
     */
    public Case[][] getCases() {
        return this.cases;
    }

    /**
     * Getter d'une ligne du sudoku
     * @param ligne La ligne du sudoku à récupérer
     * @return Une ligne du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case[] getLigne(int ligne) throws IllegalArgumentException {
        if (ligne < 0 || ligne >= this.size) {
            throw new IllegalArgumentException("La ligne " + ligne + " n'existe pas");
        }
        return this.cases[ligne];
    }

    /**
     * Getter d'une colonne du sudoku
     * @param colonne La colonne du sudoku à récupérer
     * @return Une colonne du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case[] getColonne(int colonne) throws IllegalArgumentException {
        if (colonne < 0 || colonne >= this.size) {
            throw new IllegalArgumentException("La colonne " + colonne + " n'existe pas");
        }
        Case[] col = new Case[this.size];
        for (int i = 0; i < this.size; i++) {
            col[i] = this.cases[i][colonne];
        }
        return col;
    }

    /**
     * Getter d'une case du sudoku
     * @param ligne La ligne de la case
     * @param colonne La colonne de la case
     * @return Une case du sudoku
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case getCase(int ligne, int colonne) throws IllegalArgumentException {
        if (ligne < 0 || ligne >= this.size || colonne < 0 || colonne >= this.size) {
            throw new IllegalArgumentException("Le sudoku ne contient pas de case était à la ligne " + ligne + " et colonne " + colonne);
        }
        return this.cases[ligne][colonne];
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
            throw new IllegalArgumentException("Le bloc " + bloc + " n'existe pas");
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
     * Retourne les contraintes internes entre les cases du sudoku
     * @return Une liste de contraintes entre cases
     */
    @Override
    public ArrayList<SudokuConstraint> defaultConstraints() {
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        NotEqualConstraint newConstraint;
        ArrayList<Case> toInsert;
        for(int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // contrainte entre la case j de la ligne i et la ligne i
                toInsert = new ArrayList<>(Arrays.asList(this.getLigne(i)).subList(0, this.size));
                toInsert.remove(this.getLigne(i)[j]);
                newConstraint = new NotEqualConstraint(this.getLigne(i)[j], toInsert, this);
                constraints.add(newConstraint);

                // contrainte entre la case j de la colonne i et la colonne i
                toInsert = new ArrayList<>(Arrays.asList(this.getColonne(i)).subList(0, this.size));
                toInsert.remove(this.getColonne(i)[j]);
                newConstraint = new NotEqualConstraint(this.getColonne(i)[j], toInsert, this);
                constraints.add(newConstraint);

                // contrainte entre la case j du bloc i et le bloc i
                toInsert = new ArrayList<>(Arrays.asList(this.getBloc(i).cases()).subList(0, this.size));
                toInsert.remove(this.getBloc(i).getCase(j));
                newConstraint = new NotEqualConstraint(this.getBloc(i).getCase(j), toInsert, this);
                constraints.add(newConstraint);
            }
        }
        return constraints;
    }

    /**
     * Retourne les contraintes internes appliquées sur une case du sudoku
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     */
    @Override
    public ArrayList<SudokuConstraint> constraintsOnCase(Case c) {
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        NotEqualConstraint newConstraint;
        ArrayList<Case> toInsert;

        // contrainte entre la case et sa ligne
        toInsert = new ArrayList<>(Arrays.asList(this.getLigne(c.getLine())).subList(0, this.size));
        toInsert.remove(c);
        newConstraint = new NotEqualConstraint(c, toInsert, this);
        constraints.add(newConstraint);

        // contrainte entre la case et la colonne j
        toInsert = new ArrayList<>(Arrays.asList(this.getColonne(c.getColumn())).subList(0, this.size));
        toInsert.remove(c);
        newConstraint = new NotEqualConstraint(c, toInsert, this);
        constraints.add(newConstraint);

        // contrainte entre la case et le bloc k
        toInsert = new ArrayList<>(Arrays.asList(this.getBloc(c.getBlocIndex()).cases()).subList(0, this.size));
        toInsert.remove(c);
        newConstraint = new NotEqualConstraint(c, toInsert, this);
        constraints.add(newConstraint);

        return constraints;
    }

    /**
     * Retourne la liste des cases constituant le sudoku, toujours dans le même ordre
     * @return La liste des cases constituant le sudoku
     */
    @Override
    public ArrayList<Case> casesList() {
        ArrayList<Case> cases = new ArrayList<>();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                cases.add(this.getCase(i, j));
            }
        }
        return cases;
    }

    /**
     * Crée une copie du sudoku, qui ne comporte aucune référence vers le sudoku originel
     * @return Une copie du sudoku
     */
    @Override
    public Sudoku copy() {
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

        return newSudoku;
    }

    /**
     * Crée un string qui affiche le sudoku comme une grille, avec les valeurs des cases déjà remplies et des couleurs différentes pour chaque bloc
     * @return Un string qui permet d'afficher le sudoku
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int colorDivision = (255 * 3) / this.size;

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
                if (this.cases[i][j].getValue() == -1) {
                    sb.append(" ");
                }
                else {
                    int num = this.cases[i][j].getValue();
                    if (this.symbols != null) {
                        sb.append(this.symbols.get(num));
                    }
                    else {
                        sb.append(num + 1);
                    }
                }
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
