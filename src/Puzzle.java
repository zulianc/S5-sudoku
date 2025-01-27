import java.util.ArrayList;

/**
 * Les classes qui implémentent cette interface doivent posséder un ensemble de cases et des contraintes internes sur ces cases
 */
public interface Puzzle {
    /**
     * Une implémentation de cette méthode doit retourner les contraintes internes du puzzle
     * @return Les contraintes internes du puzzle
     */
    ArrayList<SudokuConstraint> defaultConstraints();
}
