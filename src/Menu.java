import java.util.Scanner;

public class Menu {
    public void startMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        boolean stop = false;
        while (!stop) {
            System.out.println("----------");
            choice = 0;
            while (choice < 1 || choice > 2) {
                System.out.println("1. Créer un sudoku");
                System.out.println("2. Quitter");
                System.out.print("Choix : ");
                choice = sc.nextInt();
            }
            System.out.println("----------");

            switch (choice) {
                case 1: {
                    Sudoku sudoku = this.creerSudoku();
                    System.out.println("----------");
                    System.out.println(sudoku);
                    choice = 0;
                    while (choice < 1 || choice > 3) {
                        System.out.println("1. Enregistrer ce sudoku dans un fichier");
                        System.out.println("2. Résoudre ce sudoku");
                        System.out.println("3. Quitter");
                        System.out.print("Choix : ");
                        choice = sc.nextInt();
                    }
                    switch (choice) {
                        case 1: {
                            FilesOperations.convertSudokuToFile(sudoku);
                            System.out.println("Sudoku enregistré !");
                            break;
                        }
                        case 2: {
                            Solver solver = new Solver(sudoku, null);
                            boolean solved = solver.solve();
                            if (solved) {
                                System.out.println("Le sudoku a été résolu !");
                            }
                            else {
                                System.out.println("Le sudoku n'est pas résolvable !");
                            }
                            System.out.println(sudoku);
                            break;
                        }
                        case 3: {
                            System.out.println("Le sudoku a été supprimé...");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    stop = true;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private Sudoku creerSudoku() {
        Scanner sc = new Scanner(System.in);

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

        return sudoku;
    }
}
