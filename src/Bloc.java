/**
 * Un Bloc représente un bloc de cases qui ne peuvent pas avoir la même valeur à l'intérieur d'un Sudoku, et ne doit pas exister sans faire partie d'un sudoku
 *
 * @param cases Les cases contenues dans le bloc
 */
public record Bloc(Case[] cases) {
    /**
     * Constructeur
     *
     * @param cases Les cases contenues dans le bloc
     */
    public Bloc {
    }

    /**
     * Getter des cases du bloc
     *
     * @return Les cases du bloc
     */
    @Override
    public Case[] cases() {
        return this.cases;
    }

    /**
     * Getter de la i-ème case contenue dans le bloc
     *
     * @param index La position de la case dans le bloc
     * @return La case dans le bloc, si elle existe
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case getCase(int index) throws IllegalArgumentException {
        if (index >= 0 && index < this.cases.length) {
            return this.cases[index];
        }
        throw new IllegalArgumentException("Le bloc ne contient pas de case numéroté " + (index + 1));
    }

    /**
     * Getter d'une case du bloc selon sa position dans le Sudoku
     *
     * @param line   La ligne de la case souhaitée
     * @param column La colonne de la case souhaitée
     * @return La case souhaitée, si elle existe
     * @throws IllegalArgumentException Si les arguments passés n'obtiennent pas une case valide
     */
    public Case getCase(int line, int column) throws IllegalArgumentException {
        for (Case c : this.cases) {
            if (c.getLine() == line && c.getColumn() == column) {
                return c;
            }
        }
        throw new IllegalArgumentException("Le bloc ne contient pas de case étant à la ligne " + (line + 1) + " et colonne " + (column + 1));
    }

    /**
     * Getter de la taille du bloc
     *
     * @return La taille du bloc
     */
    public int getSize() {
        return this.cases.length;
    }
}
