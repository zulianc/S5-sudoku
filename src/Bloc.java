/**
 * Un Bloc représente un bloc de cases qui ne peuvent pas avoir la même valeur à l'intérieur d'un Sudoku, et ne doit pas exister sans faire partie d'un
 */
public class Bloc {
    /**
     * Les cases contenues dans le bloc
     */
    private final Case[] cases;
    /**
     * La taille du bloc, qui est la même que celle du Sudoku auquel il appartient
     */
    private final int size;

    /**
     * Constructeur de la classe
     * @param size La taille du bloc
     * @param cases Les cases contenues dans le bloc
     */
    public Bloc(int size, Case[] cases) {
        this.size = size;
        this.cases = cases;
    }

    /**
     * Getter des cases du bloc
     * @return Les cases du bloc
     */
    public Case[] getCases() {
        return this.cases;
    }

    /**
     * Getter de la i-ème case contenue dans le bloc
     * @param index La position de la case dans le bloc
     * @return La case dans le bloc, si elle existe
     */
    public Case getCase(int index) throws IllegalArgumentException {
        if (index >= 0 && index < this.cases.length) {
            return this.cases[index];
        }
        throw new IllegalArgumentException("Le bloc ne contient pas de case numéroté " + index);
    }

    /**
     * Getter d'une case du bloc selon sa position dans le Sudoku
     * @param ligne La ligne de la case souhaitée
     * @param colonne La colonne de la case souhaitée
     * @return La case souhaitée, si elle existe
     */
    public Case getCase(int ligne, int colonne) throws IllegalArgumentException {
        for (Case c : this.cases) {
            if (c.getLine() == ligne && c.getColumn() == colonne) {
                return c;
            }
        }
        throw new IllegalArgumentException("Le bloc ne contient pas de case étant à la ligne " + ligne + " et colonne " + colonne);
    }

    /**
     * Getter de la taille du bloc
     * @return La taille du bloc
     */
    public int getSize() {
        return this.size;
    }
}
