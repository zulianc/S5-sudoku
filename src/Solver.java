import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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
        ArrayList<SudokuConstraint> constraints = Objects.requireNonNullElseGet(additionalConstraints, ArrayList::new);
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
            notChangedInARow = (constraintsNumber == constraints.size()) ? notChangedInARow + 1 : 0;
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
        // on crée une copie du puzzle
        Puzzle newPuzzle = puzzle.copy();
        ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, additionalConstraints);


        // on applique le backtracking
        Puzzle backtrack = applyBacktracking(newPuzzle, newConstraints, true);
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
    private static Puzzle applyBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> constraints, boolean isRecursive) {
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
                value = (int) c.possibleValues().toArray()[ThreadLocalRandom.current().nextInt(0, c.possibleValues().size())];
                c.tryTestValue(value);
                // on vérifie si les contraintes sont toujours respectées
                isValid = true;
                for (SudokuConstraint constraint : puzzle.constraintsOnCase(c)) {
                    if (!constraint.isConstraintValid()) {
                        isValid = false;
                    }
                }
                for (SudokuConstraint constraint : constraints) {
                    if (!constraint.isConstraintValid()) {
                        isValid = false;
                    }
                }
                if (!isValid) {
                    // si elles ne les sont pas alors la valeur n'était pas la bonne
                    c.removePossibleValue(value);
                }
            } while (!isValid);

            // on met à jour les contraintes additionnels sur la copie du puzzle
            Puzzle newPuzzle = puzzle.copy();
            ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, constraints);

            // on teste la valeur candidate
            if (isRecursive) {
                backtrack = applyBacktracking(newPuzzle, newConstraints, true);
            }
            else {
                backtrack = applyBoth(newPuzzle, newConstraints);
            }
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

    /**
     * Essaie de résoudre un puzzle à l'aide des règles de contraintes et du backtracking
     * @param puzzle Le puzzle qu'on veut résoudre
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @return Un booléen qui indique si le puzzle est résolvable
     */
    public static boolean solveWithBoth(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints) {
        // on crée une copie du puzzle
        Puzzle newPuzzle = puzzle.copy();
        ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, additionalConstraints);

        // on applique l'algorithme
        Puzzle backtrack = applyBoth(newPuzzle, newConstraints);
        // si l'algo a échoué
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
     * Applique l'algo mixte pour une copie d'un puzzle et des contraintes sur ce puzzle
     * @param puzzle Une copie d'un puzzle
     * @param constraints Des contraintes additionnelles sur le puzzle
     * @return Le puzzle résolu s'il est résolvable, null sinon
     */
    private static Puzzle applyBoth(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) {
        // on récupère les contraintes
        ArrayList<SudokuConstraint> everyConstraints = new ArrayList<>();
        if (constraints != null) {
            everyConstraints.addAll(constraints);
        }
        everyConstraints.addAll(puzzle.defaultConstraints());

        // on applique les contraintes une fois
        int size;
        do {
            size = everyConstraints.size();
            boolean isValid = applyConstraints(everyConstraints);
            if (!isValid) {
                return null;
            }
        } while (everyConstraints.size() != size);

        // on applique le backtracking une fois
        return applyBacktracking(puzzle, constraints, false);
    }

    /**
     * Crée une solution possible pour un puzzle vide passé en paramètre
     * @param puzzle Le puzzle sur lequel créé la solution, qui doit être vide
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @return Un booléen qui indique si le puzzle est résolvable
     * @throws IllegalArgumentException Si le puzzle donné n'est pas vide
     */
    public static boolean generateNewSolvedPuzzle(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints) throws IllegalArgumentException{
        ArrayList<Case> cases = puzzle.casesList();
        for (Case c : cases) {
            if (!(c.getValue() == -1)) {
                throw new IllegalArgumentException("Le puzzle doit être vide !");
            }
        }
        // on utilise l'algo mixte, car il est plus rapide que l'algo de backtracking pur
        return solveWithBoth(puzzle, additionalConstraints);
    }

    /**
     * Crée un puzzle à résoudre à partir d'un puzzle résolu passé en paramètre
     * @param puzzle Le puzzle sur lequel créé une grille à résoudre, qui doit être résolu
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @param difficulty La difficulté qu'aura le puzzle, soit 1 (facile), 2 (moyen) ou 3 (difficile)
     * @return Un booléen qui indique si on a enlevé assez de cases pour atteindre la difficulté souhaitée
     * @throws IllegalArgumentException Si le puzzle donné n'est pas résolu ou la difficulté n'existe pas
     */
    public static boolean generateNewPuzzleToSolve(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, int difficulty) throws IllegalArgumentException{
        ArrayList<SudokuConstraint> constraints = puzzle.defaultConstraints();
        for (SudokuConstraint constraint : constraints) {
            if (!(constraint.hasBeenResolved())) {
                throw new IllegalArgumentException("Le puzzle doit être résolu !");
            }
        }
        if (difficulty < 0 || difficulty > 2) {
            throw new IllegalArgumentException("La difficulté doit être comprise entre 0 et 2 !");
        }
        // on applique l'algo
        return tryToRemoveValue(puzzle, additionalConstraints, 0, difficulty);
    }

    /**
     * Applique l'algo de création de puzzle à résoudre sur un puzzle passé en paramètre
     * @param puzzle Le puzzle auquel on essaye d'enlever des cases
     * @param constraints Des contraintes supplémentaires sur le puzzle si on veut en spécifier
     * @param removedCases Le nombre de cases enlevées sur le puzzle donné
     * @param difficulty La difficulté que l'on souhaite avoir sur le puzzle, soit 1 (facile), 2 (moyen) ou 3 (difficile)
     * @return Un booléen qui indique si on a enlevé assez de cases pour atteindre la difficulté souhaitée
     */
    private static boolean tryToRemoveValue(Puzzle puzzle, ArrayList<SudokuConstraint> constraints, int removedCases, int difficulty) {
        ArrayList<Case> casesList =  puzzle.casesList();
        // si assez de cases ont été enlevées on retourne le puzzle
        // facile = 50%, moyen = 67%, difficile = 75%
        if ((casesList.size() - removedCases) * (difficulty + 1) < casesList.size()) {
            return true;
        }
        // on cherche une case à supprimer au hasard
        int candidateNumber = ThreadLocalRandom.current().nextInt(0, casesList.size());
        Case candidate;
        int i = 0;
        do {
            // si la case n'est pas déjà vide alors on cache sa valeur et on regarde si le puzzle est résolvable
            if (!(casesList.get(candidateNumber).getValue() == -1)) {
                candidate = casesList.get(candidateNumber);
                candidate.hideValue();
                Puzzle newPuzzle = puzzle.copy();
                ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, constraints);
                // on utilise l'algo par contraintes, car on est sûr qu'une bonne solution est unique
                boolean isSolvable = solveWithConstraints(newPuzzle, newConstraints);
                // si le puzzle est résolvable alors on essaye d'enlever une autre case
                if (isSolvable) {
                    boolean hasWorked = tryToRemoveValue(puzzle, constraints, removedCases + 1, difficulty);
                    // si l'algo a marché
                    if (hasWorked) {
                        return true;
                    }
                }
                // sinon, on remontre la valeur de la case et on passe à la suivante
                else {
                    candidate.showValue();
                }
            }
            i++;
            candidateNumber = (candidateNumber + 1) % casesList.size();
        } while (i < casesList.size());
        // si on a testé toutes les cases et qu'aucune ne marche, c'est qu'on ne peut pas en enlever plus
        return false;
    }

    /**
     * Copie une liste de contraintes faisant référence à un ancien puzzle en des contraintes faisant référence à un nouveau puzzle
     * @param newPuzzle Le nouveau puzzle auquel faire référence
     * @param constraints Les contraintes à copier
     * @return Une copie de la liste de contraintes faisant référence au nouveau puzzle
     */
    private static ArrayList<SudokuConstraint> copyConstraints(Puzzle newPuzzle, ArrayList<SudokuConstraint> constraints) {
        ArrayList<SudokuConstraint> newConstraints = new ArrayList<>();
        if (constraints != null) {
            for (SudokuConstraint constraint : constraints) {
                newConstraints.add(constraint.copy(newPuzzle));
            }
        }
        return newConstraints;
    }
}
