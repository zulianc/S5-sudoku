import java.io.File;
import java.util.Scanner;

public abstract class Menu {
    public static void startMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        boolean stop = false;
        while (!stop) {
            System.out.println("----------");
            choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.println("MENU PRINCIPAL");
                System.out.println("1. Créer un sudoku");
                System.out.println("2. Résoudre un sudoku");
                System.out.println("3. Quitter");
                System.out.print("Choix : ");
                choice = sc.nextInt();
            }

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
        Scanner sc = new Scanner(System.in);
        int choice;
        Sudoku sudoku = creerSudoku();
        askToSaveSudoku(sudoku);
        choice = 0;
        System.out.println("----------");
        while (choice < 1 || choice > 2) {
            System.out.println("1. Résoudre ce sudoku");
            System.out.println("2. Quitter");
            System.out.print("Choix : ");
            choice = sc.nextInt();
        }
        if (choice == 1) {
            solveSudoku(sudoku);
            askToSaveSudoku(sudoku);
        }
    }

    private static void solveSudokuSubMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("----------");
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.println("1. Créer un sudoku et le résoudre");
            System.out.println("2. Importer un sudoku et le résoudre");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            choice = sc.nextInt();
        }

        Sudoku sudoku = null;
        if (choice == 1) {
            sudoku = creerSudoku();
        }
        if (choice == 2) {
            String filepath = askWhichFileToImport(false);
            if (filepath == null) {
                return;
            }
            sudoku = FilesOperations.readSudokuFromFile(filepath);
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

    private static Sudoku creerSudoku() {
        Scanner sc = new Scanner(System.in);

        System.out.println("----------");
        System.out.println("Veuillez remplir les informations du sudoku");
        System.out.print("Taille du sudoku : ");
        int size = sc.nextInt();
        int[][] values = new int[size][size];
        int[][] placements = new int[size][size];

        System.out.println("Rentrez 0 si la case est vide");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = -1;
                while (val < 0 || val > size) {
                    System.out.print("Valeur de la case ligne " + (i+1) + " colonne " + (j+1) + " : ");
                    val = sc.nextInt();
                }
                values[i][j] = val - 1;

                val = -1;
                while (val < 1 || val > size) {
                    System.out.print("Bloc auquel appartient la case : ");
                    val = sc.nextInt();
                }
                placements[i][j] = val - 1;
            }
        }

        Sudoku sudoku = new Sudoku(size, placements);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sudoku.getCase(i, j).setValeur(values[i][j]);
            }
        }

        System.out.println("Sudoku créé : ");
        System.out.println(sudoku);

        return sudoku;
    }

    private static void solveSudoku(Sudoku sudoku) {
        System.out.println("----------");
        Solver solver = new Solver(sudoku, null);
        boolean solved = solver.solve();
        if (solved) {
            System.out.println("Le sudoku a été résolu !");
        } else {
            System.out.println("Le sudoku n'est pas résolvable !");
        }
        System.out.println(sudoku);
    }

    private static void askToSaveSudoku(Sudoku sudoku) {
        Scanner sc = new Scanner(System.in);

        int choice = 0;
        System.out.println("----------");
        System.out.println("Enregistrer ce sudoku dans un fichier ?");
        while (choice < 1 || choice > 2) {
            System.out.println("1. Oui");
            System.out.println("2. Non");
            System.out.print("Choix : ");
            choice = sc.nextInt();
        }
        if (choice == 1) {
            FilesOperations.convertSudokuToFile(sudoku);
            System.out.println("Sudoku enregistré !");
        }
    }

    private static String askWhichFileToImport(boolean isMultidoku) {
        Scanner sc = new Scanner(System.in);
        String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
        File puzzleDirectory = new File(directoryPath);
        File[] puzzles = puzzleDirectory.listFiles();

        System.out.println("----------");
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

        int choice = 0;
        while (choice < 1 || choice > validFilesCount) {
            System.out.println("Importer quel fichier ?");
            System.out.print("Choix : ");
            choice = sc.nextInt();
        }

        return directoryPath + "/" + validFiles[choice - 1];
    }
}
