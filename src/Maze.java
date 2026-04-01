import java.util.ArrayList;
import java.util.List;

/**
 * Représente le labyrinthe sous forme de matrice 2D de Cell.
 * Contient la grille, la position de départ et d'arrivée.
 *
 * @author Étudiant 1
 */
public class Maze {

    private Cell[][] grid;
    private int rows;
    private int cols;
    private Cell start;
    private Cell end;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
    }

    /**
     * Initialise la grille à partir d'une matrice de caractères.
     * '#' = mur, '=' = passage, 'S' = départ, 'E' = arrivée
     */
    public void buildFromChars(char[][] chars) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell.Type type;
                switch (chars[r][c]) {
                    case '#': type = Cell.Type.WALL;  break;
                    case 'S': type = Cell.Type.START; break;
                    case 'E': type = Cell.Type.END;   break;
                    default:  type = Cell.Type.PATH;  break;
                }
                grid[r][c] = new Cell(r, c, type);
                if (type == Cell.Type.START) start = grid[r][c];
                if (type == Cell.Type.END)   end   = grid[r][c];
            }
        }
    }

    /**
     * Retourne les voisins accessibles (non murs) d'une cellule.
     * Ordre : haut, bas, gauche, droite.
     */
    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();

        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : directions) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                Cell neighbor = grid[nr][nc];
                if (!neighbor.isWall()) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    /**
     * Réinitialise les cellules visitées/solution pour relancer un algo.
     */
    public void resetVisited() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (cell.getType() == Cell.Type.VISITED || cell.getType() == Cell.Type.SOLUTION) {
                    cell.setType(Cell.Type.PATH);
                }
            }
        }
        // Remettre S et E
        if (start != null) start.setType(Cell.Type.START);
        if (end   != null) end.setType(Cell.Type.END);
    }

    // --- Getters ---
    public Cell[][] getGrid() { return grid; }
    public int getRows()      { return rows; }
    public int getCols()      { return cols; }
    public Cell getStart()    { return start; }
    public Cell getEnd()      { return end; }
    public Cell getCell(int r, int c) { return grid[r][c]; }
}
