import java.util.*;

/**
 * Génère aléatoirement un labyrinthe parfait (sans cycles) en utilisant
 * l'algorithme de Recursive Backtracker (DFS).
 *
 * @author Étudiant 1
 */
public class MazeGenerator {

    private Random random;

    public MazeGenerator() {
        this.random = new Random();
    }

    public MazeGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Génère un labyrinthe de dimensions (rows x cols).
     * Les dimensions sont automatiquement ajustées pour être impaires
     * afin que la structure mur/passage soit cohérente.
     *
     * @param rows nombre de lignes souhaité
     * @param cols nombre de colonnes souhaitées
     * @return Maze généré
     */
    public Maze generate(int rows, int cols) {
        // Rendre impair pour garantir les passages entre cellules
        if (rows % 2 == 0) rows++;
        if (cols % 2 == 0) cols++;

        char[][] chars = new char[rows][cols];

        // Tout initialiser en murs
        for (char[] row : chars) Arrays.fill(row, '#');

        // Creuser les passages via DFS
        carve(chars, 1, 1);

        // Placer S en haut-gauche et E en bas-droite
        chars[1][1] = 'S';
        chars[rows - 2][cols - 2] = 'E';

        Maze maze = new Maze(rows, cols);
        maze.buildFromChars(chars);
        return maze;
    }

    /**
     * Algorithme Recursive Backtracker : creuse des passages en DFS.
     */
    private void carve(char[][] chars, int r, int c) {
        chars[r][c] = '=';

        // Directions (haut, bas, gauche, droite) mélangées
        int[][] dirs = {{-2,0},{2,0},{0,-2},{0,2}};
        shuffleArray(dirs);

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (inBounds(chars, nr, nc) && chars[nr][nc] == '#') {
                // Casser le mur intermédiaire
                chars[r + d[0]/2][c + d[1]/2] = '=';
                carve(chars, nr, nc);
            }
        }
    }

    private boolean inBounds(char[][] chars, int r, int c) {
        return r > 0 && r < chars.length - 1 && c > 0 && c < chars[0].length - 1;
    }

    private void shuffleArray(int[][] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
    }
}
