package Constraints;

import Grids.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Une contrainte de sudoku qui stipule qu'une case ne doit pas avoir la même valeur qu'une liste d'autres cases
 */
public class NotEqualConstraint implements SudokuConstraint {
    /**
     * La case qui subit les contraintes
     */
    private final Case constrainedCase;
    /**
     * Les cases auxquelles on doit comparer leurs valeurs
     */
    private final ArrayList<Case> casesToCompareTo;
    /**
     * Le puzzle sur lequel s'applique la contrainte
     */
    private final Puzzle puzzle;
    /**
     * Indique si la contrainte a en effet modifié la case sur laquelle elle applique une contrainte
     */
    private boolean hasAppliedItsConstraint;

    /**
     * Le constructeur de la classe
     * @param constrainedCase La case qui subit les contraintes
     * @param casesToCompareTo Les cases auxquelles on doit comparer leurs valeurs
     * @param puzzle Le puzzle sur lequel s'applique la contrainte
     */
    public NotEqualConstraint(Case constrainedCase, ArrayList<Case> casesToCompareTo, Puzzle puzzle) {
        this.constrainedCase = constrainedCase;
        this.casesToCompareTo = casesToCompareTo;
        this.puzzle = puzzle;
        this.hasAppliedItsConstraint = false;
    }

    /**
     * Met à jour les valeurs possibles d'une case en fonction des contraintes appliquées dessus
     * @return Un booléen qui indique si la case est devenue irrésolvable
     */
    @Override
    public boolean setNewPossibleValues() {
        for (Case caseToCompare : this.casesToCompareTo) {
            if (caseToCompare.getValue() != -1) {
                if (constrainedCase.removePossibleValue(caseToCompare.getValue()))
                    this.hasAppliedItsConstraint = true;
            }
        }
        return this.constrainedCase.isValid();
    }

