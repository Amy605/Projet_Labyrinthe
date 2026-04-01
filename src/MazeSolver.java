/**
 * Interface que tout algorithme de résolution doit implémenter.
 *
 * @author Étudiant 2
 */
public interface MazeSolver {

    /**
     * Résout le labyrinthe donné.
     * @param maze le labyrinthe à résoudre
     * @return SolverResult contenant chemin, stats et temps
     */
    SolverResult solve(Maze maze);

    /**
     * Retourne le nom de l'algorithme.
     */
    String getName();
}
