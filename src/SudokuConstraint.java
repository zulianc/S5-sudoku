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
     * Une implémentation de cette méthode doit indiquer si la case sur laquelle sont appliquées les contraintes a été résolue
     * @return Si la case a été résolue
     */
    boolean isValidated();
}
