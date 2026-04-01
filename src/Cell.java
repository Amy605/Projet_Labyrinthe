/**
 * Représente une cellule (case) du labyrinthe.
 * Chaque cellule a une position (ligne, colonne) et un type.
 *
 * @author Étudiant 1
 */
public class Cell {

    public enum Type {
        WALL,    // Mur '#'
        PATH,    // Passage libre '='
        START,   // Point de départ 'S'
        END,     // Point d'arrivée 'E'
        VISITED, // Case visitée durant la résolution '.'
        SOLUTION // Case faisant partie du chemin solution '+'
    }

    private int row;
    private int col;
    private Type type;

    public Cell(int row, int col, Type type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public boolean isWall()  { return type == Type.WALL; }
    public boolean isStart() { return type == Type.START; }
    public boolean isEnd()   { return type == Type.END; }
    public boolean isPath()  { return type == Type.PATH || type == Type.START || type == Type.END; }

    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell c = (Cell) o;
        return row == c.row && col == c.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
