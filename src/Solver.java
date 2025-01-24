import java.util.ArrayList;

public class Solver<T extends SudokuConstraints> {
    private final Sudoku sudoku;
    private final ArrayList<T> constraints;

    public Solver(Sudoku sudoku, ArrayList<T> constraints) {
        this.sudoku = sudoku;
        this.constraints = constraints;
    }

    public boolean solve() {

        return false;
    }
}
