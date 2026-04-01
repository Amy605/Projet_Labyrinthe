/**
 * Affichage textuel (console) du labyrinthe et des résultats.
 *
 * @author Étudiant 3
 */
public class MazeDisplay {

    // Codes ANSI pour les couleurs en console
    private static final String RESET   = "\u001B[0m";
    private static final String RED     = "\u001B[31m";
    private static final String GREEN   = "\u001B[32m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String BLUE    = "\u001B[34m";
    private static final String CYAN    = "\u001B[36m";
    private static final String WHITE   = "\u001B[37m";
    private static final String BOLD    = "\u001B[1m";

    /**
     * Affiche le labyrinthe avec coloration ANSI.
     */
    public static void print(Maze maze) {
        Cell[][] grid = maze.getGrid();
        System.out.println();
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                System.out.print(cellToColoredChar(grid[r][c]));
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Affiche le labyrinthe avec le chemin solution marqué.
     */
    public static void printWithSolution(Maze maze, SolverResult result) {
        if (!result.isSolved()) {
            System.out.println(RED + BOLD + "Aucun chemin trouvé !" + RESET);
            return;
        }
        System.out.println(GREEN + BOLD + "=== Solution (" + result.getAlgorithmName() + ") ===" + RESET);
        print(maze);
    }

    /**
     * Convertit une cellule en caractère coloré.
     */
    private static String cellToColoredChar(Cell cell) {
        switch (cell.getType()) {
            case WALL:     return WHITE + "█" + RESET;
            case PATH:     return " ";
            case START:    return GREEN  + BOLD + "S" + RESET;
            case END:      return RED    + BOLD + "E" + RESET;
            case SOLUTION: return YELLOW + "+" + RESET;
            case VISITED:  return CYAN   + "·" + RESET;
            default:       return "?";
        }
    }

    /**
     * Affiche un résumé de performances d'un algorithme.
     */
    public static void printResult(SolverResult result) {
        System.out.println(BOLD + "┌─────────────────────────────────────────────┐" + RESET);
        System.out.printf(BOLD  + "│ %-43s │%n" + RESET, result.getAlgorithmName());
        System.out.println(BOLD + "├─────────────────────────────────────────────┤" + RESET);
        if (result.isSolved()) {
            System.out.printf("│ ✅ Résolu           : %-22s│%n", "Oui");
            System.out.printf("│ 📏 Longueur chemin  : %-22d│%n", result.getPathLength());
        } else {
            System.out.printf("│ ❌ Résolu           : %-22s│%n", "Non");
        }
        System.out.printf("│ 🔍 Cases explorées  : %-22d│%n", result.getStepsExplored());
        System.out.printf("│ ⏱️  Temps d'exécution: %-19.4f ms│%n", result.getExecutionTimeMs());
        System.out.println(BOLD + "└─────────────────────────────────────────────┘" + RESET);
        System.out.println();
    }

    /**
     * Affiche le titre du programme.
     */
    public static void printHeader() {
        System.out.println(BLUE + BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║        RÉSOLUTION DE LABYRINTHE              ║");
        System.out.println("║   DFS vs BFS  -  ESP Dakar  -  Master 1     ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
}
