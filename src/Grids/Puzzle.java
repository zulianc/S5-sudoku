package Grids;

import Constraints.*;

import java.util.ArrayList;

/**
 * Les classes qui implémentent cette interface doivent posséder un ensemble de cases et des contraintes internes sur ces cases
 */
public interface Puzzle {
    /**
     * Une implémentation de cette méthode doit retourner les contraintes internes du puzzle
     * @return Les contraintes internes du puzzle
     * @throws RuntimeException Si une erreur interne arrive
     */
    ArrayList<SudokuConstraint> defaultConstraints() throws RuntimeException;

    /**
     * Une implémentation de cette méthode doit retourner les contraintes internes du puzzle qui s'appliquent sur une case donnée
     * @param c La case sur laquelle les contraintes sont appliquées
     * @return La liste des contraintes appliquées sur cette case
     * @throws RuntimeException Si une erreur interne arrive
     */
    ArrayList<SudokuConstraint> constraintsOnCase(Case c) throws RuntimeException;

    /**
     * Une implémentation de cette méthode doit retourner l'ensemble des cases constituant le puzzle, toujours dans le même ordre
     * @return La liste des cases constituant le puzzle
     * @throws RuntimeException Si une erreur interne arrive
     */
    ArrayList<Case> casesList() throws RuntimeException;

    /**
     * Une implémentation de cette méthode doit retourner une copie de ce puzzle, qui n'a aucun lien envers le puzzle originel, cette méthode a également pour effet secondaire de recopier les valeurs temporaires des cases comme les "vraies valeurs"
     * @return Une copie de ce puzzle
     * @throws RuntimeException Si une erreur interne arrive
     */
    Puzzle copy() throws RuntimeException;

    /**
     * Une implémentation de cette méthode doit retourner un String représentant le puzzle, qui peut être affiché dans le terminal
     * @return Un String représent le puzzle
     */
    @Override
    String toString();
}
