package org.example.projet_final_daar.controller;

import org.example.projet_final_daar.model.Book;
import org.example.projet_final_daar.model.Livre;
import org.example.projet_final_daar.service.BookService;
import org.example.projet_final_daar.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livres")
public class LivreController {

    @Autowired
    private LivreService livreService;


    @GetMapping
    public List<Livre> getAllBooks() {
        return livreService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Livre getBookById(@PathVariable int id) {
        return livreService.getBookById(id);
    }


    @PostMapping("/charge")
    public void chargeBook(){
        for(int i=1; i<10000;i++) {
            livreService.fetchBooks(i);
        }
    }
}
