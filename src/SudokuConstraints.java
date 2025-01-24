import java.util.ArrayList;
import java.util.Set;

public abstract class SudokuConstraints {
    private final Case caseHasContraint;
    private final ArrayList<Case> casesToCompareTo;

    public SudokuConstraints(Case caseHasConstraint, ArrayList<Case> casesToCompareTo) {
        this.caseHasContraint = caseHasConstraint;
        this.casesToCompareTo = casesToCompareTo;
    }

    public abstract boolean setNewValue();
}
