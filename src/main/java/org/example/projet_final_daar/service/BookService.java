package org.example.projet_final_daar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;
import org.example.projet_final_daar.model.Book;
import org.example.projet_final_daar.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void fetchBooks(int id) {
        String apiUrl = "https://gutendex.com/books/" + id;
        String jsonFile = "src/main/java/org/example/projet_final_daar/data/books.json";

        try {
            // Requête HTTP GET pour récupérer les données JSON
            String jsonResponse = Request.get(apiUrl)
                    .connectTimeout(Timeout.ofSeconds(10))
                    .execute()
                    .returnContent()
                    .asString();

            ObjectMapper objectMapper = new ObjectMapper();
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
                    Book book = new Book(title, authors.toString(), coverImage, txtUrl);
                    bookRepository.save(book);
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
                wordCount += line.trim().split(" ").length;
                if (wordCount >= 10000)
                    return true;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du texte depuis l'URL : " + e.getMessage());
        }
        return false;
    }
}
