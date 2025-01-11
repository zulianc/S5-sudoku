public class Main {
    public static void main(String[] args) {
        // Taille de la grille (9x9 pour un Sudoku classique)
        int taille = 9;

        // Créer une instance de Sudoku
        Sudoku sudoku = new Sudoku(taille);

        // Définir les symboles (1 à 9 pour une grille 9x9)
        String[] symboles = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        sudoku.setSymboles(symboles);

        // Ajouter quelques valeurs dans la grille
        sudoku.getCase(0, 0).setValeur("5");
        sudoku.getCase(0, 1).setValeur("3");
        sudoku.getCase(1, 0).setValeur("6");
        sudoku.getCase(4, 4).setValeur("7");

        // Afficher la grille
        System.out.println("Grille de Sudoku initialisée :");
        sudoku.afficherSudoku();
    }
}
