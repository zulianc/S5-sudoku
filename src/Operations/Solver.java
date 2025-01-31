package Operations;

import Constraints.*;
import Grids.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Une classe qui permet de résoudre un puzzle en utilisant simplement les règles de déduction
 */
public abstract class Solver {
    /**
     * Une liste de string qui capture les opérations effectuées lors de la résolution
     */
    private static ArrayList<String> logs = new ArrayList<>();
    /**
     * Indique si l'algorithme doit momentanément cesser de logger l'opération
     */
    private static boolean stopLogging = false;

    /**
     * Essaie de résoudre un puzzle à l'aide des règles de déduction uniquement
     * @param puzzle Le puzzle qu'on veut résoudre
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @param menuLogs Une liste de logs à laquelle ajouter les logs de l'algorithme
     * @return Un booléen qui indique si le puzzle est résolvable
     */
    public static boolean solveWithConstraints(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, ArrayList<String> menuLogs) {
        // on initialise la liste des logs
        if (menuLogs != null) {
            logs = new ArrayList<>();
        }

        // on récupère les contraintes
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
            // cette méthode ne revient jamais en arrière donc on peut juste prendre un gros nombre (10) pour être sûr
            notChangedInARow = (constraintsNumber == constraints.size()) ? notChangedInARow + 1 : 0;
            if (notChangedInARow > 10) {
                return false;
            }
        }

        // on donne les logs au menu
        if (menuLogs != null) {
            menuLogs.addAll(logs);
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
            SudokuConstraint constraint = it.next();
            boolean validMove = constraint.setNewPossibleValues();
            log(constraint);
            if (!validMove) {
                return false;
            }
            boolean validated = constraint.hasBeenResolved();
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
     * @param menuLogs Une liste de logs à laquelle ajouter les logs de l'algorithme
     * @return Un booléen qui indique si le puzzle est résolvable
     * @throws IllegalArgumentException Si une erreur arrive durant la résolution
     */
    public static boolean solveWithBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, ArrayList<String> menuLogs) throws IllegalArgumentException {
        // on initialise la liste des logs
        if (menuLogs != null) {
            logs = new ArrayList<>();
        }

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

        // on donne les logs au menu
        if (menuLogs != null) {
            menuLogs.addAll(logs);
        }

        return true;
    }

