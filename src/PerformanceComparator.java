/**
 * Compare les performances de DFS et BFS sur le même labyrinthe.
 *
 * @author Étudiant 3
 */
public class PerformanceComparator {

    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN   = "\u001B[36m";

    /**
     * Lance les deux algorithmes et affiche un tableau comparatif.
     */
    public static void compare(Maze maze) {
        System.out.println(BOLD + "\n📊 COMPARAISON DES PERFORMANCES\n" + RESET);

        DFSSolver dfs = new DFSSolver();
        BFSSolver bfs = new BFSSolver();

        SolverResult dfResult = dfs.solve(maze);
        SolverResult bfsResult = bfs.solve(maze);

        printComparativeTable(dfResult, bfsResult);
        printWinner(dfResult, bfsResult);

        // Remettre le chemin BFS (le plus court) pour l'affichage final
        maze.resetVisited();
        bfs.solve(maze);
    }

    private static void printComparativeTable(SolverResult dfs, SolverResult bfs) {
        System.out.println("┌───────────────────────────┬───────────────┬───────────────┐");
        System.out.println("│ Critère                   │      DFS      │      BFS      │");
        System.out.println("├───────────────────────────┼───────────────┼───────────────┤");

        System.out.printf("│ %-25s │ %-13s │ %-13s │%n",
                "Résolu",
                dfs.isSolved() ? "✅ Oui" : "❌ Non",
                bfs.isSolved() ? "✅ Oui" : "❌ Non");

        System.out.printf("│ %-25s │ %-13d │ %-13d │%n",
                "Longueur du chemin",
                dfs.getPathLength(),
                bfs.getPathLength());

        System.out.printf("│ %-25s │ %-13d │ %-13d │%n",
                "Cases explorées",
                dfs.getStepsExplored(),
                bfs.getStepsExplored());

        System.out.printf("│ %-25s │ %-10.4f ms │ %-10.4f ms │%n",
                "Temps d'exécution",
                dfs.getExecutionTimeMs(),
                bfs.getExecutionTimeMs());

        System.out.println("└───────────────────────────┴───────────────┴───────────────┘");
        System.out.println();
    }

    private static void printWinner(SolverResult dfs, SolverResult bfs) {
        System.out.println(BOLD + "🏆 ANALYSE :" + RESET);

        // Chemin le plus court
        if (dfs.isSolved() && bfs.isSolved()) {
            if (bfs.getPathLength() < dfs.getPathLength()) {
                System.out.println(GREEN + "  • BFS trouve toujours le chemin le plus court !" + RESET);
            } else if (bfs.getPathLength() == dfs.getPathLength()) {
                System.out.println(YELLOW + "  • Les deux algorithmes ont trouvé des chemins de même longueur." + RESET);
            } else {
                System.out.println(CYAN + "  • DFS a trouvé un chemin plus court dans ce cas." + RESET);
            }

            // Exploration
            if (dfs.getStepsExplored() < bfs.getStepsExplored()) {
                System.out.println(GREEN + "  • DFS a exploré moins de cases (" + dfs.getStepsExplored() + " vs " + bfs.getStepsExplored() + ")." + RESET);
            } else {
                System.out.println(GREEN + "  • BFS a exploré moins de cases (" + bfs.getStepsExplored() + " vs " + dfs.getStepsExplored() + ")." + RESET);
            }
        }

        System.out.println();
        System.out.println(BOLD + "📚 CONCLUSION THÉORIQUE :" + RESET);
        System.out.println("  • BFS garantit le chemin le plus court (optimal).");
        System.out.println("  • DFS utilise moins de mémoire (pile vs file).");
        System.out.println("  • BFS est préférable pour trouver le chemin optimal.");
        System.out.println("  • DFS est préférable quand la mémoire est limitée.");
        System.out.println();
    }
}
