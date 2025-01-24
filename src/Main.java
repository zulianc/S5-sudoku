public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.startMenu();
        int[][] placements = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                placements[i][j] = ((i / 2)*2) + (j / 1);
            }
        }
        Sudoku sudoku = new Sudoku(2, placements);
        sudoku.getCase(0, 0).setValeur(0);
        System.out.println(sudoku);

        Solver solver = new Solver(sudoku, null);
        boolean solvable = solver.solve();
        if (!solvable) {
            System.out.println("cheh");
        }

        System.out.println(sudoku);
    }
}