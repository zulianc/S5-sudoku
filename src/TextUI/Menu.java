package TextUI;

import Constraints.*;
import Grids.*;
import Operations.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

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
        important("----________----_________----________----");
        important("BIENVENUE DANS L'APPLICATION DE SUDOKUS !");
        important("____--------____---------____--------____");
        System.out.println("\n");
        boolean init = true;

        int choice;
        boolean stop = false;
        while (!stop) {
            if (!init) {
                separator();
            }
            init = false;
            do {
                important("MENU PRINCIPAL");
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

        important("~~~ PASSEZ UNE BONNE JOURNÉE ~~~");
    }

    /**
     * Un des sous-menus principaux, qui permet de voir un des puzzles enregistré
     */
    private static void seePuzzleSubMenu() {
        separator();
        important("Quel type de grille enregistrée voir ?");
        int choice = askSudokuOrMultidoku();

        String filename = askWhichFileToImport((choice == 2));

        if (filename != null) {
            importPuzzle();
        }
    }

    /**
     * Un des sous-menus principaux, qui permet de créer un nouveau puzzle
     */
    private static void createPuzzleSubMenu() {
        separator();
        important("Quel type de grille créer ?");
        int choice = askSudokuOrMultidoku();

        Puzzle puzzle = null;
        if (choice == 1) {
            puzzle = createSudoku(false, null);
        }
        if (choice == 2) {
            puzzle = createMultidoku(false);
        }
        if (puzzle == null) {
            return;
        }
        askToSavePuzzle(puzzle);

        separator();
        important("Voulez-vous résoudre cette grille ?");
        choice = askYesOrNo();
        if (choice == 1) {
            solvePuzzle(puzzle);
        }
    }

    /**
     * Un des sous-menus principaux, qui permet de résoudre un puzzle
     */
    private static void solvePuzzleSubMenu() {
        separator();
        important("Quelle grille voulez-vous résoudre ?");
        int choice;
        do {
            System.out.println("1. Créer une grille et la résoudre");
            System.out.println("2. Importer une grille et la résoudre");
            System.out.print("Choix : ");
            choice = getIntFromUser(false);
        } while (choice < 1 || choice > 2);

        Puzzle puzzle = null;
        if (choice == 1) {
            separator();
            important("Quel type de grille créer ?");
            choice = askSudokuOrMultidoku();
            if (choice == 1) {
                puzzle = createSudoku(false, null);
            }
            if (choice == 2) {
                puzzle = createMultidoku(false);
            }
            if (puzzle == null) {
                return;
            }
            askToSavePuzzle(puzzle);
        }
        if (choice == 2) {
            puzzle = importPuzzle();
            if (puzzle == null) {
                return;
            }
        }

        solvePuzzle(puzzle);
    }

    /**
     * Un des sous-menus principaux, qui permet de générer un puzzle résolu
     */
    public static void generateSolvedPuzzleSubMenu() {
        // on crée le puzzle
        Puzzle puzzle = createEmptyPuzzle();
        if (puzzle == null) {
            return;
        }

        // on demande les contraintes supplémentaires
        ArrayList<SudokuConstraint> constraints = null;
        separator();
        important("Voulez-vous spécifier des règles supplémentaires ?");
        int choiceConstraints = askYesOrNo();
        if (choiceConstraints == 1) {
            constraints = getCustomRules(puzzle);
        }

        // on applique l'algorithme
        separator();
        String filename = null;
        try {
            ArrayList<String> logs = new ArrayList<>();
            boolean valid = Solver.generateNewSolvedPuzzle(puzzle, constraints, logs);
            filename = logOperations(logs, "solved", puzzle);
            if (valid) {
                success("Nouvelle grille résolue créée !");
            }
            else {
                error("Une nouvelle grille n'a pas pu être créée !");
            }
        }
        catch (RuntimeException e) {
            error("La grille spécifié n'a pas pu être utilisée pour générer une nouvelle grille résolue !");
            error(e.getMessage());
        }

        // on affiche le résultat
        System.out.println(puzzle);
        askToSeeLogs(filename);
        askToSavePuzzle(puzzle);
    }

    /**
     * Un des sous-menus principaux, qui permet de générer un puzzle à résoudre
     */
    public static void generatePuzzleToSolveSubMenu() {
        // on crée un puzzle vide
        Puzzle puzzle = createEmptyPuzzle();
        if (puzzle == null) {
            return;
        }

        // on demande la difficulté
        separator();
        important("Quelle sera la difficulté pour résoudre la grille ?");
        int difficulty;
        do {
            System.out.println("1. Facile");
            System.out.println("2. Moyen");
            System.out.println("3. Difficile");
            System.out.print("Choix : ");
            difficulty = getIntFromUser(false);
        } while (difficulty < 1 || difficulty > 3);

        // on demande les contraintes supplémentaires
        ArrayList<SudokuConstraint> constraints = null;
        separator();
        important("Voulez-vous spécifier des règles supplémentaires ?");
        int choiceConstraints = askYesOrNo();
        if (choiceConstraints == 1) {
            constraints = getCustomRules(puzzle);
        }

        // on applique l'algorithme
        separator();
        String filename = null;
        try {
            boolean valid = Solver.generateNewSolvedPuzzle(puzzle, constraints, null);
            if (valid) {
                ArrayList<String> logs = new ArrayList<>();
                valid = Solver.generateNewPuzzleToSolve(puzzle, constraints, difficulty, logs);
                filename = logOperations(logs, "toSolve-" + difficulty, puzzle);
                success("Nouvelle grille à résoudre créée !");
                if (!valid) {
                    important("L'algorithme n'a pas pu générer de grille plus dure que celle-ci !");
                }
            }
            else {
                error("Une nouvelle grille n'a pas pu être créée !");
            }
        }
        catch (RuntimeException e) {
            error("La grille spécifié n'a pas pu être utilisé pour générer une nouvelle grille à résoudre !");
            error(e.getMessage());
            return;
        }

        // on affiche le résultat
        System.out.println(puzzle);
        askToSeeLogs(filename);
        askToSavePuzzle(puzzle);
    }

    /**
     * Demande à l'utilisateur d'importer un puzzle
     * @return Le puzzle choisi par l'utilisateur, ou null si un puzzle n'a pas pu être importé
     */
    private static Puzzle importPuzzle() {
        separator();
        important("Quel type de grille importer ?");
        int choice = askSudokuOrMultidoku();
        String filename = askWhichFileToImport((choice == 2));
        if (filename == null) {
            return null;
        }
        try {
            Puzzle puzzle = null;
            if (choice == 1) {
                puzzle = FilesOperations.readSudokuFromFile(filename);
            }
            if (choice == 2) {
                puzzle = FilesOperations.readMultidokuFromFile(filename);
            }
            if (puzzle == null) {
                return null;
            }
            separator();
            success("Grille importée !");
            System.out.println(puzzle);
            return puzzle;
        }
        catch (IOException e) {
            error("Erreur lors de la lecture dans le fichier");
            return null;
        }
        catch (RuntimeException e) {
            error("Erreur lors de la lecture dans le fichier");
            error(e.getMessage());
            return null;
        }
    }

    /**
     * Demande à l'utilisateur s'il veut sauvegarder le puzzle passé en paramètre dans un fichier
     * @param puzzle Le puzzle à sauvegarder dans un fichier
     */
    private static void askToSavePuzzle(Puzzle puzzle) {
        separator();
        important("Enregistrer cette grille dans un fichier ?");
        int choice = askYesOrNo();
        if (choice == 1) {
            try {
                String filename = askNewFileName((puzzle instanceof Multidoku));
                if (puzzle instanceof Sudoku) {
                    FilesOperations.convertSudokuToFile((Sudoku) puzzle, filename);
                } else if (puzzle instanceof Multidoku) {
                    FilesOperations.convertMultidokuToFile((Multidoku) puzzle, filename);
                } else {
                    error("Type de grille inconnue !");
                    return;
                }
                success("Grille enregistrée !");
            }
            catch (IOException e) {
                error("Erreur lors de l'écriture dans le fichier !");
            }
            catch (RuntimeException e) {
                error("Le sudoku n'a pas pu être écrit dans un fichier !");
                error(e.getMessage());
            }
        }
    }

    /**
     * Demander à l'utilisateur de créer un nouveau puzzle vide
     * @return Le puzzle créé par l'utilisateur
     */
    private static Puzzle createEmptyPuzzle() {
        separator();
        important("Quel type de grille générer ?");
        int choice = askSudokuOrMultidoku();

        Puzzle puzzle = null;
        if (choice == 1) {
            puzzle = createSudoku(true, null);
        }
        if (choice == 2) {
            puzzle = createMultidoku(true);
        }
        return puzzle;
    }

    /**
     * Demande à l'utilisateur de créer un sudoku et le renvoie
     * @param isEmpty Spécifie si le sudoku doit être vide, c'est-à-dire si l'utilisateur ne doit pas rentrer ses valeurs
     * @param specifiedSize Spécifie une taille pour le sudoku, s'il est null alors la taille sera demandée à l'utilisateur
     * @return Le sudoku créé par l'utilisateur
     */
    private static Sudoku createSudoku(boolean isEmpty, Integer specifiedSize) {
        separator();
        System.out.println("Veuillez remplir les informations du sudoku");

        // choix de la taille du sudoku
        int size;
        if (specifiedSize == null) {
            if (!isEmpty) {
                important("Attention, il est très fastidieux de créer un sudoku à la main au-delà de 6 de taille !");
            }
            else {
                important("Attention, il peut être très long de générer des sudokus au-delà de 16 de taille !");
            }
            System.out.print("Taille du sudoku : ");
            size = getIntFromUser(false);
        }
        else {
            size = specifiedSize;
        }

        int[][] values = new int[size][size];
        int[][] placements = new int[size][size];

        // choix des positions et valeurs des cases
        important("Voulez-vous spécifier la position des blocs ?");
        int input = askYesOrNo();
        boolean hasCustomPlacement = (input == 1);
        if (!isEmpty) {
            important("Si une case est vide, rentrez 0 ou rien");
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val;

                if (!isEmpty) {
                    do {
                        System.out.print("Valeur de la case à la ligne " + (i+1) + " et colonne " + (j+1) + " : ");
                        val = getIntFromUser(true);
                    } while (val < 0 || val > size);
                    values[i][j] = val - 1;
                }
                else {
                    values[i][j] = -1;
                }

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

        try {
            // création du sudoku
            Sudoku sudoku = new Sudoku(size, placements);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (values[i][j] != -1)
                        sudoku.getCase(i, j).setValue(values[i][j]);
                }
            }

            // choix des symboles utilisés
            important("Voulez-vous spécifier les symboles utilisés ?");
            input = askYesOrNo();
            if (input == 1) {
                HashMap<Integer, String> symbols = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    System.out.print("Veuillez rentrer le symbole pour le chiffre " + (i+1) + " : ");
                    String symbol = scanner.nextLine();
                    symbols.put(i, symbol);
                }
                sudoku.setSymbols(symbols);
            }

            // choix des contraintes sur le sudoku
            important("Voulez-vous ajouter des règles internes au sudoku ?");
            input = askYesOrNo();
            if (input == 1) {
                ArrayList<SudokuConstraint> constraints = getCustomRules(sudoku);
                sudoku.setAddedConstraints(constraints);
            }

            // affichage du sudoku créé
            separator();
            success("Nouveau sudoku créé !");
            System.out.println(sudoku);

            return sudoku;
        }
        catch (RuntimeException e) {
            error("Le sudoku créé n'est pas valide !");
            error(e.getMessage());
            return null;
        }
    }

    /**
     * Demande à l'utilisateur de créer un multidoku et le renvoie
     * @return Le sudoku créé par l'utilisateur
     */
    private static Multidoku createMultidoku(boolean isEmpty) {
        // choix de la taille des sudokus
        separator();
        System.out.println("Veuillez remplir les informations du sudoku");
        if (!isEmpty) {
            important("Attention, il est très fastidieux de créer plusieurs sudokus à la main au-delà de 4 de taille !");
        }
        else {
            important("Attention, il peut être très long de générer plusieurs sudokus au-delà de 9 de taille !");
        }
        System.out.print("Taille des sudokus : ");
        int sizeSudokus = getIntFromUser(false);

        // création des sudokus
        ArrayList<PlacedSudoku> placedSudokus = new ArrayList<>();
        for (int i = 0; i < sizeSudokus; i++) {
            separator();
            important("Sudoku n°" + (i + 1) + " : ");
            System.out.print("Quelle est la ligne du sudoku sur la grille de multidoku ? ");
            int sudokuLine = getIntFromUser(false);
            System.out.print("Quelle est la colonne du sudoku sur la grille de multidoku ? ");
            int sudokuColumn = getIntFromUser(false);
            Sudoku sudoku = createSudoku(false, sizeSudokus);
            placedSudokus.add(new PlacedSudoku(sudoku, sudokuLine, sudokuColumn));
        }

        try {
            // création du multidoku
            Multidoku multidoku = new Multidoku(placedSudokus);

            // choix des symboles utilisés
            important("Voulez-vous spécifier les symboles utilisés ?");
            int input = askYesOrNo();
            if (input == 1) {
                HashMap<Integer, String> symbols = new HashMap<>();
                for (int i = 0; i < sizeSudokus; i++) {
                    System.out.print("Veuillez rentrer le symbole pour le chiffre " + (i+1) + " : ");
                    String symbol = scanner.nextLine();
                    symbols.put(i, symbol);
                }
                multidoku.setSymbols(symbols);
            }

            // choix des contraintes sur le multidoku
            important("Voulez-vous ajouter des règles internes au multidoku ?");
            input = askYesOrNo();
            if (input == 1) {
                ArrayList<SudokuConstraint> constraints = getCustomRules(multidoku);
                multidoku.setAddedConstraints(constraints);
            }

            // affichage du multidoku créé
            separator();
            success("Nouveau multidoku créé !");
            System.out.println(multidoku);

            return multidoku;
        }
        catch (RuntimeException e) {
            error("Le multidoku créé n'est pas valide !");
            error(e.getMessage());
            return null;
        }
    }

    /**
     * Résout un puzzle passé en paramètre, en demandant à l'utilisateur l'algo utilisé et les règles supplémentaires
     * @param puzzle Le puzzle à résoudre
     */
    private static void solvePuzzle(Puzzle puzzle) {
        // on demande l'algorithme à utiliser
        separator();
        important("Quel algorithme utiliser pour résoudre la grille ?");
        int algoChoice;
        do {
            System.out.println("1. Résoudre avec simplement les contraintes (vitesse ++, efficacité -)");
            System.out.println("2. Résoudre avec simplement le backtracking (vitesse --, efficacité ++)");
            System.out.println("3. Résoudre avec un mix des deux (vitesse -+, efficacité ++)");
            System.out.print("Choix : ");
            algoChoice = getIntFromUser(false);
        } while (algoChoice < 1 || algoChoice > 3);

        // on demande les contraintes supplémentaires
        ArrayList<SudokuConstraint> constraints = null;
        separator();
        important("Voulez-vous spécifier des règles supplémentaires ?");
        int choiceConstraints = askYesOrNo();
        if (choiceConstraints == 1) {
            constraints = getCustomRules(puzzle);
        }

        // on applique l'algorithme
        separator();
        ArrayList<String> logs = new ArrayList<>();
        boolean solved = false;
        String filename = null;
        try {
            if (algoChoice == 1) {
                solved = Solver.solveWithConstraints(puzzle, constraints, logs);
                filename = logOperations(logs, "constraints", puzzle);
            }
            if (algoChoice == 2) {
                solved = Solver.solveWithBacktracking(puzzle, constraints, logs);
                filename = logOperations(logs, "backtracking", puzzle);
            }
            if (algoChoice == 3) {
                solved = Solver.solveWithBoth(puzzle, constraints, logs);
                filename = logOperations(logs, "mixed", puzzle);
            }
        }
        catch (RuntimeException e) {
            error("L'algorithme a échoué !");
            error(e.getMessage());
        }

        // on affiche le résultat
        if (solved) {
            success("La grille a été résolue !");
        } else {
            error("La grille n'a pas pu être résolue !");
        }
        System.out.println(puzzle);

        askToSeeLogs(filename);
        askToSavePuzzle(puzzle);
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

            try {
                constraints.add(FilesOperations.readConstraint(line, puzzle));
            }
            catch (RuntimeException e) {
                error("La contrainte rentrée n'est pas valide !");
                error(e.getMessage());
            }

            important("Ajouter une autre contrainte ?");
            int choice = askYesOrNo();
            stop = (choice == 2);
        }
        return constraints;
    }

    /**
     * Crée un nouveau fichier de logs
     * @param logs La suite d'opération à loguer
     * @param algorithm Le nom de l'algorithme utilisé
     * @param puzzle Le puzzle sur lequel a été réalisé l'algorithme
     * @return Le nom du fichier de logs créé, ou null s'il y a une erreur
     */
    private static String logOperations(ArrayList<String> logs, String algorithm, Puzzle puzzle) {
        try {
            String filename = FilesOperations.createNewLogFile(logs, algorithm, puzzle);
            success("Logs enregistrés dans : S5-sudoku/data/logs/" + filename + ".txt");
            return filename;
        }
        catch (RuntimeException e) {
            error("Erreur lors de l'écriture du fichier de logs !");
            error(e.getMessage());
            return null;
        }
        catch (Exception e) {
            error("Erreur lors de l'écriture du fichier de logs !");
            return null;
        }
    }

    /**
     * Demande à l'utilisateur s'il veut voir les logs d'un fichier passé en paramètre
     * @param filename Le nom du fichier de logs
     */
    private static void askToSeeLogs(String filename) {
        if (filename == null) return;
        separator();
        important("Voir les logs de l'algorithme ?");
        int choice = askYesOrNo();
        if (choice == 1) {
            try {
                FilesOperations.printLogs(filename);
            }
            catch (IOException e) {
                error("Erreur lors de la lecture du fichier de logs !");
            }
        }
    }

    /**
     * Demande à l'utilisateur quel fichier importer
     * @param isMultidoku Si c'est un fichier de multidoku ou de sudoku
     * @return Le nom du fichier choisi par l'utilisateur
     */
    private static String askWhichFileToImport(boolean isMultidoku) {
        // récupère la liste des fichiers
        File[] puzzles = null;
        try {
            String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
            File puzzleDirectory = new File(directoryPath);
            puzzles = puzzleDirectory.listFiles();
        }
        catch (Exception e) {
            error("Erreur lors de l'accès aux grilles enregistrées !");
        }

        int validFilesCount = 0;
        String[] validFiles;
        separator();
        important("Fichiers enregistrés : ");
        // si il y a aucun fichier
        if (puzzles == null) {
            System.out.println("Pas de fichiers trouvés !");
            return null;
        }

        // on liste les fichiers .txt enregistrés
        validFiles = new String[puzzles.length];
        for (File puzzle : puzzles) {
            if (puzzle.getName().endsWith(".txt")) {
                validFilesCount++;
                System.out.println(validFilesCount + ". " + puzzle.getName());
                validFiles[validFilesCount - 1] = puzzle.getName().replace(".txt", "");
            }
        }

        // si il y a aucun fichier .txt
        if (validFilesCount == 0) {
            System.out.println("Pas de fichiers trouvés !");
            return null;
        }

        // on demande quel fichier importer
        important("Importer quel fichier ?");
        int choice;
        do {
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
        // récupère la liste des fichiers
        File[] puzzles = null;
        try {
            String directoryPath = (isMultidoku) ? "./data/multidokus" : "./data/sudokus";
            File puzzleDirectory = new File(directoryPath);
            puzzles = puzzleDirectory.listFiles();
        }
        catch (Exception e) {
            error("Erreur lors de l'accès aux grilles enregistrées !");
        }

        // on récupère le nom des fichiers .txt
        ArrayList<String> filesNames = new ArrayList<>();
        if (puzzles != null) {
            for (File puzzle : puzzles) {
                if (puzzle.getName().endsWith(".txt")) {
                    filesNames.add(puzzle.getName().replace(".txt", ""));
                }
            }
        }

        // on demande comment nommer le fichier
        separator();
        String filename = null;
        boolean fileIsOK = false;
        while (!fileIsOK) {
            important("Comment voulez-vous appeler le fichier ?");
            System.out.print("Nom : ");
            filename = scanner.nextLine();
            try {
                // si le fichier n'a pas un nom valide
                if (!filename.matches("[A-Za-z0-9_-]+")) {
                    error("Nom invalide ! Caractères autorisés : lettres sans accents, chiffres, _, -");
                }
                // si le fichier existe déjà
                else if (filesNames.contains(filename)) {
                    important("Ce fichier existe déjà, écraser le fichier ?");
                    int choice = askYesOrNo();
                    fileIsOK = (choice == 1);
                }
                // si tout va bien
                else {
                    fileIsOK = true;
                }
            }
            catch (PatternSyntaxException e) {
                error("Erreur lors du test de la validité du nom du fichier !");
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
        try {
            int value = -99;
            boolean validInput = false;
            while (!validInput) {
                String input = scanner.nextLine();
                // si on accepte les réponses vides
                if (convertNothingToZero && input.isEmpty()) {
                    value = 0;
                    validInput = true;
                } else {
                    // si l'input à la forme d'un nombre
                    if (input.matches("[0-9]+")) {
                        value = Integer.parseInt(input);
                        validInput = true;
                    } else {
                        // sinon, on redemande jusqu'à ce que ce soit bon
                        System.out.print("\033[38;2;255;0;0m" + "Veuillez rentrer un nombre : " + "\033[38;2;255;255;255m");
                    }
                }
            }
            return value;
        }
        catch (Exception e) {
            error("Erreur lors de l'essaie de la lecture du nombre !");
            return -99;
        }
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

    /**
     * Print une ligne séparatrice dans le terminal
     */
    public static void separator() {
        System.out.println("--------------------");
    }

    /**
     * Print un message en bleu dans le terminal
     * @param message Le message à print
     */
    public static void important(String message) {
        System.out.println("\033[38;2;34;167;240m" + message + "\033[38;2;255;255;255m");
    }

    /**
     * Print un message en rouge dans le terminal
     * @param message Le message à print
     */
    public static void error(String message) {
        System.out.println("\033[38;2;255;0;0m" + message + "\033[38;2;255;255;255m");
    }

    /**
     * Print un message en vert dans le terminal
     * @param message Le message à print
     */
    public static void success(String message) {
        System.out.println("\033[38;2;0;255;0m" + message + "\033[38;2;255;255;255m");
    }
}
