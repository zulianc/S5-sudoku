import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotEqualConstraint extends SudokuConstraints {
    public NotEqualConstraint(Case caseHasConstraint, ArrayList<Case> casesToCompareTo) {
        super(caseHasConstraint, casesToCompareTo);
    }

    @Override
    public boolean setNewPossibleValues() {
        Set<Integer> baseSet = super.getCase().getPossibleValues();
        int valeur = super.getCase().getValeur();

        for (Case c : super.getCasesToCompareTo()) {
            if (valeur == -1) {
                if (c.getValeur() != -1) {
                    baseSet.remove(c.getValeur());
                }
            }
            else {
                if (c.getValeur() == valeur) {
                    return false;
                }
            }
        }
        if (baseSet.isEmpty()) {
            return false;
        }
        if (baseSet.size() == 1) {
            super.getCase().setValeur((int) baseSet.toArray()[0]);
        }
        return true;
    }

    @Override
    public boolean isValidated() {
        Set<Integer> set = new HashSet<>(super.getCase().getPossibleValues());
        return (set.size() == 1 && set.contains(super.getCase().getValeur()));
    }
}
