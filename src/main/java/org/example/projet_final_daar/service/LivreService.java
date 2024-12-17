package org.example.projet_final_daar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;
import org.example.projet_final_daar.model.Book;
import org.example.projet_final_daar.model.Livre;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LivreService {

    private final String jsonFilePath = "src/main/java/org/example/projet_final_daar/data/books.json";

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public List<Livre> getAllBooks() {
        try {
            File file = new File(jsonFilePath);
            if (file.exists()) {
                // Lire les livres depuis le fichier JSON
                return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Livre[].class)));
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public Livre getBookById(int id) {
        List<Livre> livres = getAllBooks();
        if (id >= 0 && id < livres.size()) {
            return livres.get(id); // Récupérer par index
        }
        return null;
    }





    /****************************************************************************************/
    /*                 Méthode récupérer des livres contenant au moins 10000 mots           */
    /****************************************************************************************/
    public void fetchBooks(int id) {
        String apiUrl = "https://gutendex.com/books/" + id;

        try {
            // Requête HTTP GET pour récupérer les données JSON
            String jsonResponse = Request.get(apiUrl)
                    .connectTimeout(Timeout.ofSeconds(10))
                    .execute()
                    .returnContent()
                    .asString();

            JsonNode result = objectMapper.readTree(jsonResponse);

            if (!result.isEmpty() && result.has("title") && result.has("authors")
                    && result.get("formats").has("image/jpeg")
                    && result.get("formats").has("text/plain; charset=utf-8")) {

                String title = result.get("title").asText();
                StringBuilder authors = new StringBuilder();
                for (JsonNode author : result.get("authors")) {
                    authors.append(author.get("name").asText()).append(", ");
                }
                String coverImage = result.get("formats").get("image/jpeg").asText();
                String txtUrl = result.get("formats").get("text/plain; charset=utf-8").asText();

                // Vérifier si le texte contient 10 000 mots
                if (containsTenThousandWordsFromUrl(txtUrl)) {
                    Livre livre = new Livre(title, authors.toString(), coverImage, txtUrl);
                    saveBookToFile(livre);
                    System.out.println("Chargement de livre terminé : " + id);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération du livre " + id + " : " + e.getMessage());
        }
    }

    private boolean containsTenThousandWordsFromUrl(String txtUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(txtUrl).openStream()))) {
            int wordCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                wordCount += line.trim().split("\\s+").length; // Gérer les espaces multiples
                if (wordCount >= 10000) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du texte depuis l'URL : " + e.getMessage());
        }
        return false;
    }

    private void saveBookToFile(Livre livre) {
        List<Livre> livres = getAllBooks(); // Charger les livres existants
        livres.add(livre); // Ajouter le nouveau livre

        try {
            // Écrire tous les livres dans le fichier JSON
            objectMapper.writeValue(new File(jsonFilePath), livres);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du livre dans le fichier JSON : " + e.getMessage());
        }
    }
}
