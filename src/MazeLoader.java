import java.io.*;
import java.util.*;

/**
 * Charge un labyrinthe depuis un fichier texte.
 * Format attendu : lignes de caractères (#, =, S, E).
 *
 * @author Étudiant 1
 */
public class MazeLoader {

    /**
     * Charge un labyrinthe depuis un fichier.
     * @param filepath chemin vers le fichier
     * @return Maze construit
     * @throws IOException si le fichier est introuvable ou mal formaté
     */
    public static Maze loadFromFile(String filepath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("Le fichier labyrinthe est vide.");
        }

        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] chars = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols && c < line.length(); c++) {
                chars[r][c] = line.charAt(c);
            }
        }

        Maze maze = new Maze(rows, cols);
        maze.buildFromChars(chars);

        if (maze.getStart() == null) throw new IOException("Pas de point de départ 'S' trouvé.");
        if (maze.getEnd()   == null) throw new IOException("Pas de point d'arrivée 'E' trouvé.");

        return maze;
    }

    /**
     * Sauvegarde un labyrinthe dans un fichier texte.
     */
    public static void saveToFile(Maze maze, String filepath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            Cell[][] grid = maze.getGrid();
            for (int r = 0; r < maze.getRows(); r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < maze.getCols(); c++) {
                    sb.append(cellToChar(grid[r][c]));
                }
                pw.println(sb.toString());
            }
        }
    }

    private static char cellToChar(Cell cell) {
        switch (cell.getType()) {
            case WALL:     return '#';
            case START:    return 'S';
            case END:      return 'E';
            case SOLUTION: return '+';
            case VISITED:  return '.';
            default:       return '=';
        }
    }
}
