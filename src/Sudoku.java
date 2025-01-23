import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {
    private final Case[][] cases;
    private final Bloc[] blocs;
    private final int taille;
    private HashMap<Integer, String> symboles;

    public Sudoku(int taille, int[][] placements) {
        if (taille <= 0 || taille > 9) {
            throw new IllegalArgumentException("Size must be between 1 and 9");
        }
        this.taille = taille;

        this.cases = new Case[taille][taille];
        this.blocs = new Bloc[taille];
        this.symboles = null;

        Case[][] blocs = new Case[taille][taille];
        int[] placedInBlocks = new int[taille];

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int block = placements[i][j];
                if (block < 0 || block >= taille) {
                    throw new IllegalArgumentException("Illegal placement");
                }

                this.cases[i][j] = new Case(i, j, block);
                blocs[block][placedInBlocks[block]] = this.cases[i][j];

                placedInBlocks[block]++;
            }
        }

        for (int i = 0; i < taille; i++) {
            this.blocs[i] = new Bloc(taille, blocs[i]);
        }
    }

    public Sudoku(int taille, int[][] placements, HashMap<Integer, String> symboles) {
        this(taille, placements);
        if (symboles != null) {
            this.setSymboles(symboles);
        }
    }

    public Sudoku copy() {
        int[][] placements = new int[this.taille][this.taille];
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                placements[i][j] = this.cases[i][j].getBlocIndex();
            }
        }

        HashMap<Integer, String> symboles = null;
        if (this.symboles != null) {
            symboles = new HashMap<>(this.symboles);
        }

        Sudoku newSudoku = new Sudoku(this.taille, placements, symboles);

        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                newSudoku.getCase(i, j).setValeur(this.cases[i][j].getValeur());
            }
        }

        return newSudoku;
    }

    public void setSymboles(HashMap<Integer, String> symboles) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < this.taille; i++) {
            set.add(i);
        }

        if(symboles.keySet().equals(set)) {
            this.symboles = symboles;
        }
        else {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    public Case[][] getCases() {
        return this.cases;
    }

    public Case[] getLigne(int ligne) {
        if (ligne < 0 || ligne >= this.taille) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.cases[ligne];
    }

    public Case[] getColonne(int colonne) {
        if (colonne < 0 || colonne >= this.taille) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        Case[] col = new Case[this.taille];
        for (int i = 0; i < this.taille; i++) {
            col[i] = this.cases[i][colonne];
        }
        return col;
    }

    public Case getCase(int ligne, int colonne) {
        if (ligne < 0 || ligne >= this.taille || colonne < 0 || colonne >= this.taille) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.cases[ligne][colonne];
    }

    public Bloc[] getBlocs() {
        return this.blocs;
    }

    public Bloc getBloc(int block) {
        if (block < 0 || block >= this.taille) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.blocs[block];
    }

    public int getTaille() {
        return this.taille;
    }

    public HashMap<Integer, String> getSymboles() {
        return this.symboles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int colorDivision = (255 * 3) / this.taille;

        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
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

                sb.append("[");
                if (this.cases[i][j].getValeur() == -1) {
                    sb.append(" ");
                }
                else {
                    int num = this.cases[i][j].getValeur();
                    if (this.symboles != null) {
                        sb.append(this.symboles.get(num));
                    }
                    else {
                        sb.append(num);
                    }
                }
                sb.append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
