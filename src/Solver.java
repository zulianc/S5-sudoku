import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Une classe qui permet de résoudre un puzzle en utilisant simplement les règles de déduction
 */
public abstract class Solver {
    /**
     * Essaie de résoudre un puzzle à l'aide des règles de déduction uniquement
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
            boolean validated = c.hasBeenResolved();
            // si une règle a été validée, alors on la supprime
            if (validated) {
                it.remove();
            }
        }
        return true;
    }

    /**
     * Essaie de résoudre un puzzle à l'aide du backtracking uniquement
     * @param puzzle Le puzzle qu'on veut résoudre
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @return Un booléen qui indique si le puzzle est résolvable
     */
    public static boolean solveWithBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints) {
        // on met à jour les contraintes additionnels sur la copie du sudoku
        Puzzle newPuzzle = puzzle.copy();
        ArrayList<SudokuConstraint> newConstraints = new ArrayList<>();
        if (additionalConstraints != null) {
            for (SudokuConstraint constraint : additionalConstraints) {
                newConstraints.add(constraint.copy(newPuzzle));
            }
        }

        // on applique le backtracking
        Puzzle backtrack = applyBacktracking(newPuzzle, newConstraints);
        // si la backtracking a échoué
        if (backtrack == null) {
            return false;
        }
        // sinon on copie le puzzle solvé dans le puzzle a solver
        ArrayList<Case> originalCases = puzzle.casesList();
        ArrayList<Case> backtrackCases = backtrack.casesList();
        for (int i = 0; i < backtrackCases.size(); i++) {
            originalCases.get(i).setValue(backtrackCases.get(i).getValue());
        }
        return true;
    }

    /**
     * Applique l'algo de backtracking pur pour une copie d'un puzzle et des contraintes sur ce puzzle
     * @param puzzle Une copie d'un puzzle
     * @param constraints Des contraintes additionnelles sur le puzzle
     * @return Le puzzle résolu s'il est résolvable, null sinon
     */
    private static Puzzle applyBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        // on cherche la première case non résolue
        int currentCase = 0;
        while (puzzle.casesList().get(currentCase).hasValue()) {
            currentCase++;
            // si on a atteint la fin du puzzle, alors c'est qu'il est résolu
            if (currentCase == puzzle.casesList().size()) {
                return puzzle;
            }
        }
        Case c = puzzle.casesList().get(currentCase);
        // on teste toutes ses valeurs une par une
        Puzzle backtrack;
        int value;
        boolean isValid;
        do {
            do {
                // on vérifie si la case est encore résolvable
                if (!c.isValid()) return null;
                // on essaie une de ses valeurs possibles
                value = c.possibleValues().iterator().next();
                c.tryTestValue(value);
                // on vérifie si les contraintes sont toujours respectées
                isValid = true;
                for (SudokuConstraint constraint : puzzle.constraintsOnCase(c)) {
                    if (!constraint.isConstraintValid())
                        isValid = false;
                }
                for (SudokuConstraint constraint : constraints) {
                    if (!constraint.isConstraintValid())
                        isValid = false;
                }
                if (!isValid) {
                    // si elles ne les sont pas alors la valeur n'était pas la bonne
                    c.removePossibleValue(value);
                }
            } while (!isValid);

            // on met à jour les contraintes additionnels sur la copie du sudoku
            Puzzle newPuzzle = puzzle.copy();
            ArrayList<SudokuConstraint> newConstraints = new ArrayList<>();
            for (SudokuConstraint constraint : constraints) {
                newConstraints.add(constraint.copy(newPuzzle));
            }

            // on teste la valeur candidate
            backtrack = applyBacktracking(newPuzzle, newConstraints);
            if (backtrack == null) {
                // si l'algo n'a pas abouti, la valeur n'était donc pas la bonne
                c.removePossibleValue(value);
                c.scrapTestValue();
            } else {
                // sinon, on confirme la valeur
                c.confirmTestValue();
            }
        } while (backtrack == null);
        return backtrack;
    }

    public static boolean solveWithBoth(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        //TODO
        return false;
    }
}
