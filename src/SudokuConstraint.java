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

    /**
     * Une implémentation de cette méthode doit indiquer si elle applique des contraintes sur la même case que celle passée en paramètre
     * @param c La case sur laquelle on veut savoir si la contrainte s'applique
     * @return Si la contrainte s'applique sur la case
     */
    boolean isConstraintOnCase(Case c);

    /**
     * Une implémentation de cette méthode doit indiquer si elle applique des contraintes sur le puzzle passé en paramètre
     * @param puzzle Lz puzzle sur lequel on veut savoir si la contrainte s'applique
     * @return Si la contrainte s'applique sur le puzzle
     */
    boolean isConstraintOnPuzzle(Puzzle puzzle);

    /**
     * Une implémentation de cette méthode doit retourner un String contenant un symbole identifiant le type de contrainte et des nombres identifiants les cases, tout sur une ligne et séparé par un espace
     * @return Un string représentant la contrainte
     */
    @Override
    String toString();
}
