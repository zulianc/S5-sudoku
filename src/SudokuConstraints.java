import java.util.ArrayList;

public abstract class SudokuConstraints {
    private final Case caseHasContraint;
    private final ArrayList<Case> casesToCompareTo;

    public SudokuConstraints(Case caseHasConstraint, ArrayList<Case> casesToCompareTo) {
        this.caseHasContraint = caseHasConstraint;
        this.casesToCompareTo = casesToCompareTo;
    }

    public abstract boolean setNewPossibleValues();

    public abstract boolean isValidated();

    protected Case getCase() {
        return this.caseHasContraint;
    }

    protected ArrayList<Case> getCasesToCompareTo() {
        return this.casesToCompareTo;
    }
}
