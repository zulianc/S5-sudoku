import java.util.ArrayList;

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
     * Le constructeur de la classe
     * @param constrainedCase La case qui subit les contraintes
     * @param casesToCompareTo Les cases auxquelles on doit comparer leurs valeurs
     */
    public NotEqualConstraint(Case constrainedCase, ArrayList<Case> casesToCompareTo) {
        this.constrainedCase = constrainedCase;
        this.casesToCompareTo = casesToCompareTo;
    }

    /**
     * Met à jour les valeurs possibles d'une case en fonction des contraintes appliquées dessus
     * @return Un booléen qui indique si la case est devenue irrésolvable
     */
    @Override
    public boolean setNewPossibleValues() {
        for (Case caseToCompare : this.casesToCompareTo) {
            if (caseToCompare.getValue() != -1) {
                constrainedCase.removePossibleValue(caseToCompare.getValue());
            }
        }
        return this.constrainedCase.isValid();
    }

    /**
     * Indique si la case sur laquelle sont appliquées les contraintes a été résolue
     * @return Si la case a été résolue
     */
    @Override
    public boolean isValidated() {
        return (this.constrainedCase.getValue() != -1 && this.constrainedCase.isValid());
    }
}
