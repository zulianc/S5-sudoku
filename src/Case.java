public class Case {
    private String valeur;
    private int ligne, colonne, bloc;

    public Case(String valeur, int ligne, int colonne, int bloc) {
        this.valeur = valeur;
        this.ligne = ligne;
        this.colonne = colonne;
        this.bloc = bloc;
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

    public int getColonne() {
        return colonne;
    }

    public int getBloc() {
        return bloc;
    }
}