    /**
     * Applique l'algo de backtracking pur pour une copie d'un puzzle et des contraintes sur ce puzzle
     * @param puzzle Une copie d'un puzzle
     * @param constraints Des contraintes additionnelles sur le puzzle
     * @param isPureBacktracking Indique si la méthode est appelée dans le cas de l'algo de backtracking pure ou de l'algo mixte
     * @return Le puzzle résolu s'il est résolvable, null sinon
     * @throws IllegalArgumentException Si une erreur arrive durant la résolution
     */
    private static Puzzle applyBacktracking(Puzzle puzzle, ArrayList<SudokuConstraint> constraints, boolean isPureBacktracking) throws IllegalArgumentException {
        // on cherche la case à tester
        Case testedCase;
        ArrayList<Case> casesList = puzzle.casesList();
        // si on n'est pas intelligent
        if (isPureBacktracking) {
            // on cherche la première case non résolue
            int currentCase = 0;
            while (casesList.get(currentCase).hasValue()) {
                currentCase++;
                // si on a atteint la fin du puzzle, alors c'est qu'il est résolu
                if (currentCase == casesList.size()) {
                    return puzzle;
                }
            }
            testedCase = casesList.get(currentCase);
        }
        // si on est intelligent
        else {
            // on cherche la case avec le moins de valeurs possibles
            int smallestAmountOfValues = 0;
            int currentSmallestCase = -1;
            for (int i = 0; i < casesList.size(); i++) {
                Case c = casesList.get(i);
                // on ne teste que les cases qui ne sont pas résolues
                if (!c.hasValue()) {
                    if (smallestAmountOfValues == 0 || smallestAmountOfValues > c.possibleValues().size()) {
                        smallestAmountOfValues = c.possibleValues().size();
                        currentSmallestCase = i;
                    }
                }
            }
            // si aucune case a plusieurs valeurs, c'est que le puzzle est résolu
            if (currentSmallestCase == -1) {
                return puzzle;
            }
            // sinon, on prend la case avec le moins de valeurs possibles
            testedCase = casesList.get(currentSmallestCase);
        }

        // on teste toutes ses valeurs une par une
        Puzzle backtrack;
        int value;
        boolean isValid;
        do {
            do {
                // on vérifie si la case est encore résolvable
                if (!testedCase.isValid()) return null;
                // on essaie une de ses valeurs possibles
                value = (int) testedCase.possibleValues().toArray()[ThreadLocalRandom.current().nextInt(0, testedCase.possibleValues().size())];
                testedCase.tryTestValue(value);
                log(testedCase, puzzle);
                // on vérifie si les contraintes sont toujours respectées
                isValid = true;
                for (SudokuConstraint constraint : puzzle.constraintsOnCase(testedCase)) {
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
                    testedCase.removePossibleValue(value);
                }
            } while (!isValid);

            // on met à jour les contraintes additionnels sur la copie du puzzle
            Puzzle newPuzzle = puzzle.copy();
            ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, constraints);

            // on teste la valeur candidate
            if (isPureBacktracking) {
                backtrack = applyBacktracking(newPuzzle, newConstraints, true);
            }
            else {
                backtrack = applyBoth(newPuzzle, newConstraints);
            }
            if (backtrack == null) {
                // si l'algo n'a pas abouti, la valeur n'était donc pas la bonne
                testedCase.removePossibleValue(value);
                testedCase.scrapTestValue();
                log(testedCase, puzzle);
            } else {
                // sinon, on confirme la valeur
                testedCase.confirmTestValue();
                // pas besoin de log, car on ne change pas la valeur visible
            }
        } while (backtrack == null);
        return backtrack;
    }

    /**
     * Essaie de résoudre un puzzle à l'aide des règles de contraintes et du backtracking
     * @param puzzle Le puzzle qu'on veut résoudre
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @param menuLogs Une liste de logs à laquelle ajouter les logs de l'algorithme
     * @return Un booléen qui indique si le puzzle est résolvable
     * @throws IllegalArgumentException Si une erreur arrive durant la résolution
     */
    public static boolean solveWithBoth(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, ArrayList<String> menuLogs) throws IllegalArgumentException {
        // on initialise la liste des logs
        if (menuLogs != null) {
            logs = new ArrayList<>();
        }

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

        // on donne les logs au menu
        if (menuLogs != null) {
            menuLogs.addAll(logs);
        }

        return true;
    }

    /**
     * Applique l'algo mixte pour une copie d'un puzzle et des contraintes sur ce puzzle
     * @param puzzle Une copie d'un puzzle
     * @param constraints Des contraintes additionnelles sur le puzzle
     * @return Le puzzle résolu s'il est résolvable, null sinon
     * @throws IllegalArgumentException Si une erreur arrive durant la résolution
     */
    private static Puzzle applyBoth(Puzzle puzzle, ArrayList<SudokuConstraint> constraints) throws IllegalArgumentException {
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
     * @param menuLogs Une liste de logs à laquelle ajouter les logs de l'algorithme
     * @return Un booléen qui indique si le puzzle est résolvable
     * @throws IllegalArgumentException Si le puzzle donné n'est pas vide, ou si une erreur arrive durant la génération
     */
    public static boolean generateNewSolvedPuzzle(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, ArrayList<String> menuLogs) throws IllegalArgumentException{
        // on initialise la liste des logs
        if (menuLogs != null) {
            logs = new ArrayList<>();
        }

        // on vérifie les arguments
        ArrayList<Case> cases = puzzle.casesList();
        for (Case c : cases) {
            if (!(c.getValue() == -1)) {
                throw new IllegalArgumentException("Le puzzle doit être vide !");
            }
        }

        // on utilise l'algo mixte, car il est plus rapide que l'algo de backtracking pur
        boolean returnValue = solveWithBoth(puzzle, additionalConstraints, null);

        // on donne les logs au menu
        if (menuLogs != null) {
            menuLogs.addAll(logs);
        }

        return returnValue;
    }

    /**
     * Crée un puzzle à résoudre à partir d'un puzzle résolu passé en paramètre
     * @param puzzle Le puzzle sur lequel créé une grille à résoudre, qui doit être résolu
     * @param additionalConstraints Des contraintes supplémentaires si on veut en rajouter
     * @param difficulty La difficulté qu'aura le puzzle, soit 1 (facile), 2 (moyen) ou 3 (difficile)
     * @param menuLogs Une liste de logs à laquelle ajouter les logs de l'algorithme
     * @return Un booléen qui indique si on a enlevé assez de cases pour atteindre la difficulté souhaitée
     * @throws IllegalArgumentException Si le puzzle donné n'est pas résolu, la difficulté n'existe pas, ou si une erreur arrive durant la génération
     */
    public static boolean generateNewPuzzleToSolve(Puzzle puzzle, ArrayList<SudokuConstraint> additionalConstraints, int difficulty, ArrayList<String> menuLogs) throws IllegalArgumentException{
        // on initialise la liste des logs
        if (menuLogs != null) {
            logs = new ArrayList<>();
        }

        // on vérifie les arguments
        ArrayList<SudokuConstraint> constraints = puzzle.defaultConstraints();
        for (SudokuConstraint constraint : constraints) {
            if (!(constraint.hasBeenResolved())) {
                throw new IllegalArgumentException("Le puzzle doit être résolu !");
            }
        }
        if (difficulty < 1 || difficulty > 3) {
            throw new IllegalArgumentException("La difficulté doit être comprise entre 1 et 3 !");
        }

        // on applique l'algo
        boolean returnValue = tryToRemoveValue(puzzle, additionalConstraints, 0, difficulty);

        // on donne les logs au menu
        if (menuLogs != null) {
            menuLogs.addAll(logs);
        }

        return returnValue;
    }

    /**
     * Applique l'algo de création de puzzle à résoudre sur un puzzle passé en paramètre
     * @param puzzle Le puzzle auquel on essaye d'enlever des cases
     * @param constraints Des contraintes supplémentaires sur le puzzle si on veut en spécifier
     * @param removedCases Le nombre de cases enlevées sur le puzzle donné
     * @param difficulty La difficulté que l'on souhaite avoir sur le puzzle, soit 1 (facile), 2 (moyen) ou 3 (difficile)
     * @return Un booléen qui indique si on a enlevé assez de cases pour atteindre la difficulté souhaitée
     * @throws IllegalArgumentException Si une erreur arrive durant la génération
     */
    private static boolean tryToRemoveValue(Puzzle puzzle, ArrayList<SudokuConstraint> constraints, int removedCases, int difficulty) throws IllegalArgumentException {
        ArrayList<Case> casesList =  puzzle.casesList();
        // si assez de cases ont été enlevées on retourne le puzzle
        // facile = 50%, moyen = 67%, difficile = 75%
        if ((casesList.size() - removedCases) * (difficulty + 1) <= casesList.size()) {
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
                log(candidate, puzzle);
                // on met à jour les contraintes sur la copie du puzzle
                Puzzle newPuzzle = puzzle.copy();
                ArrayList<SudokuConstraint> newConstraints = copyConstraints(newPuzzle, constraints);
                // on utilise l'algo par contraintes, car on est sûr qu'une bonne solution est unique
                stopLogging = true;
                boolean isSolvable = solveWithConstraints(newPuzzle, newConstraints, null);
                stopLogging = false;
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
                    log(candidate, puzzle);
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
     * @throws IllegalArgumentException Si le puzzle passé en argument n'est pas une copie du puzzle sur lequel s'appliquent les contraintes
     */
    private static ArrayList<SudokuConstraint> copyConstraints(Puzzle newPuzzle, ArrayList<SudokuConstraint> constraints) throws IllegalArgumentException {
        ArrayList<SudokuConstraint> newConstraints = new ArrayList<>();
        if (constraints != null) {
            for (SudokuConstraint constraint : constraints) {
                newConstraints.add(constraint.copy(newPuzzle));
            }
        }
        return newConstraints;
    }

    /**
     * Rajoute un log à la suite dans la liste des logs
     * @param c La case que l'on veut logger
     * @param puzzle Le puzzle auquel appartient la case
     */
    private static void log(Case c, Puzzle puzzle) {
        try {
            if (stopLogging) return;
            StringBuilder sb = new StringBuilder();
            if (puzzle instanceof Multidoku) {
                // position du sudoku
                PlacedSudoku placedSudoku = ((Multidoku) puzzle).getSudoku(c);
                sb.append(placedSudoku.line() + 1).append(" ").append(placedSudoku.column() + 1).append(" ");
            }
            // position de la case
            sb.append(c.getLine() + 1).append(" ").append(c.getColumn() + 1).append(" ");
            // valeur de la case
            sb.append("-> ").append(c.getValue() + 1);
            logs.addLast(sb.toString());
        }
        catch (RuntimeException e) {
            logs.addLast("Erreur de création de log : " + e.getMessage());
        }
    }

    /**
     * Rajoute un log à la suite dans la liste des logs
     * @param constraint La contrainte qui s'applique sur la case que l'on veut logger
     */
    private static void log(SudokuConstraint constraint) {
        if (stopLogging) return;
        String log = constraint.log();
        if (log != null) {
            logs.addLast(log);
        }
    }
}
