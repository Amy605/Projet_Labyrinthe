import java.util.List;

/**
 * Encapsule le résultat d'une résolution de labyrinthe.
 * Contient le chemin trouvé, le nombre de cases explorées et le temps.
 *
 * @author Étudiant 2
 */
public class SolverResult {

    private List<Cell> path;        // Chemin solution (S → E)
    private int stepsExplored;      // Nombre de cases explorées
    private long executionTimeNs;   // Temps d'exécution en nanosecondes
    private String algorithmName;   // Nom de l'algorithme

    public SolverResult(String algorithmName, List<Cell> path, int stepsExplored, long executionTimeNs) {
        this.algorithmName   = algorithmName;
        this.path            = path;
        this.stepsExplored   = stepsExplored;
        this.executionTimeNs = executionTimeNs;
    }

    public boolean isSolved()         { return path != null && !path.isEmpty(); }
    public List<Cell> getPath()       { return path; }
    public int getStepsExplored()     { return stepsExplored; }
    public long getExecutionTimeNs()  { return executionTimeNs; }
    public String getAlgorithmName()  { return algorithmName; }

    public double getExecutionTimeMs() {
        return executionTimeNs / 1_000_000.0;
    }

    public int getPathLength() {
        return path != null ? path.size() : 0;
    }

    @Override
    public String toString() {
        return String.format("[%s] Résolu: %b | Chemin: %d cases | Explorées: %d | Temps: %.3f ms",
                algorithmName, isSolved(), getPathLength(), stepsExplored, getExecutionTimeMs());
    }
}
