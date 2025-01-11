public class Sudoku {
    private Case[][] sudoku;
    private Bloc[] blocs;
    private String[] symboles;
    private int taille;
    private int tailleBloc; // Taille du bloc (racine carrée de la taille de la grille)

    public Sudoku(int taille) {
        this.taille = taille;

        // La taille des blocs est la racine carrée de la taille de la grille
        this.tailleBloc = (int) Math.sqrt(taille);

        // Vérifier que la taille est un carré parfait
        if (tailleBloc * tailleBloc != taille) {
            throw new IllegalArgumentException("La taille de la grille doit être un carré parfait.");
        }

        sudoku = new Case[taille][taille];
        blocs = new Bloc[taille];

        // Initialiser les blocs
        for (int i = 0; i < taille; i++) {
            blocs[i] = new Bloc(tailleBloc);
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

                // Passer au bloc suivant si le bloc actuel est plein
                if (caseIndexDansBloc == tailleBloc * tailleBloc) {
                    blocIndex++;
                    caseIndexDansBloc = 0;
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
