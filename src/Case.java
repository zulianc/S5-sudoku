import java.util.HashSet;
import java.util.Set;

public class Case {
    private int valeur;
    private final int ligne;
    private final int colonne;
    private final int blocIndex;
    private Set<Integer> possibleValues;

    protected Case(int ligne, int colonne, int blocIndex, Set<Integer> possibleValues) {
        this.valeur = -1;
        this.ligne = ligne;
        this.colonne = colonne;
        this.blocIndex = blocIndex;
        this.possibleValues = possibleValues;
    }

    protected Case(int valeur, int ligne, int colonne, int blocIndex, Set<Integer> possibleValues) {
        this(ligne,  colonne,  blocIndex, possibleValues);
        this.valeur = valeur;
    }

    public int getValeur() {
        return this.valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
        this.possibleValues = new HashSet<>(valeur);
    }

    public void removePossibleValue(int valeur) {
        this.possibleValues.remove(valeur);
    }

    public int getLigne() {
        return this.ligne;
    }

    public int getColonne() {
        return this.colonne;
    }

    public int getBlocIndex() {
        return this.blocIndex;
    }

    public Set<Integer> getPossibleValues() {
        return this.possibleValues;
    }
}
