import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Multidoku {
    private final ArrayList<PlacedSudoku> sudokus;
    private final int sizeSudokus;
    private final int sizeMultidokuGrid;
    private HashMap<Integer, String> symbolsSudokus;

    public Multidoku(ArrayList<PlacedSudoku> sudokus) {
        int size = sudokus.getFirst().sudoku().getSize();
        for (PlacedSudoku sudoku : sudokus) {
            if (size != sudoku.sudoku().getSize()) throw new IllegalArgumentException("All sudokus are not the same size");
        }

        this.sudokus = sudokus;
        this.sizeSudokus = size;
        this.sizeMultidokuGrid = (size * 2) - 1;
    }

    public Multidoku(ArrayList<PlacedSudoku> sudokus, HashMap<Integer, String> symbolsSudokus) {
        this(sudokus);
        this.setSymbolsSudokus(symbolsSudokus);
    }

    public ArrayList<PlacedSudoku> getSudokus() {
        return this.sudokus;
    }

    public Sudoku getSudoku(int row, int col) {
        for (PlacedSudoku sudoku : this.sudokus) {
            if (sudoku.row() == row && sudoku.col() == col) {
                return sudoku.sudoku();
            }
        }
        return null;
    }

    public int getSizeSudokus() {
        return this.sizeSudokus;
    }

    public int getSizeMultidokuGrid() {
        return this.sizeMultidokuGrid;
    }

    public HashMap<Integer, String> getSymbolsSudokus() {
        return this.symbolsSudokus;
    }

    public void setSymbolsSudokus(HashMap<Integer, String> symbolsSudokus) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < this.sizeSudokus; i++) {
            set.add(i);
        }

        if(symbolsSudokus.keySet().equals(set)) {
            this.symbolsSudokus = symbolsSudokus;
        }
        else {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    @Override
    public String toString() {
        return "";
        //TODO
    }
}
