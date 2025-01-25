package org.example.projet_final_daar.model.closenessCentrality;

import java.util.*;

public class JaccardGraph {

    private final Map<String, Map<String, Double>> graph;

    // Constructeur
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
}

