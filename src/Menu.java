import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * La classe qui gère l'interface entre l'utilisateur et l'application
 */
public abstract class Menu {
    /**
     * L'objet qui sert à lire les input de l'utilisateur
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * La méthode à appeler pour démarrer le menu, propose les grands choix du menu
     */
    public static void startMenu() {
        System.out.print("\033[38;2;34;167;240m");
        System.out.println("----------\nBIENVENUE DANS L'APPLICATION DE SUDOKUS !\n----------\n");
        System.out.print("\033[38;2;255;255;255m");

        int choice;
        boolean stop = false;
        while (!stop) {
            System.out.println("----------");
            do {
                System.out.println("MENU PRINCIPAL");
                System.out.println("1. Afficher une grille enregistrée");
                System.out.println("2. Créer une grille");
                System.out.println("3. Résoudre une grille");
                System.out.println("4. Générer une grille résolue");
                System.out.println("5. Générer une grille à résoudre");
                System.out.println("6. Quitter");
                System.out.print("Choix : ");
                choice = getIntFromUser(false);
            } while (choice < 1 || choice > 6);

            switch (choice) {
                case 1: {
                    seePuzzleSubMenu();
                    break;
                }
                case 2: {
                    createPuzzleSubMenu();
                    break;
                }
                case 3: {
                    solvePuzzleSubMenu();
                    break;
                }
                case 4: {
                    generateSolvedPuzzleSubMenu();
                    break;
                }
                case 5: {
                    generatePuzzleToSolveSubMenu();
                    break;
                }
                case 6: {
                    stop = true;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    /**
     * Un des sous-menus principaux, qui permet de voir un des puzzles enregistré
     */
    private static void seePuzzleSubMenu() {
        System.out.println("----------");
        System.out.println("Quel type de grille enregistrée voir ?");
        int choice = askSudokuOrMultidoku();

        String filename = askWhichFileToImport((choice == 2));

        if (filename != null) {
            Puzzle puzzle = null;
            if (choice == 1) {
                puzzle = FilesOperations.readSudokuFromFile(filename);
            }
            if (choice == 2) {
                puzzle = FilesOperations.readMultidokuFromFile(filename);
            }
            if (puzzle != null) {
                System.out.println(puzzle);
            }
        }
    }

    /**
     * Un des sous-menus principaux, qui permet de créer un nouveau puzzle
     */
    private static void createPuzzleSubMenu() {
        System.out.println("----------");
        System.out.println("Quel type de grille créer ?");
        int choice = askSudokuOrMultidoku();

        Puzzle puzzle = null;
        if (choice == 1) {
            puzzle = createSudoku();
        }
        if (choice == 2) {
            puzzle = createMultidoku();
        }
        if (puzzle == null) {
            return;
        }
        askToSavePuzzle(puzzle);

        System.out.println("----------");
        System.out.println("Voulez-vous résoudre cette grille ?");
        choice = askYesOrNo();

        if (choice == 1) {
            solvePuzzle(puzzle);
            askToSavePuzzle(puzzle);
        }
    }

    /**
     * Un des sous-menus principaux, qui permet de résoudre un puzzle
     */
    private static void solvePuzzleSubMenu() {
        System.out.println("----------");
        int choice;
        do {
            System.out.println("1. Créer une grille et la résoudre");
            System.out.println("2. Importer une grille et la résoudre");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 2);

        Puzzle puzzle = null;
        if (choice == 1) {
            System.out.println("----------");
            System.out.println("Quel type de grille créer ?");
            choice = askSudokuOrMultidoku();
            if (choice == 1) {
                puzzle = createSudoku();
            }
            if (choice == 2) {
                puzzle = createMultidoku();
            }
            if (puzzle == null) {
                return;
            }
            askToSavePuzzle(puzzle);
        }
        if (choice == 2) {
            System.out.println("----------");
            System.out.println("Quel type de grille importer ?");
            choice = askSudokuOrMultidoku();
            String filename = askWhichFileToImport((choice == 2));
            if (filename == null) {
                return;
            }
            if (choice == 1) {
                puzzle = FilesOperations.readSudokuFromFile(filename);
            }
            if (choice == 2) {
                puzzle = FilesOperations.readMultidokuFromFile(filename);
            }
            if (puzzle == null) {
                return;
            }
            System.out.println("Grille importée : ");
            System.out.println(puzzle);
        }

        solvePuzzle(puzzle);
        askToSavePuzzle(puzzle);
    }

    /**
     * Un des sous-menus principaux, qui permet de générer un puzzle résolu
     */
    public static void generateSolvedPuzzleSubMenu() {
        //TODO
        System.out.println("Pas encore implémenté");
    }

    /**
     * Un des sous-menus principaux, qui permet de générer un puzzle à résoudre
     */
    public static void generatePuzzleToSolveSubMenu() {
        //TODO
        System.out.println("Pas encore implémenté");
    }

    /**
     * Demande à l'utilisateur de créer un sudoku et le renvoie
     * @return Le sudoku créé par l'utilisateur
     */
    private static Sudoku createSudoku() {
        System.out.println("----------");
        System.out.println("Veuillez remplir les informations du sudoku");

        System.out.println("Attention, il est très fastidieux de créer un sudoku à la main dès 8 ou 9 de taille !");
        System.out.print("Taille du sudoku : ");
        int size = getIntFromUser(false);

        int[][] values = new int[size][size];
        int[][] placements = new int[size][size];

        System.out.println("Voulez-vous spécifier la position des blocs ?");
        int input = askYesOrNo();
        boolean hasCustomPlacement = (input == 1);

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

            System.out.println("Voulez-vous spécifier les symboles utilisés ?");
            input = askYesOrNo();
            if ((input == 1)) {
                HashMap<Integer, String> symbols = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    System.out.print("Veuillez rentrer le symbole pour le chiffre " + (i+1) + " : ");
                    String symbol = scanner.nextLine();
                    symbols.put(i, symbol);
                }
                sudoku.setSymbols(symbols);
            }

            System.out.println("Voulez-vous spécifier des règles supplémentaires ?");
            input = askYesOrNo();
            if (input == 1) {
                ArrayList<SudokuConstraint> constraints = getCustomRules(sudoku);
                sudoku.setAddedConstraints(constraints);
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Le sudoku créé n'est pas valide !");
            return null;
        }

        System.out.println("Sudoku créé : ");
        System.out.println(sudoku);

        return sudoku;
    }

    /**
     * Demande à l'utilisateur de créer un multidoku et le renvoie
     * @return Le sudoku créé par l'utilisateur
     */
    private static Multidoku createMultidoku() {
        //TODO
        System.out.println("Pas encore implémenté");
        return null;
    }

    /**
     * Résout un puzzle passé en paramètre, en demandant à l'utilisateur l'algo utilisé et les règles supplémentaires
     * @param puzzle Le puzzle à résoudre
     */
    private static void solvePuzzle(Puzzle puzzle) {
        System.out.println("----------");
        int choice;
        do {
            System.out.println("1. Résoudre avec simplement les contraintes");
            System.out.println("2. Résoudre avec le backtracking");
            System.out.println("3. Résoudre avec l'algo mixte");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 3);

        ArrayList<SudokuConstraint> constraints = null;
        System.out.println("----------");
        System.out.println("Voulez-vous spécifier des règles supplémentaires ?");
        int choiceConstraints = askYesOrNo();
        if (choiceConstraints == 1) {
            constraints = getCustomRules(puzzle);
        }

        boolean solved = false;
        if (choice == 1) {
            solved = Solver.solveWithConstraints(puzzle, constraints);
        }
        if (choice == 2) {
            solved = Solver.solveWithBacktracking(puzzle, constraints);
        }
        if (choice == 3) {
            solved = Solver.solveWithBoth(puzzle, constraints);
        }

        System.out.println("----------");
        if (solved) {
            System.out.println("La grille a été résolue !");
        } else {
            System.out.println("La grille n'a pas pu être résolue !");
        }
        System.out.println(puzzle);
    }

    /**
     * Demande à l'utilisateur de spécifier des règles de résolutions supplémentaires sur un puzzle
     * @param puzzle Le puzzle sur lequel appliquer des règles supplémentaires pour la résolution
     * @return Une liste de contraintes sur ce puzzle
     */
    private static ArrayList<SudokuConstraint> getCustomRules(Puzzle puzzle) {
        ArrayList<SudokuConstraint> constraints = new ArrayList<>();
        boolean stop = false;
        while (!stop) {
            System.out.print("Rentrez une nouvelle contrainte : ");
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                System.out.println("Une contrainte ne peut pas être vide !");
            }
            else {
                String[] elements = line.split(" ");
                Case caseHasContraint = null;
                ArrayList<Case> casesToCompareTo = new ArrayList<>();
                boolean validFormat = true;
                if (elements.length > 2 && elements.length % 2 == 1 && puzzle instanceof Sudoku) {
                    caseHasContraint = ((Sudoku) puzzle).getCase(Integer.parseInt(elements[1]) - 1, Integer.parseInt(elements[2]) - 1);
                    for (int i = 3; i < elements.length; i += 2) {
                        casesToCompareTo.add(((Sudoku) puzzle).getCase(Integer.parseInt(elements[i]) - 1, Integer.parseInt(elements[i+1]) - 1));
                    }
                }
                else if (elements.length > 4 && elements.length % 4 == 1 && puzzle instanceof Multidoku) {
                    caseHasContraint = ((Multidoku) puzzle).getSudoku(Integer.parseInt(elements[1]) - 1, Integer.parseInt(elements[2]) - 1).sudoku().getCase(Integer.parseInt(elements[3]) - 1, Integer.parseInt(elements[4]) - 1);
                    for (int i = 5; i < elements.length; i += 4) {
                        casesToCompareTo.add(((Multidoku) puzzle).getSudoku(Integer.parseInt(elements[i]) - 1, Integer.parseInt(elements[i+1]) - 1).sudoku().getCase(Integer.parseInt(elements[i+2]) - 1, Integer.parseInt(elements[i+3]) - 1));
                    }
                }
                else {
                    System.out.println("La contrainte ne respecte pas un format connu !");
                    validFormat = false;
                }

                SudokuConstraint constraint = null;
                if (validFormat) {
                    if (elements[0].equals("!=")) {
                        constraint = new NotEqualConstraint(caseHasContraint, casesToCompareTo, puzzle);
                    }
                    else if (elements[0].equals("=")) {
                        constraint = new EqualConstraint(caseHasContraint, casesToCompareTo, puzzle);
                    }
                    if (constraint != null && constraint.isConstraintOnPuzzle(puzzle)) {
                        constraints.add(constraint);
                    }
                }
            }

            System.out.println("Ajouter une autre contrainte ? ");
            int choice = askYesOrNo();
            stop = (choice == 2);
        }
        return constraints;
    }

    /**
     * Demande à l'utilisateur s'il veut sauvegarder le puzzle passé en paramètre dans un fichier
     * @param puzzle Le puzzle à sauvegarder dans un fichier
     */
    private static void askToSavePuzzle(Puzzle puzzle) {
        System.out.println("----------");
        System.out.println("Enregistrer cette grille dans un fichier ?");
        int choice = askYesOrNo();
        if (choice == 1) {
            String filename = askNewFileName((puzzle instanceof Multidoku));
            if (puzzle instanceof Sudoku) {
                FilesOperations.convertSudokuToFile((Sudoku) puzzle, filename);
            }
            else if (puzzle instanceof Multidoku) {
                FilesOperations.convertMultidokuToFile((Multidoku) puzzle, filename);
            }
            else {
                System.out.println("Type de grille inconnue !");
                return;
            }
            System.out.println("Grille enregistrée !");
        }
    }

    /**
     * Demande à l'utilisateur quel fichier importer
     * @param isMultidoku Si c'est un fichier de multidoku ou de sudoku
     * @return Le nom du fichier choisi par l'utilisateur
     */
    private static String askWhichFileToImport(boolean isMultidoku) {
        String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
        File puzzleDirectory = new File(directoryPath);
        File[] puzzles = puzzleDirectory.listFiles();
        
        int validFilesCount = 0;
        String[] validFiles;
        System.out.println("----------");
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
                validFiles[validFilesCount - 1] = puzzle.getName().replace(".txt", "");
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

    /**
     * Demande à l'utilisateur un nom de fichier pour stocker un puzzle
     * @param isMultidoku Si c'est un fichier de multidoku ou de sudoku
     * @return Le nom du fichier spécifié par l'utilisateur
     */
    private static String askNewFileName(boolean isMultidoku) {
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

        System.out.println("----------");
        String filename = null;
        boolean fileIsOK = false;
        while (!fileIsOK) {
            System.out.println("Comment voulez-vous appeler le fichier ?");
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

    /**
     * Demande un nombre entier à l'utilisateur, jusqu'à ce qu'il en donne un
     * @param convertNothingToZero Si le fait que l'utilisateur ne rentre rien doit être interprété comme un 0
     * @return L'entier rentré par l'utilisateur
     */
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

    /**
     * Demande à l'utilisateur un choix entre "Oui" ou "Non"
     * @return 1 si l'utilisateur répond "Oui", 2 si "Non"
     */
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

    /**
     * Demande à l'utilisateur un choix entre "Sudoku" ou "Multidoku"
     * @return 1 si l'utilisateur répond "Sudoku", 2 si "Multidoku"
     */
    private static int askSudokuOrMultidoku() {
        int choice;
        do {
            System.out.println("1. Sudoku");
            System.out.println("2. Multidoku");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 2);
        return choice;
    }
}
