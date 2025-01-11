public class Bloc {
    private Case[] cases;
    private int tailleBloc;

    public Bloc(int tailleBloc) {
        this.tailleBloc = tailleBloc;
        this.cases = new Case[tailleBloc * tailleBloc]; // Un tableau de cases pour un bloc
    }

    public void ajouterCase(int index, Case nouvelleCase) {
        if (index >= cases.length) {
            throw new IllegalStateException("Le bloc est déjà plein.");
        }
        cases[index] = nouvelleCase;
    }

    public int getNombreCasesActuelles() {
        int count = 0;
        for (Case c : cases) {
            if (c != null) {
                count++;
            }
        }
        return count;
    }
}
