import java.util.*;

/**
 * Résolution du labyrinthe par Breadth First Search (BFS).
 *
 * Principe : explore niveau par niveau (distance croissante).
 * Utilise une file (Queue) pour gérer la frontière d'exploration.
 * GARANTIT le chemin le plus court (en nombre de cases).
 *
 * @author Étudiant 2
 */
public class BFSSolver implements MazeSolver {

    @Override
    public String getName() {
        return "BFS (Breadth First Search)";
    }

    @Override
    public SolverResult solve(Maze maze) {
        maze.resetVisited();

        long startTime = System.nanoTime();

        Cell start = maze.getStart();
        Cell end   = maze.getEnd();

        Cell[][] parent = new Cell[maze.getRows()][maze.getCols()];
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];

        Queue<Cell> queue = new LinkedList<>();
        queue.add(start);
        visited[start.getRow()][start.getCol()] = true;

        int stepsExplored = 0;
        boolean found = false;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            stepsExplored++;

            if (current.equals(end)) {
                found = true;
                break;
            }

            // Marquer comme visitée
            if (!current.isStart() && !current.isEnd()) {
                current.setType(Cell.Type.VISITED);
            }

            for (Cell neighbor : maze.getNeighbors(current)) {
                int nr = neighbor.getRow();
                int nc = neighbor.getCol();
                if (!visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = current;
                    queue.add(neighbor);
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
