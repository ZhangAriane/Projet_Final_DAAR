package org.example.projet_final_daar.service;


import org.example.projet_final_daar.model.Livre;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static org.example.projet_final_daar.model.closenessCentrality.ClosenessCentrality.calculateClosenessCentrality2;

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
        // Liste pour stocker les mots extraits
        List<String> words = new ArrayList<>();

        // Connexion à l'URL et lecture du contenu ligne par ligne
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            // Divise chaque ligne en mots en utilisant une regex pour séparer les mots
            String[] tokens = line.split("\\W+");

            // Ajoute les mots qui respectent la condition de longueur (> 4)
            for (String word : tokens) {
                if (word.length() >= 4) {
                    words.add(word);
                }
            }
        }

        reader.close();
        return words; // Retourne la liste des mots
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
        Map<String, List<String>> res = new HashMap<>();
        for (Livre livre : livres) {
            // Extrait les mots depuis l'URL de chaque livre et les associe à son titre
            res.put(Integer.toString(livre.getId()), extractWordsFromUrl(livre.getTxt()));
        }
        return res;
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

        // Extrait les mots de chaque livre
        Map<String, List<String>> extractWordsFromBooks = extractWordsFromBooks(livres);

        for (Livre livre : livres) {
            // Calcule la centralité de proximité pour le livre actuel
            double n = calculateClosenessCentrality2(extractWordsFromBooks, livres.size(), Integer.toString(livre.getId()));
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

}
