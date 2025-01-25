package org.example.projet_final_daar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.projet_final_daar.model.Livre;
import org.example.projet_final_daar.model.automaton.DFAutomaton;
import org.example.projet_final_daar.model.automaton.NDFAutomaton;
import org.example.projet_final_daar.model.automaton.RegEx;
import org.example.projet_final_daar.model.automaton.RegExTree;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class AutomatonService {


    /**
     * Cherche une expression regulière dans un fichier avec la méthode d'automate
     * Retourne true si le fichier contient l'expression regulière
     *
     * @param tree   arbre d'expression regulière
     * @param urlTxt l'URL du fichier texte
     */
    private static int searchRegExpInFileAutomaton(String tree, String urlTxt) throws Exception {
        RegExTree treeRegEx = RegEx.parse(tree);
        int res = 0;
        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(new URL(urlTxt).openStream()))) {
            String ligne;

            //Aplication de la méthode sur chaque ligne du fichier
            while ((ligne = lecteur.readLine()) != null) {
                StringBuilder texte = new StringBuilder();

                if (!ligne.isEmpty()) {

                    for (int i = 0; i < ligne.length(); i++) {
                        //Si un caractère n'appartient pas a ascii
                        if ((int) ligne.charAt(i) < 256)
                            texte.append(ligne.charAt(i));
                        else
                            //Remplace les caractères spéciaux (hors ascii) par un point d'interrogation
                            texte.append("?");
                    }
                    NDFAutomaton ndf = RegExTree.convertTreetoNDF(treeRegEx); ///une fonction récursive
                    DFAutomaton dfa = ndf.convertNFAtoDFA();
                    DFAutomaton dfa_minimized = dfa.minimizeDFA();
                    if (dfa_minimized.search(texte.toString()))
                        res++;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private HashMap<Integer, List<Livre>> preSearchMotifInAllURLAutomaton(String motif) {
        HashMap<Integer, List<Livre>> res = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            String jsonFilePath = "src/main/java/org/example/projet_final_daar/data/books.json";
            File file = new File(jsonFilePath);
            if (file.exists()) {
                // Lire les livres depuis le fichier JSON
                List<Livre> livres = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Livre[].class)));

                for (Livre livre : livres) {
                    int n = searchRegExpInFileAutomaton(motif, livre.getTxt());
                    if (n > 0) {
                        if (!res.containsKey(n)) {
                            ArrayList<Livre> temp = new ArrayList<>();
                            temp.add(livre);
                            res.put(n, temp);
                        } else {
                            res.get(n).add(livre);
                        }
                    }


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    private List<Livre> sortByNumberOccurences(HashMap<Integer, List<Livre>> map) {
        // Trier les clés par ordre décroissant
        List<Integer> keys = new ArrayList<>(map.keySet());
        keys.sort(Collections.reverseOrder());

        // Construire le résultat
        List<Livre> res = new ArrayList<>();
        for (int key : keys) {
            res.addAll(map.get(key));
        }
        return res;
    }


    public List<Livre> searchMotifInAllURLAutomatonSortByOcurrences(String motif) {
        return sortByNumberOccurences(preSearchMotifInAllURLAutomaton(motif));
    }

    public List<Livre> searchMotifInAllURLAutomaton(String motif)  {
        List<Livre> res = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            String jsonFilePath = "src/main/java/org/example/projet_final_daar/data/books.json";
            File file = new File(jsonFilePath);
            if (file.exists()) {
                // Lire les livres depuis le fichier JSON
                List<Livre> livres = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Livre[].class)));

                for (Livre livre : livres) {
                    int n = searchRegExpInFileAutomaton(motif, livre.getTxt());
                    if (n > 0) {
                        res.add(livre);
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    public static void main(String[] args) throws Exception {


        //cherche "Sargon" dans le livre 56667-0 avec KMP
        System.out.println(AutomatonService.searchRegExpInFileAutomaton("Sargon", "https://www.gutenberg.org/files/2069/2069-0.txt"));

    }

}


