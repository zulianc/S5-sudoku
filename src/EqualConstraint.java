import java.util.ArrayList;

/**
 * Une contrainte de sudoku qui stipule qu'une case doit avoir la même valeur qu'une liste d'autres cases
 */
public class EqualConstraint implements SudokuConstraint {
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
     * Le constructeur de la classe
     * @param constrainedCase La case qui subit les contraintes
     * @param casesToCompareTo Les cases auxquelles on doit comparer leurs valeurs
     * @param puzzle Le puzzle sur lequel s'applique la contrainte
     */
    public EqualConstraint(Case constrainedCase, ArrayList<Case> casesToCompareTo, Puzzle puzzle) {
        this.constrainedCase = constrainedCase;
        this.casesToCompareTo = casesToCompareTo;
        this.puzzle = puzzle;
    }

    /**
     * Met à jour les valeurs possibles d'une case en fonction des contraintes appliquées dessus
     * @return Un booléen qui indique si la case est devenue irrésolvable
     */
    @Override
    public boolean setNewPossibleValues() {
        for (Case caseToCompare : this.casesToCompareTo) {
            try {
                if (caseToCompare.getValue() != -1) {
                    constrainedCase.setValue(caseToCompare.getValue());
                }
            }
            catch (IllegalArgumentException e) {
                return false;
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
            if (caseToCompare.getValue() != -1) {
                if (constrainedCase.getValue() != caseToCompare.getValue()) {
                    return false;
                }
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
     * @throws IllegalArgumentException Si une erreur arrive parce que le puzzle en argument n'est pas une copie du puzzle originel
     */
    @Override
    public SudokuConstraint copy(Puzzle newPuzzle) throws IllegalArgumentException {
        Case newConstrainedCase;
        ArrayList<Case> newCasesToCompareTo = new ArrayList<>();
        if (newPuzzle instanceof Sudoku) {
            if (!(puzzle instanceof Sudoku)) {
                throw new IllegalArgumentException("La contrainte ne s'appliquait pas sur un sudoku");
            }

            newConstrainedCase = ((Sudoku) newPuzzle).getCase(this.constrainedCase.getLine(), this.constrainedCase.getColumn());
            for (Case caseToCompare : this.casesToCompareTo) {
                newCasesToCompareTo.add(((Sudoku) newPuzzle).getCase(caseToCompare.getLine(), caseToCompare.getColumn()));
            }
        } else if (newPuzzle instanceof Multidoku) {
            if (!(puzzle instanceof Multidoku)) {
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

        return new EqualConstraint(newConstrainedCase, newCasesToCompareTo, newPuzzle);
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
                ((Multidoku) this.puzzle).getSudoku(this.constrainedCase);
            }
            catch (IllegalArgumentException e) {
                return false;
            }
            for (Case caseToCompare : this.casesToCompareTo) {
                try {
                    ((Multidoku) this.puzzle).getSudoku(caseToCompare);
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
     * Crée un string qui représente la case
     * @return Un string qui représente la case
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // type de contrainte
        sb.append("= ");
        // case contrainte
        sb.append(this.constrainedCase.getLine() + 1).append(" ").append(this.constrainedCase.getColumn() + 1).append(" ");
        // cases contraignantes
        for (Case caseToCompare : this.casesToCompareTo) {
            sb.append(caseToCompare.getLine() + 1).append(" ").append(caseToCompare.getColumn() + 1).append(" ");
        }
        return sb.toString();
    }
}
