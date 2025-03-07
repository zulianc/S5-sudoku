package Operations;

import Constraints.*;
import Grids.*;

import java.io.*;
import java.time.LocalDateTime;
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
     * @throws IOException S'il y a une erreur lors de l'écriture dans le fichier
     * @throws IllegalArgumentException Si les données du sudoku sont invalides
     */
    public static void convertSudokuToFile(Sudoku sudoku, String filename) throws IOException, IllegalArgumentException {
        // ouverture du fichier (ou création s'il n'existait pas)
        String filepath = "./data/sudokus/" + filename + ".txt";
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // on récupère ce qu'on doit écrire dans le fichier
        convertSudoku(sudoku, bw);

        // on écrit dans le fichier
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * Prend un multidoku en entrée et le stocke dans un fichier
     * @param multidoku Le multidoku à stocker
     * @param filename Le nom du fichier où stocker le multidoku
     * @throws IOException S'il y a une erreur lors de l'écriture dans le fichier
     * @throws IllegalArgumentException Si les données du multidoku sont invalides
     */
    public static void convertMultidokuToFile(Multidoku multidoku, String filename) throws IOException, IllegalArgumentException {

        // ouverture du fichier (ou création s'il n'existait pas)
        String filepath = "./data/multidokus/" + filename + ".txt";
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // on écrit les informations de base du multidoku
        boolean useCustomSymbols = (multidoku.getSymbols() != null);
        bw.append("puzzleType:\nmultidoku\n");
        bw.append("useCustomSymbols:\n").append(Boolean.toString(useCustomSymbols)).append("\n");
        bw.append("sudokusCount:\n").append(Integer.toString(multidoku.getSudokus().size())).append("\n");

        // on écrit les informations pour chaque sudoku
        for (PlacedSudoku placedSudoku : multidoku.getSudokus()) {
            bw.append("sudokuLine:\n").append(Integer.toString(placedSudoku.line() + 1)).append("\n");
            bw.append("sudokuColumn:\n").append(Integer.toString(placedSudoku.column() + 1)).append("\n");
            convertSudoku(placedSudoku.sudoku(), bw);
        }

        // on remplit les symboles utilisés par le multidoku, s'ils ont été spécifiés
        bw.append("symbols:\n");
        if (useCustomSymbols) {
            for (int i = 0; i < multidoku.getSizeSudokus(); i++) {
                bw.append(multidoku.getSymbols().get(i)).append("\n");
            }
        }

        // on remplit les contraintes additionnelles sur le sudoku
        bw.append("additionalConstraints:\n");
        for (SudokuConstraint constraint : multidoku.getAddedConstraints()) {
            bw.append(constraint.toString()).append("\n");
        }
        bw.append("end\n");

        // on écrit dans le fichier
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * Ajoute un sudoku à la suite dans un BufferedWriter
     * @param sudoku Le sudoku à convertir
     * @param bw Le BufferedWriter où écrire
     * @throws IOException S'il y a une erreur lors de l'écriture
     * @throws IllegalArgumentException Si les données du sudoku sont invalides
     */
    private static void convertSudoku(Sudoku sudoku, BufferedWriter bw) throws IOException, IllegalArgumentException {
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
     * @throws IOException S'il y a une erreur lors de la lecture
     * @throws RuntimeException S'il y a une erreur lors de la création du sudoku
     */
    public static Sudoku readSudokuFromFile(String filename) throws IOException, IllegalArgumentException {
        // ouverture du fichier
        String filepath = "./data/sudokus/" + filename + ".txt";
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

        // fermeture du fichier
        br.close();
        fr.close();
        return sudoku;
    }

    /**
     * Lit un multidoku depuis un fichier et renvoie une nouvelle instance de ce multidoku
     * @param filename Le fichier dans lequel est stocké le multidoku
     * @return Le multidoku créé depuis le fichier
     * @throws IOException S'il y a une erreur lors de la lecture
     * @throws RuntimeException S'il y a une erreur lors de la création du multidoku
     */
    public static Multidoku readMultidokuFromFile(String filename) throws IOException, RuntimeException {
        // ouverture du fichier
        String filepath = "./data/multidokus/" + filename + ".txt";
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);

        // on vérifie que c'est bien un fichier de sudoku
        br.readLine();
        String puzzleType = br.readLine();
        if (!puzzleType.equals("multidoku")) {
            throw new IllegalArgumentException("Le fichier " + filepath + " n'est pas un fichier de multidoku");
        }

        // on lit les informations de base sur le multidoku
        br.readLine();
        boolean hasCustomSymbols = Boolean.parseBoolean(br.readLine());
        br.readLine();
        int sudokusCount = Integer.parseInt(br.readLine());

        // on lit les informations pour chaque sudoku
        ArrayList<PlacedSudoku> placedSudokus = new ArrayList<>();
        for (int i = 0; i < sudokusCount; i++) {
            br.readLine();
            int sudokuLine = Integer.parseInt(br.readLine()) - 1;
            br.readLine();
            int sudokuColumn = Integer.parseInt(br.readLine()) - 1;
            br.readLine();
            puzzleType = br.readLine();
            if (!puzzleType.equals("sudoku")) {
                throw new IllegalArgumentException("Le sudoku numéro " + (i + 1) + " dans " + filepath + " n'est pas un fichier de sudoku");
            }
            Sudoku sudoku = readSudoku(br);
            placedSudokus.add(new PlacedSudoku(sudoku, sudokuLine, sudokuColumn));
        }

        // on lit les symboles du multidoku, s'ils sont spécifiés
        br.readLine();
        HashMap<Integer, String> symbols = null;
        if (hasCustomSymbols) {
            symbols = new HashMap<>();
            for (int i = 0; i < placedSudokus.getFirst().sudoku().getSize(); i++) {
                symbols.put(i, br.readLine());
            }
        }

        // on crée le multidoku
        Multidoku multidoku = new Multidoku(placedSudokus, symbols);

        // on lit les contraintes additionnelles
        br.readLine();
        String line;
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        do {
            line = br.readLine();
            if (!line.equals("end"))
                constraints.add(readConstraint(line, multidoku));
        } while (!line.equals("end"));
        multidoku.setAddedConstraints(constraints);

        // fermeture du fichier
        br.close();
        fr.close();
        return multidoku;
    }

    /**
     * Lit un sudoku à la suite de la position d'un BufferedReader
     * @param br Le BufferedReader en train de lire un sudoku
     * @return Le sudoku lu
     * @throws IOException S'il y a une erreur lors de la lecture
     * @throws RuntimeException S'il y a une erreur lors de la création du sudoku
     */
    private static Sudoku readSudoku(BufferedReader br) throws IOException, RuntimeException {
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
            if (!line.equals("end"))
                constraints.add(readConstraint(line, sudoku));
        } while (!line.equals("end"));
        sudoku.setAddedConstraints(constraints);

        return sudoku;
    }

    /**
     * Lit une contrainte contenue dans un String et la crée
     * @param line Le string qui contient la contrainte
     * @param puzzle Le puzzle sur lequel s'applique la contrainte
     * @return La contrainte contenue dans le string
     * @throws RuntimeException Si une erreur se produit
     */
    public static SudokuConstraint readConstraint(String line, Puzzle puzzle) throws RuntimeException {
        // on teste si la ligne est vide
        if (line.isEmpty()) throw new RuntimeException("Une contrainte ne peut pas être vide !");

        String[] elements = line.split(" ");
        Case caseHasContraint;
        ArrayList<Case> casesToCompareTo = new ArrayList<>();

        // si c'est une contrainte de sudoku
        if (elements.length > 2 && elements.length % 2 == 1 && puzzle instanceof Sudoku) {
            caseHasContraint = ((Sudoku) puzzle).getCase(Integer.parseInt(elements[1]) - 1, Integer.parseInt(elements[2]) - 1);
            for (int i = 3; i < elements.length; i += 2) {
                casesToCompareTo.add(((Sudoku) puzzle).getCase(Integer.parseInt(elements[i]) - 1, Integer.parseInt(elements[i+1]) - 1));
            }
        }

        // si c'est une contrainte du multidoku
        else if (elements.length > 4 && elements.length % 4 == 1 && puzzle instanceof Multidoku) {
            caseHasContraint = ((Multidoku) puzzle).getSudoku(Integer.parseInt(elements[1]) - 1, Integer.parseInt(elements[2]) - 1).sudoku().getCase(Integer.parseInt(elements[3]) - 1, Integer.parseInt(elements[4]) - 1);
            for (int i = 5; i < elements.length; i += 4) {
                casesToCompareTo.add(((Multidoku) puzzle).getSudoku(Integer.parseInt(elements[i]) - 1, Integer.parseInt(elements[i+1]) - 1).sudoku().getCase(Integer.parseInt(elements[i+2]) - 1, Integer.parseInt(elements[i+3]) - 1));
            }
        }

        // sinon ce n'est pas implémenté ou erroné
        else {
            throw new RuntimeException("La contrainte ne respecte pas un format connu !");
        }

        // on crée la contrainte
        SudokuConstraint constraint = null;
        if (elements[0].equals("!=")) {
            constraint = new NotEqualConstraint(caseHasContraint, casesToCompareTo, puzzle);
        }
        else if (elements[0].equals("=")) {
            constraint = new EqualConstraint(caseHasContraint, casesToCompareTo, puzzle);
        }
        if (constraint != null && constraint.isConstraintOnPuzzle(puzzle)) {
            return constraint;
        }
        return null;
    }

    /**
     * Créer un nouveau fichier de logs à la date du jour
     * @param logs La liste de logs à ajouter au fichier
     * @param algorithm Le nom de l'algorithme utilisé qui a créé les logs
     * @param puzzle Le puzzle sur lequel l'algorithme a été effectué
     * @return Le nom du fichier de logs créé
     * @throws RuntimeException Si le puzzle n'est pas pris en charge
     * @throws Exception S'il y a une erreur lors de l'écriture dans le fichier
     */
    public static String createNewLogFile(ArrayList<String> logs, String algorithm, Puzzle puzzle) throws RuntimeException, Exception {
        // on crée le nom du fichier à partir de la date actuelle
        LocalDateTime date = java.time.LocalDateTime.now();
        String filepath = "./data/logs/";
        StringBuilder filename = new StringBuilder();
        filename.append(date.getYear()).append("_").append(date.getMonthValue()).append("_").append(date.getDayOfMonth()).append("_").append(date.getHour()).append("_").append(date.getMinute()).append("_").append(date.getSecond());
        while (new File(filepath + filename + ".txt").isFile()) {
            filename.append("_bis");
        }
        filepath = filepath + filename + ".txt";

        // on ouvre le fichier
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // on rajoute le nom de l'algo
        bw.append("algorithm:\n").append(algorithm).append("\n");

        // on rajoute la taille du puzzle
        if (puzzle instanceof Sudoku) {
            bw.append("puzzleType:\nsudoku\n");
            bw.append("size:\n").append(Integer.toString(((Sudoku) puzzle).getSize())).append("\n");
        }
        else if (puzzle instanceof Multidoku) {
            bw.append("puzzleType:\nmultidoku\n");
            bw.append("sudokusSize:\n").append(Integer.toString(((Multidoku) puzzle).getSizeSudokus())).append("\n");
            bw.append("sudokusCount:\n").append(Integer.toString(((Multidoku) puzzle).getSudokus().size())).append("\n");
            for (PlacedSudoku placedSudoku : (((Multidoku) puzzle).getSudokus())) {
                bw.append("sudokuLine:\n").append(Integer.toString(placedSudoku.line() + 1)).append("\n");
                bw.append("sudokuColumn:\n").append(Integer.toString(placedSudoku.column() + 1)).append("\n");
            }
        }
        else {
            throw new RuntimeException("Type de puzzle inconnu");
        }

        // on rajoute les logs
        bw.append("logs:\n");
        for (String line : logs) {
            bw.append(line).append("\n");
        }
        bw.append("end\n");

        // on ferme le fichier
        bw.flush();
        bw.close();
        fw.close();
        return filename.toString();
    }

    /**
     * Affiche la suite de logs d'un fichier dans le terminal
     * @param filename Le nom du fichier de logs
     * @throws IOException S'il y a une erreur lors de la lecture dans le fichier
     */
    public static void printLogs(String filename) throws IOException {
        // on ouvre le fichier
        String filepath = "./data/logs/" + filename + ".txt";
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);

        // on cherche le début des logs
        String line;
        do {
            line = br.readLine();
        } while (!line.equals("logs:"));

        // on affiche tout les logs
        do {
            line = br.readLine();
            if (!line.equals("end")) {
                System.out.println(line);
            }
        } while (!line.equals("end"));
    }
}
