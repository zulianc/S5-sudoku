import java.util.ArrayList;
import java.util.HashMap;

public class Multidoku {
    private ArrayList<PlacedSudoku> sudokus;
    private int sizeSudokus;
    private HashMap<Integer, String> symbolsSudokus;

    public Multidoku(ArrayList<PlacedSudoku> sudokus, int sizeSudokus, HashMap<Integer, String> symbolsSudokus) {
        this.sudokus = sudokus;
        this.sizeSudokus = sizeSudokus;
        this.symbolsSudokus = symbolsSudokus;
    }

    public ArrayList<PlacedSudoku> getSudokus() {
        return sudokus;
    }

    public Sudoku getSudoku(int row, int col) {
        for (PlacedSudoku sudoku : sudokus) {
            if (sudoku.getRow() == row && sudoku.getCol() == col) {
                return sudoku.getSudoku();
            }
        }
        return null;
    }

    public int getSizeSudokus() {
        return sizeSudokus;
    }

    public HashMap<Integer, String> getSymbolsSudokus() {
        return symbolsSudokus;
    }

    @Override
    public String toString() {
        return "";
        //TODO
    }
}
