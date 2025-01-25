import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public abstract class FilesOperations {
    public static void convertSudokuToFile(Sudoku sudoku) {
        //TODO
    }

    public static Sudoku readSudokuFromFile(String filepath) {
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String puzzleType = br.readLine();
            if (puzzleType.equals("sudoku")) {
                br.readLine();
                boolean hasSymbols = Boolean.parseBoolean(br.readLine());
                br.readLine();
                int size = Integer.parseInt(br.readLine());

                br.readLine();
                int[][] placement = new int[size][size];
                int[][] values = new int[size][size];
                for (int i = 0; i < size; i++) {
                    String line = br.readLine();
                    for (int j = 0; j < size; j++) {
                        String[] column = line.split(" ");
                        placement[i][j] = Integer.parseInt(column[j]) - 1;
                    }
                }

                br.readLine();
                for (int i = 0; i < size; i++) {
                    String line = br.readLine();
                    for (int j = 0; j < size; j++) {
                        String[] column = line.split(" ");
                        values[i][j] = Integer.parseInt(column[j]) - 1;
                    }
                }

                HashMap<Integer, String> symbols = null;
                if (hasSymbols) {
                    br.readLine();
                    symbols = new HashMap<>();
                    for (int i = 0; i < size; i++) {
                        symbols.put(i, br.readLine());
                    }
                }

                Sudoku sudoku = new Sudoku(size, placement, symbols);
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        sudoku.getCase(i, j).setValeur(values[i][j]);
                    }
                }
                return sudoku;
            }
            else {
                throw new IllegalArgumentException("Not a sudoku file");
            }
        }
        catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
        return null;
    }
}
