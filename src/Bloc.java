import java.util.ArrayList;
import java.util.List;

public class Bloc {
    private List<Case> cases; // Liste dynamique pour stocker les cases
    private int capacite; // Nombre maximum de cases dans le bloc

    public Bloc(int capacite) {
        this.capacite = capacite;
        this.cases = new ArrayList<>(capacite);
    }

    // Ajouter une case au bloc
    public void ajouterCase(int index, Case nouvelleCase) {
        if (cases.size() >= capacite) {
            throw new IllegalStateException("Le bloc est déjà plein.");
        }
        cases.add(nouvelleCase);
    }

    // Obtenir une case par index
    public Case getCase(int index) {
        if (index < 0 || index >= cases.size()) {
            throw new IndexOutOfBoundsException("Index hors limites pour le bloc.");
        }
        return cases.get(index);
    }

    // Obtenir le nombre actuel de cases ajoutées au bloc
    public int getNombreCasesActuelles() {
        return cases.size();
    }


}
