import java.util.ArrayList;
import java.util.Iterator;

public class Solver {
    private final ArrayList<SudokuConstraints> constraints;

    public Solver(Sudoku sudoku, ArrayList<SudokuConstraints> constraints) {
        if (constraints == null) {
            this.constraints = new ArrayList<>();
        }
        else {
            this.constraints = constraints;
        }
        this.constraints.addAll(sudoku.getDefaultConstraints());
    }

    public boolean solve() {
        int notChangedInARow = 0;
        boolean changed;
        while (!this.constraints.isEmpty()) {
            changed = false;
            Iterator<SudokuConstraints> it = this.constraints.iterator();
            while (it.hasNext()) {
                SudokuConstraints c = it.next();
                boolean validMove = c.setNewPossibleValues();
                if (!validMove) {
                    return false;
                }
                boolean validated = c.isValidated();
                if (validated) {
                    it.remove();
                    changed = true;
                }
            }
            notChangedInARow = (!changed) ? notChangedInARow + 1 : 0;
            if (notChangedInARow > 10) {
                return false;
            }
        }
        return true;
    }
}
