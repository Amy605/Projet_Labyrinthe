import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--gui")) {
            launchGUI();
        } else {
            launchConsole();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  MODE CONSOLE
    // ──────────────────────────────────────────────────────────
    private static void launchConsole() {
        MazeDisplay.printHeader();
        Scanner sc = new Scanner(System.in);
        Maze maze = null;

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": // Générer un labyrinthe
                    System.out.print("Taille (ex: 15) : ");
                    try {
                        int size = Integer.parseInt(sc.nextLine().trim());
                        if (size < 5) { System.out.println("Taille minimale : 5"); break; }
                        maze = new MazeGenerator().generate(size, size);
                        System.out.println("✅ Labyrinthe " + maze.getRows() + "×" + maze.getCols() + " généré.");
                        MazeDisplay.print(maze);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Taille invalide.");
                    }
                    break;

                case "2": // Charger depuis un fichier
                    System.out.print("Chemin du fichier : ");
                    String path = sc.nextLine().trim();
                    try {
                        maze = MazeLoader.loadFromFile(path);
                        System.out.println("✅ Labyrinthe chargé (" + maze.getRows() + "×" + maze.getCols() + ")");
                        MazeDisplay.print(maze);
                    } catch (IOException e) {
                        System.out.println("❌ Erreur : " + e.getMessage());
                    }
                    break;

                case "3": // Résoudre avec DFS
                    if (maze == null) { System.out.println("❌ Aucun labyrinthe chargé."); break; }
                    SolverResult dfsResult = new DFSSolver().solve(maze);
                    MazeDisplay.printResult(dfsResult);
                    MazeDisplay.printWithSolution(maze, dfsResult);
                    break;

                case "4": // Résoudre avec BFS
                    if (maze == null) { System.out.println("❌ Aucun labyrinthe chargé."); break; }
                    SolverResult bfsResult = new BFSSolver().solve(maze);
                    MazeDisplay.printResult(bfsResult);
                    MazeDisplay.printWithSolution(maze, bfsResult);
                    break;

                case "5": // Comparer DFS et BFS
                    if (maze == null) { System.out.println("❌ Aucun labyrinthe chargé."); break; }
                    PerformanceComparator.compare(maze);
                    MazeDisplay.print(maze);
                    break;

                case "6": // Afficher le labyrinthe actuel
                    if (maze == null) { System.out.println("❌ Aucun labyrinthe chargé."); break; }
                    MazeDisplay.print(maze);
                    break;

                case "7": // Sauvegarder
                    if (maze == null) { System.out.println("❌ Aucun labyrinthe chargé."); break; }
                    System.out.print("Nom du fichier de sortie : ");
                    String out = sc.nextLine().trim();
                    try {
                        MazeLoader.saveToFile(maze, out);
                        System.out.println("✅ Labyrinthe sauvegardé dans " + out);
                    } catch (IOException e) {
                        System.out.println("❌ Erreur sauvegarde : " + e.getMessage());
                    }
                    break;

                case "0": // Quitter
                    System.out.println("Au revoir ! 👋");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Option invalide.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\u001B[36m┌─────────────────────────────────────┐");
        System.out.println("│            MENU PRINCIPAL           │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. 🎲 Générer un labyrinthe        │");
        System.out.println("│  2. 📂 Charger depuis un fichier    │");
        System.out.println("│  3. 🔍 Résoudre avec DFS            │");
        System.out.println("│  4. 🌊 Résoudre avec BFS            │");
        System.out.println("│  5. 📊 Comparer DFS vs BFS          │");
        System.out.println("│  6. 👁  Afficher le labyrinthe      │");
        System.out.println("│  7. 💾 Sauvegarder                  │");
        System.out.println("│  0. 🚪 Quitter                      │");
        System.out.println("└─────────────────────────────────────┘\u001B[0m");
        System.out.print("Votre choix : ");
    }

    // ──────────────────────────────────────────────────────────
    //  MODE GUI (CORRIGÉ)
    // ──────────────────────────────────────────────────────────
    private static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                // On utilise le style "CrossPlatform" au lieu de "System" 
                // pour permettre la coloration personnalisée des boutons.
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Impossible de charger le Look and Feel");
            }
            new MazeGUI();
        });
    }
}