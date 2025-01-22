public class Case {
    private int valeur;
    private final int ligne;
    private final int colonne;
    private final int blocIndex;

    protected Case(int ligne, int colonne, int blocIndex) {
        this.valeur = -1;
        this.ligne = ligne;
        this.colonne = colonne;
        this.blocIndex = blocIndex;
    }

    protected Case(int valeur, int ligne, int colonne, int blocIndex) {
        this(ligne,  colonne,  blocIndex);
        this.valeur = valeur;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
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
