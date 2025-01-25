import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public abstract class FilesOperations {
    public static void convertSudokuToFile(Sudoku sudoku, String filename) {
        try {
            boolean hasSymbols = !(sudoku.getSymboles() == null);

            String filepath = "./data/sudokus/" + filename + ".txt";
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.append("puzzleType:\n").append("sudoku\n");
            bw.append("hasSymbols:\n").append(Boolean.toString(hasSymbols)).append("\n");
            bw.append("size:\n").append(Integer.toString(sudoku.getTaille())).append("\n");

            bw.append("placements:\n");
            for (int i = 0; i < sudoku.getTaille(); i++) {
                for (int j = 0; j < sudoku.getTaille(); j++) {
                    bw.append(Integer.toString(sudoku.getCase(i, j).getBlocIndex() + 1)).append(" ");
                }
                bw.append("\n");
            }

            bw.append("values:\n");
            for (int i = 0; i < sudoku.getTaille(); i++) {
                for (int j = 0; j < sudoku.getTaille(); j++) {
                    bw.append(Integer.toString(sudoku.getCase(i, j).getValeur() + 1)).append(" ");
                }
                bw.append("\n");
            }

            if (hasSymbols) {
                bw.append("symbols:\n");
                for (int i = 0; i < sudoku.getTaille(); i++) {
                    bw.append(sudoku.getSymboles().get(i)).append("\n");
                }
            }

            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
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

                br.close();
                fr.close();
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
