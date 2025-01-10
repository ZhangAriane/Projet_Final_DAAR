package org.example.projet_final_daar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.projet_final_daar.model.KPM.KMPWithCarryOver;
import org.example.projet_final_daar.model.Livre;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KPMWithCarryOverService {

    /**
     * Cherche un motif dans un fichier avec la méthode KMP
     * Retourne true si le motif existe dans le fichier
     * @param motif motif à chercher dans le fichier
     * @param urlTxt l'url du fichier texte
     */
    private  Boolean searchMotifInURLKMP(String motif, String urlTxt) {
        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(new URL(urlTxt).openStream()))) {
            String ligne;

            // Application de la méthode sur chaque ligne du fichier depuis l'URL
            while ((ligne = lecteur.readLine()) != null) {
                StringBuilder texte = new StringBuilder();

                if (!ligne.isEmpty()) {
                    for (int i = 0; i < ligne.length(); i++) {
                        // Si un caractère appartient à l'ASCII
                        if ((int) ligne.charAt(i) < 256) {
                            texte.append(ligne.charAt(i));
                        } else {
                            // Remplace les caractères spéciaux (hors ASCII) par un point d'interrogation
                            texte.append("?");
                        }
                    }

                    // Appel de la méthode KMP pour rechercher le motif
                    if (KMPWithCarryOver.kmpRecherche(texte.toString(), motif)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public  List<Livre> searchMotifInAllURLKMP(String motif) {
        List<Livre> res = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            String jsonFilePath = "src/main/java/org/example/projet_final_daar/data/books.json";
            File file = new File(jsonFilePath);
            if (file.exists()) {
                // Lire les livres depuis le fichier JSON
                List<Livre> livres =  new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Livre[].class)));

                for(Livre livre : livres) {
                    if(searchMotifInURLKMP(motif,livre.getTxt())) {
                        res.add(livre);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    
}
