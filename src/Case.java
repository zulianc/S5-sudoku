import java.util.HashSet;

/**
 * Une Case représente une case à l'intérieur d'un Sudoku, et ne doit pas exister sans faire partie d'un sudoku
 */
public class Case {
    /**
     * La valeur que contient la case, entre 0 et (taille du sudoku - 1), ou -1 pour indiquer que la case est vide
     */
    private int value;
    private int testValue;
    /**
     * La ligne sur laquelle est placée cette case dans le sudoku
     */
    private final int line;
    /**
     * La colonne sur laquelle est placée cette case dans le sudoku
     */
    private final int column;
    /**
     * Le bloc sur lequel est placée cette case dans le sudoku
     */
    private final int blocIndex;
    /**
     * L'ensemble des valeurs que peut prendre la case, si la case à une valeur il ne contient que cette valeur, s'il est vide le sudoku n'est pas résolvable
     */
    private HashSet<Integer> possibleValues;

    /**
     * Constructeur de la classe quand on ne sait pas la valeur exacte de la case
     * @param possibleValues L'ensemble des valeurs que peut prendre la case
     * @param line La ligne de cette case dans le sudoku
     * @param column La colonne de cette case dans le sudoku
     * @param blocIndex Le bloc de cette case dans le sudoku
     * @throws IllegalArgumentException Si la case n'a pas au moins une valeur possible
     */
    public Case(HashSet<Integer> possibleValues, int line, int column, int blocIndex) throws IllegalArgumentException {
        this.value = -1;
        this.testValue = -1;
        this.line = line;
        this.column = column;
        this.blocIndex = blocIndex;
        if (possibleValues == null || possibleValues.isEmpty()) {
            throw new IllegalArgumentException("La case doit avoir au moins une valeur possible");
        }
        if (possibleValues.size() == 1) {
            this.value = possibleValues.iterator().next();
        }
        this.possibleValues = possibleValues;
    }

    /**
     * Constructeur de la classe quand on sait la valeur exacte de la case
     * @param value La valeur que contient la case
     * @param line La ligne de cette case dans le sudoku
     * @param column La colonne de cette case dans le sudoku
     * @param blocIndex Le bloc de cette case dans le sudoku
     */
    public Case(int value, int line, int column, int blocIndex) {
        this.value = value;
        this.testValue = -1;
        this.line = line;
        this.column = column;
        this.blocIndex = blocIndex;
        HashSet<Integer> possibleValues = new HashSet<>();
        possibleValues.add(value);
        this.possibleValues = possibleValues;
    }

    /**
     * Donne une valeur à cette case, mais seulement si cette valeur fait partie de ses valeurs possibles
     * @param value La valeur à essayer d'attribuer à cette case
     * @throws IllegalArgumentException Si la valeur ne peut pas être attribuée à la case
     */
    public void setValue(int value) throws IllegalArgumentException {
        if (this.possibleValues.contains(value)) {
            this.value = value;
            this.possibleValues = new HashSet<>();
            this.possibleValues.add(value);
        }
        else {
            throw new IllegalArgumentException("La case ne peut pas avoir la valeur " + value);
        }
    }

    public void tryValue(int value) {
        this.testValue = value;
    }

    /**
     * Indique à la case qu'elle ne doit pas prendre cette valeur, ce qui ne fait rien si cette valeur était déjà impossible
     * @param valeur La valeur à essayer d'enlever
     */
    public void removePossibleValue(int valeur) {
        this.possibleValues.remove(valeur);
        if (this.possibleValues.size() == 1) {
            this.value = possibleValues.iterator().next();
        }
    }

    /**
     * Indique si la case respecte toujours ses contraintes
     * @return true si elle a au moins une valeur possible, false sinon
     */
    public boolean isValid() {
        return !this.possibleValues.isEmpty();
    }

    public boolean hasValue() {
        return this.value != -1;
    }

    /**
     * Retourne les valeurs que peut prendre la case
     * @return Les valeurs que peut prendre la case
     */
    public HashSet<Integer> possibleValues() {
        return new HashSet<>(this.possibleValues);
    }

    /**
     * Getter de la valeur de la case
     * @return La valeur de la case
     */
    public int getValue() {
        if (this.value == -1) {
            return this.testValue;
        }
        return this.value;
    }

    /**
     * Getter de la ligne de la case
     * @return La ligne de la case
     */
    public int getLine() {
        return this.line;
    }

    /**
     * Getter de la colonne de la case
     * @return La colonne de la case
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Getter du bloc de la case
     * @return Le bloc de la case
     */
    public int getBlocIndex() {
        return this.blocIndex;
    }

    /**
     * Convertit les informations de la case en un string
     * @return Le string en question
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Valeur : ").append(this.value).append("\n");
        sb.append("Position : ").append(this.line).append(" ").append(this.column).append(" ").append(this.blocIndex).append("\n");
        sb.append("Possible Values : ");
        for (Integer i : this.possibleValues) {
            sb.append(i).append(" ");
        }
        return sb.toString();
    }
}
