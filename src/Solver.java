import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

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
    public static boolean solveWithConstraints(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints) {
        //on récupère les contraintes
        ArrayList<SudokuConstraint> constraints;
        constraints = Objects.requireNonNullElseGet(additionalConstraints, ArrayList::new);
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
    private static boolean applyConstraints(ArrayList<SudokuConstraint> constraints) {
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

    public static boolean solveWithBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        Puzzle backtrack = applyBacktracking(puzzle.copy(), constraints);
        if (backtrack == null) {
            return false;
        }
        ArrayList<Case> originalCases = puzzle.casesList();
        ArrayList<Case> backtrackCases = backtrack.casesList();
        for (int i = 0; i < backtrackCases.size(); i++) {
            originalCases.get(i).setValue(backtrackCases.get(i).getValue());
        }
        return true;
    }

    private static Puzzle applyBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        // on cherche la première case non résolue
        int currentCase = 0;
        while (puzzle.casesList().get(currentCase).hasValue()) {
            currentCase++;
            if (currentCase == puzzle.casesList().size()) {
                return puzzle;
            }
        }
        Case c = puzzle.casesList().get(currentCase);
        // on applique les contraintes pour savoir ses valeurs possibles
        boolean isValid = applyConstraints(puzzle.constraintsOnCase(c));
        if (!isValid) {
            return null;
        }
        // on teste toutes les valeurs une par une
        Puzzle backtrack;
        do {
            int value;
            do {
                // on vérifie si la case est encore résolvable
                if (!c.isValid()) return null;
                // on essaie une de ses valeurs possibles
                value = c.possibleValues().iterator().next();
                c.tryValue(value);
                // on vérifie si les contraintes sont toujours respectées
                isValid = applyConstraints(puzzle.constraintsOnCase(c));
                if (!isValid) {
                    c.removePossibleValue(value);
                }
            } while (!isValid);
            // on teste la valeur candidate
            backtrack = applyBacktracking(puzzle.copy(), null);
            if (backtrack == null) {
                // si l'algo n'a pas abouti, la valeur n'était donc pas la bonne
                c.removePossibleValue(value);
            } else {
                // sinon, on confirme la valeur
                c.setValue(value);
            }
        } while (backtrack == null);
        return backtrack;
    }

    public static boolean solveWithBoth(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        //TODO
        return false;
    }
}
