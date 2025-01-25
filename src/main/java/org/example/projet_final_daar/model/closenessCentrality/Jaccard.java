package org.example.projet_final_daar.model.closenessCentrality;

import java.util.*;
import java.util.stream.*;

public class Jaccard {

    /**
     * Calcule la distance de Jaccard entre deux documents donnés sous forme de listes de mots.
     *
     * @param doc1 La liste des mots du premier document.
     * @param doc2 La liste des mots du second document.
     * @return La distance de Jaccard entre les deux documents.
     */
    public static double jaccardDistance(List<String> doc1, List<String> doc2) {
        Map<String, Long> counter1 = doc1.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        Map<String, Long> counter2 = doc2.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        // Trouve les mots communs entre les deux documents
        Set<String> commonWords = new HashSet<>(counter1.keySet());
        commonWords.retainAll(counter2.keySet());

        if (commonWords.isEmpty()) return 0.0;

        // Calcule le numérateur : somme des différences absolues des occurrences
        double numerator = commonWords.stream()
                .mapToDouble(w -> Math.abs(counter1.get(w) - counter2.get(w)))
                .sum();

        // Calcule le dénominateur : somme des valeurs maximales des occurrences
        double denominator = commonWords.stream()
                .mapToDouble(w -> Math.max(counter1.get(w), counter2.get(w)))
                .sum();

        return numerator / denominator;
    }

    /**
     * Construit un graphe de similarité entre un document donné et les autres documents,
     * basé sur la distance de Jaccard.
     *
     * @param documents Une map associant le nom des fichiers à leurs listes de mots.
     * @param doc       Le document cible à comparer avec les autres.
     * @return Un objet JaccardGraph représentant les connexions entre le document cible et les autres.
     */
    public static JaccardGraph buildJaccardGraph(Map<String, List<String>> documents, String doc) {
        JaccardGraph graph = new JaccardGraph();
        List<String> fileNames = new ArrayList<>(documents.keySet());

        for (String fileName : fileNames) {
            if (!fileName.equals(doc)) {
                // Calcule la distance de Jaccard entre le document cible et un autre document
                double distance = jaccardDistance(documents.get(fileName), documents.get(doc));
                graph.addEdge(fileName, doc, distance);
            }
        }

        return graph;
    }

    public static void main(String[] args) {
        Map<String, List<String>> documents = new HashMap<>();

        // Premier document sous forme de liste de mots.
        List<String> doc1 = Arrays.asList("Il", "fait", "coucou", "au", "coucou", "qui", "se", "repose");

        // Deuxième document sous forme de liste de mots.
        List<String> doc2 = Arrays.asList("l'", "horloge", "fait", "coucou", "pour", "que", "je", "me", "repose");

        // Troisième document (exemple supplémentaire).
        List<String> doc3 = Arrays.asList("le", "soleil", "brille", "et", "le", "vent", "souffle");

        documents.put("doc1", doc1);
        documents.put("doc2", doc2);
        documents.put("doc3", doc3);

        // Construit le graphe de similarité entre les documents.
        JaccardGraph graph = buildJaccardGraph(documents, "doc1");

        // Affiche le graphe.
        graph.printGraph();
    }
}
