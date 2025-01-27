import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void startMenu() {
        int choice;
        boolean stop = false;
        while (!stop) {
            System.out.println("----------");
            do {
                System.out.println("MENU PRINCIPAL");
                System.out.println("1. Créer un sudoku");
                System.out.println("2. Résoudre un sudoku");
                System.out.println("3. Quitter");
                System.out.print("Choix : ");
                choice = getIntFromUser(false);
            } while (choice < 1 || choice > 3);

            switch (choice) {
                case 1: {
                    createSudokuSubMenu();
                    break;
                }
                case 2: {
                    solveSudokuSubMenu();
                    break;
                }
                case 3: {
                    stop = true;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private static void createSudokuSubMenu() {
        Sudoku sudoku = createSudoku();
        askToSaveSudoku(sudoku);

        System.out.println("----------");
        int choice;
        do {
            System.out.println("1. Résoudre ce sudoku");
            System.out.println("2. Quitter");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 2);

        if (choice == 1) {
            solveSudoku(sudoku);
            askToSaveSudoku(sudoku);
        }
    }

    private static void solveSudokuSubMenu() {
        System.out.println("----------");
        int choice;
        do {
            System.out.println("1. Créer un sudoku et le résoudre");
            System.out.println("2. Importer un sudoku et le résoudre");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 3);
        if (choice == 3) {
            return;
        }

        Sudoku sudoku = null;
        if (choice == 1) {
            sudoku = createSudoku();
            askToSaveSudoku(sudoku);
        }
        if (choice == 2) {
            String filename = askWhichFileToImport(false);
            if (filename == null) {
                return;
            }
            sudoku = FilesOperations.readSudokuFromFile(filename);
            if (sudoku == null) {
                System.out.println("Erreur lors de la lecture du sudoku !");
                return;
            }
            System.out.println("Sudoku importé : ");
            System.out.println(sudoku);
        }
        solveSudoku(sudoku);
        askToSaveSudoku(sudoku);
    }

    private static Sudoku createSudoku() {
        System.out.println("----------");
        System.out.println("Veuillez remplir les informations du sudoku");

        System.out.print("Taille du sudoku : ");
        int size = getIntFromUser(false);

        int[][] values = new int[size][size];
        int[][] placements = new int[size][size];

        System.out.println("Voulez-vous spécifier la position des blocs ?");
        int input = askYesOrNo();
        boolean hasCustomPlacement = (input == 1);

        System.out.println("Voulez-vous spécifier les symboles utilisés ?");
        input = askYesOrNo();
        boolean hascustomSymbols = (input == 1);

        System.out.println("Si une case est vide, rentrez 0 ou rien");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val;
                do {
                    System.out.print("Valeur de la case à la ligne " + (i+1) + " et colonne " + (j+1) + " : ");
                    val = getIntFromUser(true);
                } while (val < 0 || val > size);
                values[i][j] = val - 1;

                if (hasCustomPlacement) {
                    do {
                        System.out.print("Bloc auquel appartient cette case : ");
                        val = getIntFromUser(false);
                    } while (val < 1 || val > size);
                    placements[i][j] = val - 1;
                }
            }
        }

        if (!hasCustomPlacement) {
            placements = null;
        }

        Sudoku sudoku;
        try {
            sudoku = new Sudoku(size, placements);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (values[i][j] != -1)
                        sudoku.getCase(i, j).setValue(values[i][j]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Le sudoku créé n'est pas valide !");
            return null;
        }

        if (hascustomSymbols) {
            HashMap<Integer, String> symbols = new HashMap<>();
            for (int i = 0; i < size; i++) {
                System.out.print("Veuillez rentrer le symbole pour le chiffre " + (i+1) + " : ");
                String symbol = scanner.nextLine();
                symbols.put(i, symbol);
            }
            sudoku.setSymbols(symbols);
        }

        System.out.println("Sudoku créé : ");
        System.out.println(sudoku);

        return sudoku;
    }

    private static void solveSudoku(Sudoku sudoku) {
        boolean solved = Solver.solve(sudoku, null);

        System.out.println("----------");
        if (solved) {
            System.out.println("Le sudoku a été résolu !");
        } else {
            System.out.println("Le sudoku n'est pas résolvable !");
        }
        System.out.println(sudoku);
    }

    private static void askToSaveSudoku(Sudoku sudoku) {
        System.out.println("----------");
        System.out.println("Enregistrer ce sudoku dans un fichier ?");
        int choice = askYesOrNo();
        if (choice == 1) {
            String filename = askNewFileName(false);
            FilesOperations.convertSudokuToFile(sudoku, filename);
            System.out.println("Sudoku enregistré !");
        }
    }

    private static String askWhichFileToImport(boolean isMultidoku) {
        System.out.println("----------");
        
        String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
        File puzzleDirectory = new File(directoryPath);
        File[] puzzles = puzzleDirectory.listFiles();
        
        int validFilesCount = 0;
        String[] validFiles;
        System.out.println("Fichiers enregistrés : ");
        if (puzzles == null) {
            System.out.println("Pas de fichiers trouvés !");
            return null;
        }

        validFiles = new String[puzzles.length];
        for (File puzzle : puzzles) {
            if (puzzle.getName().endsWith(".txt")) {
                validFilesCount++;
                System.out.println(validFilesCount + ". " + puzzle.getName());
                validFiles[validFilesCount - 1] = puzzle.getName();
            }
        }

        if (validFilesCount == 0) {
            System.out.println("Pas de fichiers trouvés !");
            return null;
        }

        int choice;
        do {
            System.out.println("Importer quel fichier ?");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > validFilesCount);

        return validFiles[choice - 1];
    }

    private static String askNewFileName(boolean isMultidoku) {
        System.out.println("----------");

        String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
        File puzzleDirectory = new File(directoryPath);
        File[] puzzles = puzzleDirectory.listFiles();
        ArrayList<String> filesNames = new ArrayList<>();
        if (puzzles != null) {
            for (File puzzle : puzzles) {
                if (puzzle.getName().endsWith(".txt")) {
                    filesNames.add(puzzle.getName().replace(".txt", ""));
                }
            }
        }

        String filename = null;
        boolean fileIsOK = false;
        while (!fileIsOK) {
            System.out.println("Comment voulez-vous appeller le fichier ?");
            System.out.print("Nom : ");
            filename = scanner.nextLine();

            if (!filename.matches("[A-Za-z0-9_-]+")) {
                System.out.println("Nom invalide !");
            }

            else if (filesNames.contains(filename)) {
                System.out.println("Ce fichier existe déjà, écraser le fichier ?");
                int choice = askYesOrNo();
                fileIsOK = (choice == 1);
            }

            else {
                fileIsOK = true;
            }
        }

        return filename;
    }

    private static int getIntFromUser(boolean convertNothingToZero) {
        int value = -99;
        boolean validInput = false;
        while (!validInput) {
            String input = scanner.nextLine();
            if (convertNothingToZero && input.isEmpty()) {
                value = 0;
                validInput = true;
            }
            else {
                if (input.matches("[0-9]+")) {
                    value = Integer.parseInt(input);
                    validInput = true;
                } else {
                    System.out.print("Veuillez rentrer un nombre : ");
                }
            }
        }
        return value;
    }

    private static int askYesOrNo() {
        int choice;
        do {
            System.out.println("1. Oui");
            System.out.println("2. Non");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 2);
        return choice;
    }
}
