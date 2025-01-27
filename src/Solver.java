import java.util.ArrayList;
import java.util.Iterator;

/**
 * Une classe qui permet de résoudre un puzzle en utilisant simplement les règles de déduction
 */
public abstract class Solver {
    /**
     * Essaie de résoudre un puzzle à l'aide des règles de déduction
     * @param puzzle Le puzzle qu'on veut résoudre
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @return Un booléen qui indique si le puzzle est résolvable
     */
    public static boolean solve(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints) {
        //on récupère les contraintes
        ArrayList<SudokuConstraint> constraints;
        if (additionalConstraints == null) {
            constraints = new ArrayList<>();
        }
        else {
            constraints = additionalConstraints;
        }
        constraints.addAll(puzzle.defaultConstraints());

        // on applique les contraintes tant qu'elles n'ont pas été toutes validées
        int notChangedInARow = 0;
        while (!constraints.isEmpty()) {
            int constraintsNumber = constraints.size();
            boolean valid = applyConstraints(constraints);
            if (!valid) {
                return false;
            }
            // failsafe au cas où l'algorithme serait dans une boucle infinie
            notChangedInARow = (constraintsNumber != constraints.size()) ? notChangedInARow + 1 : 0;
            if (notChangedInARow > 10) {
                return false;
            }
        }
        return true;
    }

    /**
     * Applique chaque contrainte d'une liste de contraintes une fois
     * @param constraints La liste des contraintes à appliquer
     * @return Un booléen qui indique si le puzzle est résolvable
     */
    public static boolean applyConstraints(ArrayList<SudokuConstraint> constraints) {
        Iterator<SudokuConstraint> it = constraints.iterator();
        while (it.hasNext()) {
            SudokuConstraint c = it.next();
            boolean validMove = c.setNewPossibleValues();
            if (!validMove) {
                return false;
            }
            boolean validated = c.isValidated();
            // si une règle a été validée, alors on la supprime
            if (validated) {
                it.remove();
            }
        }
        return true;
    }
}
