/**
 * Les classes qui implémentent cette interface représentent une contrainte appliquée sur une case
 */
public interface SudokuConstraint {
    /**
     * Une implémentation de cette méthode doit mettre à jour les valeurs possibles d'une case en fonction des contraintes appliquées dessus
     * @return Un booléen qui indique si la case est devenue irrésolvable
     */
    boolean setNewPossibleValues();

    /**
     * Une implémentation de cette méthode doit indiquer si la case respecte toujours les contraintes
     * @return Si la case respecte toujours les contraintes
     */
    boolean isConstraintValid();

    /**
     * Une implémentation de cette méthode doit indiquer si la case sur laquelle sont appliquées les contraintes a été résolue
     * @return Si la case a été résolue
     */
    boolean hasBeenResolved();

    /**
     * Une implémentation de cette méthode doit créer une contrainte identique, mais ayant lieu sur le puzzle passé en paramètre
     * @param newPuzzle Le puzzle sur lequel la nouvelle contrainte doit s'appliquer
     * @return Une copie de cette contrainte qui référence le nouveau puzzle
     */
    SudokuConstraint copy(Puzzle newPuzzle);
}
