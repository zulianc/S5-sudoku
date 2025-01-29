import java.util.ArrayList;

/**
 * Les classes qui implémentent cette interface doivent posséder un ensemble de cases et des contraintes internes sur ces cases
 */
public interface Puzzle {
    /**
     * Une implémentation de cette méthode doit retourner les contraintes internes du puzzle
     * @return Les contraintes internes du puzzle
     */
    ArrayList<SudokuConstraint> defaultConstraints();

    /**
     * Une implémentation de cette méthode doit retourner les contraintes internes du puzzle qui s'appliquent sur une case donnée
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     */
    ArrayList<SudokuConstraint> constraintsOnCase(Case c);

    /**
     * Une implémentation de cette méthode doit retourner l'ensemble des cases constituant le puzzle, toujours dans le même ordre
     * @return La liste des cases constituant le puzzle
     */
    ArrayList<Case> casesList();

    /**
     * Une implémentation de cette méthode doit retourner une copie de ce puzzle, qui n'a aucun lien envers le puzzle originel, cette méthode a également pour effet secondaire de recopier les valeurs temporaires des cases comme les "vraies valeurs"
     * @return Une copie de ce puzzle
     */
    Puzzle copy();
}
