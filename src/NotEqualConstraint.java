import java.util.ArrayList;

public class NotEqualConstraint extends SudokuConstraints {
    public NotEqualConstraint(Case caseHasConstraint, ArrayList<Case> casesToCompareTo) {
        super(caseHasConstraint, casesToCompareTo);
    }

    @Override
    public boolean setNewValue() {
        return false;
    }
}
