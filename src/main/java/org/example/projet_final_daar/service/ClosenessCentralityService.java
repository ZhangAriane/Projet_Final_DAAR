package org.example.projet_final_daar.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.projet_final_daar.model.Livre;
import org.example.projet_final_daar.model.closenessCentrality.JaccardGraph;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.projet_final_daar.model.closenessCentrality.ClosenessCentrality.calculateClosenessCentrality;
import static org.example.projet_final_daar.model.closenessCentrality.Jaccard.buildJaccardGraph;
import static org.example.projet_final_daar.model.closenessCentrality.JaccardGraph.loadGraphFromDirectory;

@Service
public class ClosenessCentralityService {

    /**
     * Méthode pour extraire les mots d'un texte accessible via une URL.
     * Seuls les mots ayant une longueur supérieure à 4 inclus.
     *
     * @param urlString L'URL vers le texte.
     * @return Une liste contenant tous les mots du texte répondant à la condition.
     * @throws Exception En cas d'erreur lors de la connexion ou de la lecture.
     */
    private List<String> extractWordsFromUrl(String urlString) throws Exception {
        try {
            List<String> words = new ArrayList<>();
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\W+");
                for (String word : tokens) {
                    if (word.length() >= 4) {
                        words.add(word);
                    }
                }
            }
            reader.close();
            return words;
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture de l'URL : " + urlString, e);
        }
    }


    /**
     * Extrait les mots d'une liste de livres.
     * Retourne une map où chaque titre est associé à la liste des mots extraits du texte.
     *
     * @param livres Liste des livres.
     * @return Une map avec le titre du livre comme clé et la liste des mots comme valeur.
     * @throws Exception En cas d'erreur lors de la lecture des URLs des livres.
     */
    private Map<String, List<String>> extractWordsFromBooks(List<Livre> livres) throws Exception {
        return livres.parallelStream().collect(Collectors.toMap(
                livre -> Integer.toString(livre.getId()),
                livre -> {
                    try {
                        return extractWordsFromUrl(livre.getTxt());
                    } catch (Exception e) {
                        throw new RuntimeException("Erreur lors de l'extraction des mots pour le livre : " + livre.getId(), e);
                    }
                }
        ));
    }


    /**
     * Calcule la centralité de proximité pour chaque livre, puis classe les livres par leur centralité.
     * Les résultats sont stockés dans une HashMap où les clés sont les scores de centralité et les
     * valeurs sont les livres correspondants.
     *
     * @param livres Liste des livres.
     * @return Une HashMap contenant les centralités comme clés et les livres correspondants comme valeurs.
     * @throws Exception En cas d'erreur lors de l'extraction des mots ou du calcul de la centralité.
     */
    private HashMap<Double, List<Livre>> preClosenessCentrality(List<Livre> livres) throws Exception {
        HashMap<Double, List<Livre>> res = new HashMap<>();


        // Lire le graph depuis les fichiers txt
        JaccardGraph graph = loadGraphFromDirectory("src/main/java/org/example/projet_final_daar/data/graph", livres);

        for (Livre livre : livres) {
            // Calcule la centralité de proximité pour le livre actuel
            double n = calculateClosenessCentrality(graph, livres.size(), Integer.toString(livre.getId()));
            if (n > 0) { // Si le score de centralité est positif
                if (!res.containsKey(n)) {
                    ArrayList<Livre> temp = new ArrayList<>();
                    temp.add(livre); // Ajoute le livre à la liste temporaire
                    res.put(n, temp); // Associe la centralité à la liste
                } else {
                    res.get(n).add(livre); // Ajoute le livre à une clé existante
                }
            }
        }

        return res; // Retourne la HashMap des centralités
    }

    /**
     * Trie une HashMap de livres par centralité de proximité dans l'ordre décroissant.
     *
     * @param map La HashMap contenant les scores de centralité comme clés et les livres comme valeurs.
     * @return Une liste de livres triée par centralité décroissante.
     */
    private List<Livre> sortByClosenessCentrality(HashMap<Double, List<Livre>> map) {
        // Trier les clés (scores de centralité) par ordre décroissant
        List<Double> keys = new ArrayList<>(map.keySet());
        keys.sort(Collections.reverseOrder());

        // Construire la liste triée des livres
        List<Livre> res = new ArrayList<>();
        for (Double key : keys) {
            res.addAll(map.get(key)); // Ajoute tous les livres associés à la clé actuelle
        }
        return res;
    }

    /**
     * Service pour trier une liste de livres par centralité de proximité.
     *
     * @param livres Liste des livres.
     * @return Une liste de livres triée par centralité décroissante.
     * @throws Exception En cas d'erreur lors du prétraitement ou du tri.
     */
    public List<Livre> sortByClosenessCentralityService(List<Livre> livres) throws Exception {
        // Effectue le prétraitement et retourne la liste triée
        return sortByClosenessCentrality(preClosenessCentrality(livres));
    }


    /*********************************************************************************************/
    /*       Méthode pour calculer et sauvegarder le graph de Jaccard de la bibliothèque         */

    /*********************************************************************************************/

    public void buildGraph() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String jsonFilePath = "src/main/java/org/example/projet_final_daar/data/books.json";
        File file = new File(jsonFilePath);
        if (file.exists()) {
            // Lire les livres depuis le fichier JSON
            List<Livre> livres = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Livre[].class)));

            // Extrait les mots de chaque livre
            Map<String, List<String>> extractWordsFromBooks = extractWordsFromBooks(livres);

            JaccardGraph graph = new JaccardGraph();
            for (Livre livre : livres) {
                buildJaccardGraph(graph, extractWordsFromBooks, Integer.toString(livre.getId()));
                extractWordsFromBooks.remove(Integer.toString(livre.getId()));
                System.out.println("Graph Created : " + livre.getId());
            }
            graph.saveGraphToTxt("src/main/java/org/example/projet_final_daar/data/graph",10000 );
        }

        System.out.println("Graph Created");
    }
}
