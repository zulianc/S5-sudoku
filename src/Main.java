import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.startMenu();
        int[][] placements = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                placements[i][j] = ((i / 2)*2) + (j / 2);
            }
        }
        Sudoku sudoku = new Sudoku(4, placements);

        HashMap<Integer, String> symboles = new HashMap<>();
        symboles.put(0, "A");
        symboles.put(1, "B");
        symboles.put(2, "C");
        symboles.put(3, "D");
        sudoku.setSymboles(symboles);

        sudoku.getCase(0, 3).setValeur(3);
        sudoku.getCase(2, 0).setValeur(1);
        sudoku.getCase(2, 3).setValeur(2);
        sudoku.getCase(3, 0).setValeur(3);
        sudoku.getCase(3, 2).setValeur(0);
        sudoku.getCase(3, 3).setValeur(1);
        System.out.println(sudoku);

        Solver solver = new Solver(sudoku, null);
        boolean solvable = solver.solve();
        if (!solvable) {
            System.out.println("cheh");
        }

        System.out.println(sudoku);
    }
}