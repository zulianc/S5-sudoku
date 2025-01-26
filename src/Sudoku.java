import java.util.*;

public class Sudoku {
    private final Case[][] cases;
    private final Bloc[] blocs;
    private final int size;
    private HashMap<Integer, String> symbols;
    private final Boolean useDefaultPlacements;

    public Sudoku(int size, int[][] placements) {
        if (size <= 0 || size > 9) {
            throw new IllegalArgumentException("Size must be between 1 and 9");
        }
        this.size = size;

        this.useDefaultPlacements = (placements == null);
        if (placements == null) {
            placements = new int[size][size];
            int width = (int) Math.sqrt(size);
            while (size % width != 0 && width > 1) {
                width--;
            }
            int height = size / width;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    placements[i][j] = ((i/height) * height) + (j/width);
                }
            }
        }

        this.cases = new Case[size][size];
        this.blocs = new Bloc[size];
        this.symbols = null;

        Case[][] blocs = new Case[size][size];
        int[] placedInBlocks = new int[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int block = placements[i][j];
                if (block < 0 || block >= size) {
                    throw new IllegalArgumentException("Illegal placement");
                }

                HashSet<Integer> possibleValues = new HashSet<>();
                for (int k = 0; k < size; k++) {
                    possibleValues.add(k);
                }
                this.cases[i][j] = new Case(i, j, block, possibleValues);
                blocs[block][placedInBlocks[block]] = this.cases[i][j];

                placedInBlocks[block]++;
            }
        }

        for (int i = 0; i < size; i++) {
            this.blocs[i] = new Bloc(size, blocs[i]);
        }
    }

    public Sudoku(int size, int[][] placements, HashMap<Integer, String> symbols) {
        this(size, placements);
        if (symbols != null) {
            this.setSymbols(symbols);
        }
    }

    public Sudoku copy() {
        int[][] placements = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                placements[i][j] = this.cases[i][j].getBlocIndex();
            }
        }

        HashMap<Integer, String> symboles = null;
        if (this.symbols != null) {
            symboles = new HashMap<>(this.symbols);
        }

        Sudoku newSudoku = new Sudoku(this.size, placements, symboles);

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                newSudoku.getCase(i, j).setValeur(this.cases[i][j].getValeur());
            }
        }

        return newSudoku;
    }

    public ArrayList<SudokuConstraints> defaultConstraints() {
        ArrayList<SudokuConstraints> constraints = new ArrayList<>();
        NotEqualConstraint newConstraint;
        ArrayList<Case> toInsert;
        for(int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // line i
                toInsert = new ArrayList<>(Arrays.asList(this.getLigne(i)).subList(0, this.size));
                toInsert.remove(this.getLigne(i)[j]);
                newConstraint = new NotEqualConstraint(this.getLigne(i)[j], toInsert);
                constraints.add(newConstraint);

                // column i
                toInsert = new ArrayList<>(Arrays.asList(this.getColonne(i)).subList(0, this.size));
                toInsert.remove(this.getColonne(i)[j]);
                newConstraint = new NotEqualConstraint(this.getColonne(i)[j], toInsert);
                constraints.add(newConstraint);

                // block i
                toInsert = new ArrayList<>(Arrays.asList(this.getBloc(i).getCases()).subList(0, this.size));
                toInsert.remove(this.getBloc(i).getCase(j));
                newConstraint = new NotEqualConstraint(this.getBloc(i).getCase(j), toInsert);
                constraints.add(newConstraint);
            }
        }
        return constraints;
    }

    public void setSymbols(HashMap<Integer, String> symbols) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < this.size; i++) {
            set.add(i);
        }

        if(symbols.keySet().equals(set)) {
            this.symbols = symbols;
        }
        else {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    public Case[][] getCases() {
        return this.cases;
    }

    public Case[] getLigne(int ligne) {
        if (ligne < 0 || ligne >= this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.cases[ligne];
    }

    public Case[] getColonne(int colonne) {
        if (colonne < 0 || colonne >= this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        Case[] col = new Case[this.size];
        for (int i = 0; i < this.size; i++) {
            col[i] = this.cases[i][colonne];
        }
        return col;
    }

    public Case getCase(int ligne, int colonne) {
        if (ligne < 0 || ligne >= this.size || colonne < 0 || colonne >= this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.cases[ligne][colonne];
    }

    public Bloc[] getBlocs() {
        return this.blocs;
    }

    public Bloc getBloc(int block) {
        if (block < 0 || block >= this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.blocs[block];
    }

    public int getSize() {
        return this.size;
    }

    public HashMap<Integer, String> getSymbols() {
        return this.symbols;
    }

    public boolean isUsingDefaultPlacements() {
        return this.useDefaultPlacements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int colorDivision = (255 * 3) / this.size;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
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
        sb.append("\033[38;2;255;255;255m");
        return sb.toString();
    }
}
