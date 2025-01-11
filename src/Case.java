public class Case {
    private String valeur;
    private int ligne;
    private int colonne;
    private int blocIndex;

    public Case(String valeur, int ligne, int colonne, int blocIndex) {
        this.valeur = valeur;
        this.ligne = ligne;
        this.colonne = colonne;
        this.blocIndex = blocIndex;
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

    public int getBlocIndex() {
        return blocIndex;
    }
}
