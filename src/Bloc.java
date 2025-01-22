public class Bloc {
    private final Case[] cases;
    private final int taille;

    protected Bloc(int taille, Case[] cases) {
        this.taille = taille;
        this.cases = cases;
    }

    public void changerValeur(int index, int valeur) {
        if (index >= 0 && index < this.cases.length) {
            this.cases[index].setValeur(valeur);
            return;
        }
        throw new IllegalArgumentException("Index out of bounds");
    }

    public void changerValeur(int ligne, int colonne, int valeur) {
        for (Case c : this.cases) {
            if (c.getLigne() == ligne && c.getColonne() == colonne) {
                c.setValeur(valeur);
                return;
            }
        }
        throw new IllegalArgumentException("Index out of bounds");
    }

    public Case getCase(int index) {
        if (index >= 0 && index < this.cases.length) {
            return this.cases[index];
        }
        throw new IllegalArgumentException("Index out of bounds");
    }

    public Case getCase(int ligne, int colonne) {
        for (Case c : this.cases) {
            if (c.getLigne() == ligne && c.getColonne() == colonne) {
                return c;
            }
        }
        throw new IllegalArgumentException("Index out of bounds");
    }

    public int getTaille() {
        return this.taille;
    }
}
