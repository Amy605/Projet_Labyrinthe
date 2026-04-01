import java.util.*;

/**
 * Résolution du labyrinthe par Depth First Search (DFS).
 *
 * Principe : explore un chemin jusqu'au bout avant de revenir en arrière.
 * Utilise une pile (Stack) pour gérer la frontière d'exploration.
 * Ne garantit pas le chemin le plus court.
 *
 * @author Étudiant 2
 */
public class DFSSolver implements MazeSolver {

    @Override
    public String getName() {
        return "DFS (Depth First Search)";
    }

    @Override
    public SolverResult solve(Maze maze) {
        maze.resetVisited();

        long startTime = System.nanoTime();

        Cell start = maze.getStart();
        Cell end   = maze.getEnd();

        // parent[r][c] = cellule depuis laquelle on est arrivé en (r,c)
        Cell[][] parent = new Cell[maze.getRows()][maze.getCols()];
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];

        Stack<Cell> stack = new Stack<>();
        stack.push(start);
        visited[start.getRow()][start.getCol()] = true;

        int stepsExplored = 0;
        boolean found = false;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            stepsExplored++;

            if (current.equals(end)) {
                found = true;
                break;
            }

            // Marquer comme visitée (sauf départ/arrivée)
            if (!current.isStart() && !current.isEnd()) {
                current.setType(Cell.Type.VISITED);
            }

            for (Cell neighbor : maze.getNeighbors(current)) {
                int nr = neighbor.getRow();
                int nc = neighbor.getCol();
                if (!visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = current;
                    stack.push(neighbor);
                }
            }
        }

        long endTime = System.nanoTime();

        List<Cell> path = found ? reconstructPath(parent, start, end) : new ArrayList<>();

        // Marquer le chemin solution
        for (Cell c : path) {
            if (!c.isStart() && !c.isEnd()) {
                c.setType(Cell.Type.SOLUTION);
            }
        }

        return new SolverResult(getName(), path, stepsExplored, endTime - startTime);
    }

    /**
     * Reconstruit le chemin depuis end vers start en remontant les parents.
     */
    private List<Cell> reconstructPath(Cell[][] parent, Cell start, Cell end) {
        LinkedList<Cell> path = new LinkedList<>();
        Cell current = end;

        while (current != null) {
            path.addFirst(current);
            if (current.equals(start)) break;
            current = parent[current.getRow()][current.getCol()];
        }

        return path;
    }
}
