package org.example.projet_final_daar.model.closenessCentrality;

import java.util.*;

import static org.example.projet_final_daar.model.closenessCentrality.Jaccard.buildJaccardGraph;

public class ClosenessCentrality {

    /**
     * Calcule la closeness centrality pour un nœud donné dans le graphe.
     *
     * @param graph Le graphe pondéré.
     * @param n     Le nombre total de nœuds dans le graphe.
     * @param doc   Le nœud cible pour lequel calculer la centralité.
     * @return La centralité de proximité du nœud cible.
     */
    public static Double calculateClosenessCentrality(JaccardGraph graph, int n, String doc) {
        Double dis = 0.0;
        Map<String, Double> neighbors = graph.getNeighbors(doc);
        for (String key : neighbors.keySet()) {
            dis += neighbors.get(key);
        }
        if(dis == 0.0) {
            return 0.0;
        }
        return (n - 1) / dis;
    }

    /**
     * Calcule la closeness centrality en générant un graphe basé sur la distance de Jaccard.
     *
     * @param documents La map contenant les documents et leurs listes de mots.
     * @param n         Le nombre total de nœuds dans le graphe.
     * @param doc       Le nœud cible pour lequel calculer la centralité.
     * @return La centralité de proximité du nœud cible.
     */
    public static Double calculateClosenessCentrality2(Map<String, List<String>> documents, int n, String doc) {
        JaccardGraph graph = new JaccardGraph();
        JaccardGraph result = buildJaccardGraph(graph,documents, doc);
        return calculateClosenessCentrality(result, n, doc);
    }

    public static void main(String[] args) {
        // Création du graphe pondéré
        JaccardGraph graph = new JaccardGraph();

        // Ajout des arêtes avec des poids (par exemple, distance de Jaccard)
        graph.addEdge("A", "B", 0.7);
        graph.addEdge("A", "C", 1.0);
        graph.addEdge("A", "E", 0.1);
        graph.addEdge("A", "F", 0.0);

        graph.addEdge("B", "A", 0.7);
        graph.addEdge("B", "E", 0.1);
        graph.addEdge("B", "C", 0.3);
        graph.addEdge("B", "F", 0.0);

        graph.addEdge("C", "A", 1.0);
        graph.addEdge("C", "B", 0.3);
        graph.addEdge("C", "E", 0.4);
        graph.addEdge("C", "F", 0.0);

        graph.addEdge("E", "B", 0.1);
        graph.addEdge("E", "A", 0.1);
        graph.addEdge("E", "C", 0.4);
        graph.addEdge("E", "F", 0.0);

        graph.addEdge("F", "B", 0.0);
        graph.addEdge("F", "A", 0.0);
        graph.addEdge("F", "C", 0.0);
        graph.addEdge("F", "E", 0.0);

        // Affiche le graphe
        System.out.println("Graphe pondéré :");
        graph.printGraph();

        // Calcul de la closeness centrality
        Double centralityA = calculateClosenessCentrality(graph, 5, "A");
        Double centralityB = calculateClosenessCentrality(graph, 5, "B");
        Double centralityC = calculateClosenessCentrality(graph, 5, "C");
        Double centralityE = calculateClosenessCentrality(graph, 5, "E");

        // Affichage des résultats
        System.out.println("ClosenessCentrality :");
        System.out.println("C(A) = " + centralityA);
        System.out.println("C(B) = " + centralityB);
        System.out.println("C(C) = " + centralityC);
        System.out.println("C(E) = " + centralityE);
    }
}
