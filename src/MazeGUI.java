import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Interface graphique (Swing) pour visualiser et résoudre le labyrinthe.
 * Fonctionnalités : charger, générer, résoudre avec DFS ou BFS, animer.
 *
 * @author Étudiant 3
 */
public class MazeGUI extends JFrame {

    private static final int CELL_SIZE = 20;

    // Couleurs
    private static final Color COLOR_WALL     = new Color(40, 40, 60);
    private static final Color COLOR_PATH     = new Color(240, 240, 245);
    private static final Color COLOR_START    = new Color(50, 200, 100);
    private static final Color COLOR_END      = new Color(220, 50, 50);
    private static final Color COLOR_SOLUTION = new Color(255, 200, 50);
    private static final Color COLOR_VISITED  = new Color(150, 200, 255);
    private static final Color COLOR_BG       = new Color(20, 20, 35);

    private Maze maze;
    private SolverResult lastResult;

    private JPanel mazePanel;
    private JLabel statusLabel;
    private JButton btnLoadFile;
    private JButton btnGenerate;
    private JButton btnDFS;
    private JButton btnBFS;
    private JButton btnCompare;
    private JSpinner sizeSpinner;

    public MazeGUI() {
        super("🧩 Résolution de Labyrinthe - ESP Dakar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(COLOR_BG);
        buildUI();
        // Générer un labyrinthe de démo au lancement
        generateMaze(15);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BG);

        // --- Panneau de contrôle (haut) ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        controlPanel.setBackground(new Color(30, 30, 50));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Taille
        JLabel sizeLabel = styledLabel("Taille :");
        sizeSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 51, 2));
        styleSpinner(sizeSpinner);

        btnGenerate  = styledButton("🎲 Générer", new Color(60, 100, 180));
        btnLoadFile  = styledButton("📂 Charger", new Color(60, 130, 80));
        btnDFS       = styledButton("🔍 DFS", new Color(120, 60, 160));
        btnBFS       = styledButton("🌊 BFS", new Color(30, 130, 160));
        btnCompare   = styledButton("📊 Comparer", new Color(160, 100, 30));

        controlPanel.add(sizeLabel);
        controlPanel.add(sizeSpinner);
        controlPanel.add(btnGenerate);
        controlPanel.add(btnLoadFile);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(btnDFS);
        controlPanel.add(btnBFS);
        controlPanel.add(btnCompare);

        // --- Panneau labyrinthe (centre) ---
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (maze != null) drawMaze((Graphics2D) g);
            }
        };
        mazePanel.setBackground(COLOR_BG);

        // --- Barre de statut (bas) ---
        statusLabel = new JLabel("Bienvenue ! Générez ou chargez un labyrinthe.");
        statusLabel.setForeground(new Color(200, 200, 220));
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(25, 25, 40));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Légende
        JPanel legend = buildLegendPanel();

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(mazePanel), BorderLayout.CENTER);
        add(legend, BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        btnGenerate.addActionListener(e -> generateMaze((int) sizeSpinner.getValue()));
        btnLoadFile.addActionListener(e -> loadFromFile());
        btnDFS.addActionListener(e -> solveWith(new DFSSolver()));
        btnBFS.addActionListener(e -> solveWith(new BFSSolver()));
        btnCompare.addActionListener(e -> compareAlgorithms());
    }

    private void generateMaze(int size) {
        MazeGenerator gen = new MazeGenerator();
        maze = gen.generate(size, size);
        lastResult = null;
        updateMazePanelSize();
        statusLabel.setText("Labyrinthe " + maze.getRows() + "×" + maze.getCols() + " généré.");
        mazePanel.repaint();
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Charger un labyrinthe (.txt)");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                maze = MazeLoader.loadFromFile(file.getAbsolutePath());
                lastResult = null;
                updateMazePanelSize();
                statusLabel.setText("Labyrinthe chargé : " + file.getName() +
                        " (" + maze.getRows() + "×" + maze.getCols() + ")");
                mazePanel.repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                        "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void solveWith(MazeSolver solver) {
        if (maze == null) return;
        maze.resetVisited();
        lastResult = solver.solve(maze);

        String msg = lastResult.isSolved()
                ? String.format("%s → Chemin: %d cases | Explorées: %d | %.3f ms",
                solver.getName(), lastResult.getPathLength(),
                lastResult.getStepsExplored(), lastResult.getExecutionTimeMs())
                : solver.getName() + " → Aucun chemin trouvé !";

        statusLabel.setText(msg);
        mazePanel.repaint();
    }

    private void compareAlgorithms() {
        if (maze == null) return;

        DFSSolver dfs = new DFSSolver();
        BFSSolver bfs = new BFSSolver();

        SolverResult dfsR = dfs.solve(maze);
        maze.resetVisited();
        SolverResult bfsR = bfs.solve(maze);

        String msg = String.format(
                "DFS → %d cases, %d explorées, %.3f ms    |    BFS → %d cases, %d explorées, %.3f ms",
                dfsR.getPathLength(), dfsR.getStepsExplored(), dfsR.getExecutionTimeMs(),
                bfsR.getPathLength(), bfsR.getStepsExplored(), bfsR.getExecutionTimeMs());

        statusLabel.setText(msg);

        JOptionPane.showMessageDialog(this,
                String.format(
                        "Comparaison DFS vs BFS\n\n" +
                        "─────────────────────────────────────\n" +
                        "                    DFS         BFS\n" +
                        "Longueur chemin  : %6d      %6d\n" +
                        "Cases explorées  : %6d      %6d\n" +
                        "Temps (ms)       : %9.4f  %9.4f\n" +
                        "─────────────────────────────────────\n\n" +
                        "BFS garantit le chemin le plus court.\n" +
                        "DFS est plus rapide en mémoire.",
                        dfsR.getPathLength(), bfsR.getPathLength(),
                        dfsR.getStepsExplored(), bfsR.getStepsExplored(),
                        dfsR.getExecutionTimeMs(), bfsR.getExecutionTimeMs()),
                "Comparaison DFS vs BFS",
                JOptionPane.INFORMATION_MESSAGE);

        mazePanel.repaint();
    }

    private void drawMaze(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Cell[][] grid = maze.getGrid();
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Color color = cellColor(grid[r][c]);
                g.setColor(color);
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Léger contour pour les passages
                if (grid[r][c].getType() != Cell.Type.WALL) {
                    g.setColor(new Color(200, 200, 220, 30));
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
                }

                // Lettre S ou E
                if (grid[r][c].isStart() || grid[r][c].isEnd()) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("SansSerif", Font.BOLD, CELL_SIZE - 4));
                    String letter = grid[r][c].isStart() ? "S" : "E";
                    FontMetrics fm = g.getFontMetrics();
                    int x = c * CELL_SIZE + (CELL_SIZE - fm.stringWidth(letter)) / 2;
                    int y = r * CELL_SIZE + (CELL_SIZE + fm.getAscent() - fm.getDescent()) / 2;
                    g.drawString(letter, x, y);
                }
            }
        }
    }

    private Color cellColor(Cell cell) {
        switch (cell.getType()) {
            case WALL:     return COLOR_WALL;
            case PATH:     return COLOR_PATH;
            case START:    return COLOR_START;
            case END:      return COLOR_END;
            case SOLUTION: return COLOR_SOLUTION;
            case VISITED:  return COLOR_VISITED;
            default:       return Color.GRAY;
        }
    }

    private void updateMazePanelSize() {
        if (maze == null) return;
        int w = maze.getCols() * CELL_SIZE;
        int h = maze.getRows() * CELL_SIZE;
        mazePanel.setPreferredSize(new Dimension(w, h));
        mazePanel.revalidate();
        pack();
    }

    private JPanel buildLegendPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(styledLabel("Légende"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(legendItem(COLOR_WALL, "Mur"));
        panel.add(legendItem(COLOR_PATH, "Passage"));
        panel.add(legendItem(COLOR_START, "Départ (S)"));
        panel.add(legendItem(COLOR_END, "Arrivée (E)"));
        panel.add(legendItem(COLOR_SOLUTION, "Solution (+)"));
        panel.add(legendItem(COLOR_VISITED, "Exploré (·)"));
        return panel;
    }

    private JPanel legendItem(Color color, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setBackground(new Color(25, 25, 40));
        JLabel colorBox = new JLabel("  ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(16, 16));
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 200, 220));
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        row.add(colorBox);
        row.add(label);
        return row;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 200, 220));
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Monospaced", Font.PLAIN, 12));
        spinner.setPreferredSize(new Dimension(60, 28));
    }
}
