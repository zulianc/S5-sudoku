import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotEqualConstraint implements SudokuConstraints {
    private final Case constrainedCase;
    private final ArrayList<Case> casesToCompareTo;

    public NotEqualConstraint(Case caseHasConstraint, ArrayList<Case> casesToCompareTo) {
        this.constrainedCase = caseHasConstraint;
        this.casesToCompareTo = casesToCompareTo;
    }

    @Override
    public boolean setNewPossibleValues() {
        Set<Integer> casePossibleValues = this.constrainedCase.getPossibleValues();
        int caseValue = this.constrainedCase.getValeur();

        for (Case caseToCompare : this.casesToCompareTo) {
            if (caseValue == -1) {
                if (caseToCompare.getValeur() != -1) {
                    casePossibleValues.remove(caseToCompare.getValeur());
                }
            }
            else {
                if (caseToCompare.getValeur() == caseValue) {
                    return false;
                }
            }
        }
        if (casePossibleValues.isEmpty()) {
            return false;
        }
        if (casePossibleValues.size() == 1) {
            this.constrainedCase.setValeur((int) casePossibleValues.toArray()[0]);
        }
        return true;
    }

    @Override
    public boolean isValidated() {
        Set<Integer> set = new HashSet<>(this.constrainedCase.getPossibleValues());
        return (set.size() == 1 && set.contains(this.constrainedCase.getValeur()));
    }
}