    /**
     * Indique si la case respecte toujours les contraintes
     * @return Si la case respecte toujours les contraintes
     */
    @Override
    public boolean isConstraintValid() {
        for (Case caseToCompare : this.casesToCompareTo) {
            if (caseToCompare.getValue() == this.constrainedCase.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Indique si la case sur laquelle sont appliquées les contraintes a été résolue
     * @return Si la case a été résolue
     */
    @Override
    public boolean hasBeenResolved() {
        return (this.constrainedCase.getValue() != -1 && this.constrainedCase.isValid());
    }

    /**
     * Crée une copie de la contrainte qui référence le puzzle passé en paramètre
     * @param newPuzzle Le puzzle sur lequel la nouvelle contrainte doit s'appliquer
     * @return Une copie de la contrainte qui référence le nouveau puzzle
     * @throws IllegalArgumentException Si le puzzle passé en argument n'est pas une copie du puzzle originel
     */
    @Override
    public SudokuConstraint copy(Puzzle newPuzzle) throws IllegalArgumentException {
        Case newConstrainedCase;
        ArrayList<Case> newCasesToCompareTo = new ArrayList<>();
        if (newPuzzle instanceof Sudoku) {
            if (!(this.puzzle instanceof Sudoku)) {
                throw new IllegalArgumentException("La contrainte ne s'appliquait pas sur un sudoku");
            }

            newConstrainedCase = ((Sudoku) newPuzzle).getCase(this.constrainedCase.getLine(), this.constrainedCase.getColumn());
            for (Case caseToCompare : this.casesToCompareTo) {
                newCasesToCompareTo.add(((Sudoku) newPuzzle).getCase(caseToCompare.getLine(), caseToCompare.getColumn()));
            }
        } else if (newPuzzle instanceof Multidoku) {
            if (!(this.puzzle instanceof Multidoku)) {
                throw new IllegalArgumentException("La contrainte ne s'appliquait pas sur un multidoku");
            }

            // on récupère le puzzle sur lequel était appliquée la contrainte
            PlacedSudoku originalSudoku = ((Multidoku) this.puzzle).getSudoku(this.constrainedCase);
            // on récupère le sudoku qui correspond à l'ancien sudoku
            Sudoku newSudoku = ((Multidoku) newPuzzle).getSudoku(originalSudoku.line(), originalSudoku.column()).sudoku();
            // on récupère la nouvelle case aux mêmes coordonnées que l'ancienne
            newConstrainedCase = newSudoku.getCase(this.constrainedCase.getLine(), this.constrainedCase.getColumn());
            // même chose
            for (Case caseToCompare : this.casesToCompareTo) {
                originalSudoku = ((Multidoku) this.puzzle).getSudoku(caseToCompare);
                newSudoku = ((Multidoku) newPuzzle).getSudoku(originalSudoku.line(), originalSudoku.column()).sudoku();
                newCasesToCompareTo.add(newSudoku.getCase(caseToCompare.getLine(), caseToCompare.getColumn()));
            }
        }
        else {
            throw new IllegalArgumentException("Le nouveau puzzle n'est pas d'un genre connu");
        }

        return new NotEqualConstraint(newConstrainedCase, newCasesToCompareTo, newPuzzle);
    }

    /**
     * Indique si la contrainte s'applique sur la même case que celle passée en paramètre
     * @param c La case sur laquelle on veut savoir si la contrainte s'applique
     * @return Si la contrainte s'applique sur la case
     */
    @Override
    public boolean isConstraintOnCase(Case c) {
        return this.constrainedCase == c;
    }

    /**
     * Indique si la contrainte s'applique sur la même case que celle passée en paramètre
     * @param puzzle La case sur laquelle on veut savoir si la contrainte s'applique
     * @return Si la contrainte s'applique sur la case
     */
    @Override
    public boolean isConstraintOnPuzzle(Puzzle puzzle) {
        if (puzzle instanceof Sudoku) {
            if (!(this.constrainedCase == ((Sudoku) puzzle).getCase(this.constrainedCase.getLine(), this.constrainedCase.getColumn()))) return false;
            for (Case caseToCompare : this.casesToCompareTo) {
                if (!(caseToCompare == ((Sudoku) puzzle).getCase(caseToCompare.getLine(), caseToCompare.getColumn()))) return false;
            }
        } else if (puzzle instanceof Multidoku) {
            try {
                ((Multidoku) puzzle).getSudoku(this.constrainedCase);
            }
            catch (IllegalArgumentException e) {
                return false;
            }
            for (Case caseToCompare : this.casesToCompareTo) {
                try {
                    ((Multidoku) puzzle).getSudoku(caseToCompare);
                }
                catch (IllegalArgumentException e) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Crée un string qui représente la contrainte
     * @return Un string qui représente la contrainte
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // type de contrainte
        sb.append("!= ");
        // case contrainte
        if (this.puzzle instanceof Multidoku) {
            // position du sudoku
            PlacedSudoku placedSudoku = ((Multidoku) this.puzzle).getSudoku(this.constrainedCase);
            sb.append(placedSudoku.line() + 1).append(" ").append(placedSudoku.column() + 1).append(" ");
        }
        sb.append(this.constrainedCase.getLine() + 1).append(" ").append(this.constrainedCase.getColumn() + 1).append(" ");
        // cases contraignantes
        for (Case caseToCompare : this.casesToCompareTo) {
            if (this.puzzle instanceof Multidoku) {
                // position du sudoku
                PlacedSudoku placedSudoku = ((Multidoku) this.puzzle).getSudoku(caseToCompare);
                sb.append(placedSudoku.line() + 1).append(" ").append(placedSudoku.column() + 1).append(" ");
            }
            sb.append(caseToCompare.getLine() + 1).append(" ").append(caseToCompare.getColumn() + 1).append(" ");
        }
        return sb.toString();
    }

    /**
     * Retourne un String qui log une contrainte récemment appliquée, ou null s'il y en a pas
     * @return Un String qui log la dernière contrainte appliquée, ou null sinon
     */
    @Override
    public String log() {
        if (!this.hasAppliedItsConstraint) return null;
        StringBuilder sb = new StringBuilder();
        if (this.puzzle instanceof Multidoku) {
            // position du sudoku
            PlacedSudoku placedSudoku = ((Multidoku) this.puzzle).getSudoku(this.constrainedCase);
            sb.append(placedSudoku.line() + 1).append(" ").append(placedSudoku.column() + 1).append(" ");
        }
        // position de la case
        sb.append(this.constrainedCase.getLine() + 1).append(" ").append(this.constrainedCase.getColumn() + 1).append(" ");
        // valeur de la case
        sb.append("-> ").append(this.constrainedCase.getValue() + 1);
        return sb.toString();
    }

    public NotEqualConstraint(Case case1, Case case2, Puzzle puzzle) {
        this.constrainedCase = case1;
        this.casesToCompareTo = new ArrayList<>(Arrays.asList(case2));  // Transforme la deuxième case en une liste
        this.puzzle = puzzle;
        this.hasAppliedItsConstraint = false;
    }

}
