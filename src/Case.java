import java.util.HashSet;
import java.util.Set;

public class Case {
    private int valeur;
    private final int ligne;
    private final int colonne;
    private final int blocIndex;
    private HashSet<Integer> possibleValues;

    protected Case(int ligne, int colonne, int blocIndex, HashSet<Integer> possibleValues) {
        this.valeur = -1;
        this.ligne = ligne;
        this.colonne = colonne;
        this.blocIndex = blocIndex;
        this.possibleValues = possibleValues;
    }

    protected Case(int valeur, int ligne, int colonne, int blocIndex) {
        this.valeur = valeur;
        this.ligne = ligne;
        this.colonne = colonne;
        this.blocIndex = blocIndex;
        HashSet<Integer> possibleValues = new HashSet<>();
        possibleValues.add(valeur);
        this.possibleValues = possibleValues;
    }

    public int getValeur() {
        return this.valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
        this.possibleValues = new HashSet<>();
        this.possibleValues.add(valeur);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Valeur : ").append(this.valeur).append("\n");
        sb.append("Position : ").append(this.ligne).append(" ").append(this.colonne).append(" ").append(this.blocIndex).append("\n");
        sb.append("Possible Values : ");
        for (Integer i : this.possibleValues) {
            sb.append(i).append(" ");
        }
        return sb.toString();
    }
}
