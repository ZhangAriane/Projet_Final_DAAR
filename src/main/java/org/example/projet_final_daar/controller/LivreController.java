package org.example.projet_final_daar.controller;

import org.example.projet_final_daar.model.Livre;
import org.example.projet_final_daar.service.AutomatonService;
import org.example.projet_final_daar.service.KPMWithCarryOverService;
import org.example.projet_final_daar.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livres")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private KPMWithCarryOverService kpmWithCarryOverService;

    @Autowired
    private AutomatonService automatonService;


    @GetMapping
    public List<Livre> getAllBooks() {
        return livreService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livre>  getBookById(@RequestParam(required = false) Integer id) {
        Livre livre =  livreService.getBookById(id);
        if (livre != null) {
            return ResponseEntity.ok(livre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{motif}")
    public List<Livre>  getBookByMotif(@RequestParam String motif) {
        return  kpmWithCarryOverService.searchMotifInAllURLKMP(motif);
    }

    @GetMapping("/advancedSearch/{regEx}")
    public List<Livre>  getBookByRegEx(@RequestParam String regEx) {
        return  automatonService.searchMotifInAllURLKMP(regEx);
    }


    @PostMapping("/charge")
    public void chargeBook(){
        for(int i=1; i<10000;i++) {
            livreService.fetchBooks(i);
        }
    }
}
