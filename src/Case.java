import java.util.HashSet;

/**
 * Une Case représente une case à l'intérieur d'un Sudoku, et ne doit pas exister sans faire partie d'un sudoku
 */
public class Case {
    /**
     * La valeur que contient la case, la valeur -1 indique que la case est vide
     */
    private int value;
    /**
     * Une valeur temporaire attribuée à la case, un peu comme quand on écrit au crayon à papier
     */
    private int testValue;
    /**
     * Indique si la valeur de la case doit être cachée du monde extérieur, la case apparaitra alors toujours vide
     */
    private boolean valueIsHidden;
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
            throw new IllegalArgumentException("La case ne peut pas avoir la valeur " + (value + 1));
        }
    }

    /**
     * Rend la valeur actuelle de la case invisible du monde extérieur
     */
    public void hideValue() {
        this.valueIsHidden = true;
    }

    /**
     * Rend la valeur actuelle de la case visible du monde extérieur à nouveau
     */
    public void showValue() {
        this.valueIsHidden = false;
    }

    /**
     * Attribue à la case une valeur temporaire qui doit faire partie de ses valeurs possibles, comme si on l'écrivait au crayon à papier
     * @param testValue La valeur temporaire à attribuer
     * @throws IllegalArgumentException Si la valeur ne peut pas être attribuée à la case
     */
    public void tryTestValue(int testValue) throws IllegalArgumentException {
        if (this.possibleValues.contains(testValue)) {
            this.testValue = testValue;
        }
        else {
            throw new IllegalArgumentException("La case ne peut pas avoir la valeur " + (testValue + 1));
        }
    }

    /**
     * Enlève toute valeur temporaire présente sur cette case, comme si on la gommait
     */
    public void scrapTestValue() {
        this.testValue = -1;
    }

    /**
     * Confirme la valeur temporaire écrite sur la case, comme si on la repassait au stylo
     * @throws RuntimeException Si la case n'avait pas de valeur temporaire
     */
    public void confirmTestValue() throws RuntimeException {
        if (this.testValue == -1) {
            throw new RuntimeException("La case n'avait pas de valeur de test");
        }
        this.setValue(this.testValue);
        this.testValue = -1;
    }

    /**
     * Indique à la case qu'elle ne doit pas prendre cette valeur, ce qui ne fait rien si cette valeur était déjà impossible
     * @param valeur La valeur à essayer d'enlever
     * @return Si le fait d'enlever cette valeur a résolu la case
     */
    public boolean removePossibleValue(int valeur) {
        this.possibleValues.remove(valeur);
        if (this.possibleValues.size() == 1 && this.value == -1) {
            this.value = possibleValues.iterator().next();
            return true;
        }
        return false;
    }

    /**
     * Indique si la case respecte toujours ses contraintes internes
     * @return true si elle a au moins une valeur possible, false sinon
     */
    public boolean isValid() {
        return !this.possibleValues.isEmpty();
    }

    /**
     * Indique si la case contient déjà une valeur ou pas
     * @return Si la case contient déjà une valeur
     */
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
     * Getter de la valeur de la case, temporaire ou non
     * @return La valeur de la case
     */
    public int getValue() {
        if (this.valueIsHidden) {
            return -1;
        }
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
