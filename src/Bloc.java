public class Bloc {
    private Case[] cases;
    public int taille;

    public Bloc(int taille) {
        this.taille = taille;
        cases = new Case[this.taille];
    }

    // Obtenir une case par son index
    public Case getCase(int index) {
        if (index < 0 || index >= taille) {
            throw new IndexOutOfBoundsException("Index invalide : " + index);
        }
        return cases[index];
    }

    // Afficher le contenu du bloc
    public void afficherBloc() {
        for (int i = 0; i < taille; i++) {
            System.out.print((cases[i] != null ? cases[i].getValeur() : "vide") + " ");
        }
        System.out.println();
    }
}

