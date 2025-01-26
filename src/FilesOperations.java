import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public abstract class FilesOperations {
    public static void convertSudokuToFile(Sudoku sudoku, String filename) {
        try {
            boolean useCustomPlacements = !(sudoku.isUsingDefaultPlacements());
            boolean useCstomSymbols = (sudoku.getSymbols() != null);

            String filepath = "./data/sudokus/" + filename + ".txt";
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.append("puzzleType:\n").append("sudoku\n");
            bw.append("useCustomPlacements:\n").append(Boolean.toString(useCustomPlacements)).append("\n");
            bw.append("useCustomSymbols:\n").append(Boolean.toString(useCstomSymbols)).append("\n");
            bw.append("size:\n").append(Integer.toString(sudoku.getSize())).append("\n");

            bw.append("values:\n");
            for (int i = 0; i < sudoku.getSize(); i++) {
                for (int j = 0; j < sudoku.getSize(); j++) {
                    bw.append(Integer.toString(sudoku.getCase(i, j).getValeur() + 1)).append(" ");
                }
                bw.append("\n");
            }

            bw.append("placements:\n");
            if (useCustomPlacements) {
                for (int i = 0; i < sudoku.getSize(); i++) {
                    for (int j = 0; j < sudoku.getSize(); j++) {
                        bw.append(Integer.toString(sudoku.getCase(i, j).getBlocIndex() + 1)).append(" ");
                    }
                    bw.append("\n");
                }
            }

            bw.append("symbols:\n");
            if (useCstomSymbols) {
                for (int i = 0; i < sudoku.getSize(); i++) {
                    bw.append(sudoku.getSymbols().get(i)).append("\n");
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
                boolean hasCustomPlacements = Boolean.parseBoolean(br.readLine());
                br.readLine();
                boolean hasCustomSymbols = Boolean.parseBoolean(br.readLine());
                br.readLine();
                int size = Integer.parseInt(br.readLine());

                int[][] placements = new int[size][size];
                int[][] values = new int[size][size];

                br.readLine();
                for (int i = 0; i < size; i++) {
                    String line = br.readLine();
                    String[] column = line.split(" ");
                    for (int j = 0; j < size; j++) {
                        values[i][j] = Integer.parseInt(column[j]) - 1;
                    }
                }

                br.readLine();
                if (hasCustomPlacements) {
                    for (int i = 0; i < size; i++) {
                        String line = br.readLine();
                        String[] column = line.split(" ");
                        for (int j = 0; j < size; j++) {
                            placements[i][j] = Integer.parseInt(column[j]) - 1;
                        }
                    }
                }
                else {
                    placements = null;
                }

                br.readLine();
                HashMap<Integer, String> symbols = null;
                if (hasCustomSymbols) {
                    symbols = new HashMap<>();
                    for (int i = 0; i < size; i++) {
                        symbols.put(i, br.readLine());
                    }
                }

                Sudoku sudoku = new Sudoku(size, placements, symbols);
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
