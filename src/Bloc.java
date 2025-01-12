public class Bloc {
    private Case[] cases;
    private int taille;

    public Bloc(int taille) {
        this.taille = taille;
        this.cases = new Case[taille]; // Un tableau de cases pour un bloc
    }

    public void ajouterCase(int index, Case nouvelleCase) {
        if (index >= 0 && index < cases.length) {
            cases[index] = nouvelleCase;
        }
    }

    public void afficherCases() {
        for (int i = 0; i < cases.length; i++) {
            if (cases[i] != null) {
                System.out.println(cases[i].toString());
            }
        }
    }


}
