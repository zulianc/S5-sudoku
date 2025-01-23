public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.startMenu();
        int[][] placements = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                placements[i][j] = ((i / 3)*3) + (j / 3);
            }
        }
        Sudoku sudoku = new Sudoku(9, placements);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku.getCase(i, j).setValeur(placements[i][j]);
            }
        }
        System.out.println(sudoku);
        Sudoku sudoku2 = sudoku.copy();
        sudoku.getCase(0, 0).setValeur(1);
        System.out.println(sudoku2);
    }
}