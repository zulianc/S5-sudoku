import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Une classe qui procure des méthodes pour gérer le stockage des puzzles dans des fichiers
 */
public abstract class FilesOperations {
    /**
     * Prend un sudoku en entrée et le stocke dans un fichier
     * @param sudoku Le sudoku à stocker
     * @param filename Le nom du fichier où stocker le sudoku
     */
    public static void convertSudokuToFile(Sudoku sudoku, String filename) {
        String filepath = "./data/sudokus/" + filename + ".txt";
        try {
            // ouverture du fichier (ou création s'il n'existait pas)
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);

            // on récupère ce qu'on doit écrire dans le fichier
            convertSudoku(sudoku, bw);

            // on écrit dans le fichier
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans le fichier " + filepath);
        }
    }

    /**
     * Prend un multidoku en entrée et le stocke dans un fichier
     * @param multidoku Le multidoku à stocker
     * @param filename Le nom du fichier où stocker le multidoku
     */
    public static void convertMultidokuToFile(Multidoku multidoku, String filename) {
        //TODO
    }

    /**
     * Ajoute un sudoku à la suite dans un BufferedWriter
     * @param sudoku Le sudoku à convertir
     * @param bw Le BufferedWriter où écrire
     * @throws IOException S'il y a une erreur lors de l'écriture
     */
    private static void convertSudoku(Sudoku sudoku, BufferedWriter bw) throws IOException {
        boolean useCustomPlacements = !(sudoku.isUsingDefaultPlacements());
        boolean useCustomSymbols = (sudoku.getSymbols() != null);

        // on remplit les informations de bases sur le sudoku
        bw.append("puzzleType:\n").append("sudoku\n");
        bw.append("useCustomPlacements:\n").append(Boolean.toString(useCustomPlacements)).append("\n");
        bw.append("useCustomSymbols:\n").append(Boolean.toString(useCustomSymbols)).append("\n");
        bw.append("size:\n").append(Integer.toString(sudoku.getSize())).append("\n");

        // on remplit les valeurs des cases du sudoku
        bw.append("values:\n");
        for (int i = 0; i < sudoku.getSize(); i++) {
            for (int j = 0; j < sudoku.getSize(); j++) {
                bw.append(Integer.toString(sudoku.getCase(i, j).getValue() + 1)).append(" ");
            }
            bw.append("\n");
        }

        // on remplit les placements des blocs du sudoku, s'ils ont été spécifiés
        bw.append("placements:\n");
        if (useCustomPlacements) {
            for (int i = 0; i < sudoku.getSize(); i++) {
                for (int j = 0; j < sudoku.getSize(); j++) {
                    bw.append(Integer.toString(sudoku.getCase(i, j).getBlocIndex() + 1)).append(" ");
                }
                bw.append("\n");
            }
        }

        // on remplit les symboles utilisés par le sudoku, s'ils ont été spécifiés
        bw.append("symbols:\n");
        if (useCustomSymbols) {
            for (int i = 0; i < sudoku.getSize(); i++) {
                bw.append(sudoku.getSymbols().get(i)).append("\n");
            }
        }

        // on remplit les contraintes additionnelles sur le sudoku
        bw.append("additionalConstraints:\n");
        for (SudokuConstraint constraint : sudoku.getAddedConstraints()) {
            bw.append(constraint.toString()).append("\n");
        }
        bw.append("end\n");
    }

    /**
     * Lit un sudoku depuis un fichier et renvoie une nouvelle instance de ce sudoku
     * @param filename Le fichier dans lequel est stocké le sudoku
     * @return Le sudoku créé depuis le fichier
     */
    public static Sudoku readSudokuFromFile(String filename) {
        String filepath = "./data/sudokus/" + filename + ".txt";
        try {
            // ouverture du fichier
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            // on vérifie que c'est bien un fichier de sudoku
            br.readLine();
            String puzzleType = br.readLine();
            if (!puzzleType.equals("sudoku")) {
                throw new IllegalArgumentException("Le fichier " + filepath + " n'est pas un fichier de sudoku");
            }

            // on lit le sudoku
            Sudoku sudoku = readSudoku(br);

            br.close();
            fr.close();
            return sudoku;
        }
        catch (IOException e) {
            System.out.println("Erreur lors de la lecture dans le fichier " + filepath);
            return null;
        }
        catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la création du sudoku contenu dans le fichier " + filepath);
            return null;
        }
    }

    /**
     * Lit un multidoku depuis un fichier et renvoie une nouvelle instance de ce multidoku
     * @param filename Le fichier dans lequel est stocké le multidoku
     * @return Le multidoku créé depuis le fichier
     */
    public static Multidoku readMultidokuFromFile(String filename) {
        //TODO
        return null;
    }

    /**
     * Lit un sudoku à la suite de la position d'un BufferedReader
     * @param br Le BufferedReader en train de lire un sudoku
     * @return Le sudoku lu
     * @throws IOException S'il y a une erreur lors de la lecture
     * @throws IllegalArgumentException S'il y a une erreur lors de la création du sudoku
     */
    private static Sudoku readSudoku(BufferedReader br) throws IOException, IllegalArgumentException {
        // on lit les informations de base sur le sudoku
        br.readLine();
        boolean hasCustomPlacements = Boolean.parseBoolean(br.readLine());
        br.readLine();
        boolean hasCustomSymbols = Boolean.parseBoolean(br.readLine());
        br.readLine();
        int size = Integer.parseInt(br.readLine());

        int[][] placements = new int[size][size];
        int[][] values = new int[size][size];

        // on lit les valeurs des cases du sudoku
        br.readLine();
        for (int i = 0; i < size; i++) {
            String line = br.readLine();
            String[] column = line.split(" ");
            for (int j = 0; j < size; j++) {
                values[i][j] = Integer.parseInt(column[j]) - 1;
            }
        }

        // on lit les placements des blocs du sudoku, s'ils sont spécifiés
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

        // on lit les symboles du sudoku, s'ils sont spécifiés
        br.readLine();
        HashMap<Integer, String> symbols = null;
        if (hasCustomSymbols) {
            symbols = new HashMap<>();
            for (int i = 0; i < size; i++) {
                symbols.put(i, br.readLine());
            }
        }

        // on crée le sudoku
        Sudoku sudoku = new Sudoku(size, placements, symbols);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (values[i][j] != -1)
                    sudoku.getCase(i, j).setValue(values[i][j]);
            }
        }

        // on lit les contraintes additionnelles
        br.readLine();
        String line;
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        do {
            line = br.readLine();
            if (line.isEmpty()) {
                throw new IOException("Une ligne de contrainte est vide !");
            }
            if (!line.equals("end")) {
                String[] elements = line.split(" ");
                if (elements.length < 3 || elements.length % 2 != 1) {
                    throw new IOException("Une ligne de contrainte ne respecte pas le bon format : " + line);
                }
                Case caseHasContraint = sudoku.getCase(Integer.parseInt(elements[1]) - 1, Integer.parseInt(elements[2]) - 1);
                ArrayList<Case> casesToCompareTo = new ArrayList<>();
                for (int i = 3; i < elements.length; i += 2) {
                    casesToCompareTo.add(sudoku.getCase(Integer.parseInt(elements[i]) - 1, Integer.parseInt(elements[i+1]) - 1));
                }
                if (elements[0].equals("!=")) {
                    constraints.add(new NotEqualConstraint(caseHasContraint, casesToCompareTo, sudoku));
                }
                else if (elements[0].equals("=")) {
                    constraints.add(new EqualConstraint(caseHasContraint, casesToCompareTo, sudoku));
                }
                else {
                    throw new IOException("Type de contrainte inconnu : " + elements[0]);
                }
            }
        } while (!line.equals("end"));
        sudoku.setAddedConstraints(constraints);

        return sudoku;
    }
}
