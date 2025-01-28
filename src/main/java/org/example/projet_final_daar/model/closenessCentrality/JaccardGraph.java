package org.example.projet_final_daar.model.closenessCentrality;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projet_final_daar.model.Livre;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JaccardGraph {

    private final Map<String, Map<String, Double>> graph;

    public JaccardGraph() {
        this.graph = new HashMap<>();
    }

    // Ajoute une arête pondérée au graphe
    public void addEdge(String node1, String node2, double weight) {
        graph.computeIfAbsent(node1, k -> new HashMap<>()).put(node2, weight);
        graph.computeIfAbsent(node2, k -> new HashMap<>()).put(node1, weight); // Graphe non orienté
    }

    // Retourne les voisins d'un nœud sous forme de map (clé : voisin, valeur : poids)
    public Map<String, Double> getNeighbors(String node) {
        return graph.getOrDefault(node, Collections.emptyMap());
    }

    // Retourne l'ensemble des nœuds du graphe
    public Set<String> getNodes() {
        return graph.keySet();
    }

    // Affiche le graphe
    public void printGraph() {
        graph.forEach((node, neighbors) -> {
            System.out.println(node + " est connecté à :");
            neighbors.forEach((neighbor, weight) ->
                    System.out.println("  - " + neighbor + " avec un poids de " + weight)
            );
        });
    }


    /**
     * Sauvegarde un graphe dans plusieurs fichiers texte si la taille d'un fichier dépasse la limite.
     *
     * @param directoryPath   Répertoire où les fichiers seront sauvegardés.
     * @param maxLinesPerFile Taille maximale d'un fichier texte (en octets).
     */
    public void saveGraphToTxt(String directoryPath, int maxLinesPerFile) {
        // Utiliser un Set pour stocker les arêtes uniques
        Set<String> uniqueEdges = new HashSet<>();

        // Crée le répertoire s'il n'existe pas
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        int fileIndex = 1; // Numéro du fichier
        long currentLineCount = 0; // Taille actuelle du fichier

        try {
            BufferedWriter writer = createNewWriter(directoryPath, fileIndex);

            for (Map.Entry<String, Map<String, Double>> entry : graph.entrySet()) {
                String source = entry.getKey();
                for (Map.Entry<String, Double> neighbor : entry.getValue().entrySet()) {
                    String target = neighbor.getKey();
                    double weight = neighbor.getValue();

                    // Crée une clé unique pour éviter les doublons
                    String edgeKey = source.compareTo(target) < 0
                            ? source + " " + target + " " + weight
                            : target + " " + source + " " + weight;

                    // Ajoute uniquement si l'arête est unique
                    if (uniqueEdges.add(edgeKey)) {
                        long edgeSize = edgeKey.getBytes().length + System.lineSeparator().getBytes().length;

                        // Écrit l'arête dans le fichier
                        writer.write(edgeKey);
                        writer.newLine();
                        currentLineCount += edgeSize; // Met à jour la taille actuelle

                        // Vérifie si on dépasse la taille maximale
                        if (currentLineCount >= maxLinesPerFile) {
                            writer.close(); // Ferme le fichier actuel
                            fileIndex++; // Incrémente le numéro du fichier
                            writer = createNewWriter(directoryPath, fileIndex); // Nouveau fichier
                            currentLineCount = 0; // Réinitialise la taille du fichier
                        }


                    }
                }
            }

            writer.close(); // Ferme le dernier fichier
            System.out.println("Graphes sauvegardés dans le répertoire : " + directoryPath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du graphe : " + e.getMessage());
        }
    }


    /**
     * Crée un nouveau BufferedWriter pour un fichier texte.
     *
     * @param directoryPath Répertoire de sortie.
     * @param fileIndex     Numéro du fichier.
     * @return Un BufferedWriter pour écrire dans le fichier.
     * @throws IOException En cas d'erreur lors de la création du fichier.
     */
    private BufferedWriter createNewWriter(String directoryPath, int fileIndex) throws IOException {
        String filePath = directoryPath + File.separator + "graph_part_" + fileIndex + ".txt";
        return new BufferedWriter(new FileWriter(filePath));
    }


    /**
     * Charge un graphe à partir de plusieurs fichiers texte situés dans un répertoire donné.
     *
     * @param directoryPath Le chemin du répertoire contenant les fichiers texte.
     * @param livres        La liste des livres pour filtrer les arêtes valides.
     * @return Un objet JaccardGraph construit à partir des fichiers texte.
     * @throws IOException En cas d'erreur lors de la lecture des fichiers.
     */
    public static JaccardGraph loadGraphFromDirectory(String directoryPath, List<Livre> livres) throws IOException {
        JaccardGraph graph = new JaccardGraph();

        // Liste des fichiers texte dans le répertoire
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Le chemin spécifié n'est pas un répertoire : " + directoryPath);
        }

        File[] txtFiles = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (txtFiles == null || txtFiles.length == 0) {
            throw new FileNotFoundException("Aucun fichier texte trouvé dans le répertoire : " + directoryPath);
        }

        // Crée un Set pour une recherche rapide des IDs des livres
        Set<String> livresIdSet = livres.stream()
                .map(livre -> Integer.toString(livre.getId()))
                .collect(Collectors.toSet());

        // Parcourt chaque fichier texte dans le répertoire
        for (File txtFile : txtFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(txtFile))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // Divise chaque ligne en tokens (source, target, weight)
                    String[] parts = line.split("\\s+");
                    if (parts.length != 3) {
                        System.err.println("Format incorrect ignoré dans " + txtFile.getName() + " : " + line);
                        continue; // Ignore les lignes incorrectes
                    }

                    String source = parts[0];
                    String target = parts[1];
                    double weight;

                    try {
                        weight = Double.parseDouble(parts[2]); // Convertit le poids en double
                    } catch (NumberFormatException e) {
                        System.err.println("Poids non valide ignoré dans " + txtFile.getName() + " : " + line);
                        continue; // Ignore la ligne si le poids n'est pas un nombre valide
                    }

                    // Ajoute l'arête uniquement si les IDs source et target existent dans la liste des livres
                    if (livresIdSet.contains(source) && livresIdSet.contains(target)) {
                        graph.addEdge(source, target, weight);
                    }
                }
            }
        }

        return graph;
    }


}

