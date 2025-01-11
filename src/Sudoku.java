public class Sudoku {
    private Case[][] sudoku;
    private Bloc[] blocs;
    private String[] symboles;
    private int taille;
    private int tailleBloc; // Nombre de cases par bloc

    public Sudoku(int taille) {
        this.taille = taille;

        // Initialiser la taille des blocs
        tailleBloc = (int) Math.sqrt(taille); // Bloc carré de taille racine de la taille de la grille

        sudoku = new Case[taille][taille];
        blocs = new Bloc[taille];

        // Initialiser les blocs
        for (int i = 0; i < taille; i++) {
            blocs[i] = new Bloc(tailleBloc); // Chaque bloc peut contenir `tailleBloc * tailleBloc` cases
        }

        // Initialiser la grille et remplir les blocs
        initialiserGrille();
    }

    private void initialiserGrille() {
        int blocIndex = 0; // Index du bloc
        int caseIndexDansBloc = 0; // Index des cases dans chaque bloc

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                // Créer une nouvelle case
                Case nouvelleCase = new Case(null, ligne, colonne, blocIndex);
                sudoku[ligne][colonne] = nouvelleCase;

                // Ajouter la case au bloc
                blocs[blocIndex].ajouterCase(caseIndexDansBloc, nouvelleCase);
                caseIndexDansBloc++;

                // Vérifier si le bloc est plein et passer au bloc suivant si nécessaire
                if (caseIndexDansBloc == tailleBloc * tailleBloc) {
                    blocIndex++;
                    caseIndexDansBloc = 0; // Réinitialiser l'index des cases pour le nouveau bloc
                }
            }
        }
    }

    public void setSymboles(String[] symboles) {
        if (symboles.length != taille) {
            throw new IllegalArgumentException("Le nombre de symboles doit correspondre à la taille de la grille.");
        }
        this.symboles = symboles;
    }

    public void afficherSudoku() {
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                String valeur = (sudoku[ligne][colonne].getValeur() != null) ? sudoku[ligne][colonne].getValeur() : ".";
                System.out.print(valeur + " ");
            }
            System.out.println();
        }
    }

    public Case getCase(int ligne, int colonne) {
        if (ligne < 0 || ligne >= taille || colonne < 0 || colonne >= taille) {
            throw new IndexOutOfBoundsException("Coordonnées hors limites : " + ligne + ", " + colonne);
        }
        return sudoku[ligne][colonne];
    }
}
