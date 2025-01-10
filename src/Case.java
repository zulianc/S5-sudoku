public class Case {
    private String valeur;
    private int ligne, colonne, bloc;

    public Case( int ligne, int colonne, int bloc) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.bloc = bloc;
    }

    public Case(String valeur, int ligne, int colonne, int bloc) {
        this(ligne, colonne, bloc);
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public int getLigne() {
        return ligne;
    }

    public int getBloc() {
        return bloc;
    }

    public int getColonne() {
        return colonne;
    }
}
