package org.example.projet_final_daar.controller;

import org.example.projet_final_daar.model.Livre;
import org.example.projet_final_daar.model.closenessCentrality.ClosenessCentrality;
import org.example.projet_final_daar.service.AutomatonService;
import org.example.projet_final_daar.service.ClosenessCentralityService;
import org.example.projet_final_daar.service.KPMWithCarryOverService;
import org.example.projet_final_daar.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private KPMWithCarryOverService kpmWithCarryOverService;

    @Autowired
    private AutomatonService automatonService;

    @Autowired
    private ClosenessCentralityService closenessCentralityService;

    @GetMapping("/hello")
    public String helloWorld() {
        return "HelloWorld!";
    }

    @GetMapping("/livres")
    public List<Livre> getAllBooks() {
        return livreService.getAllBooks();
    }

    @GetMapping("/livres/{id}")
    public ResponseEntity<Livre> getBookById(@PathVariable Integer id) {
        Livre livre = livreService.getBookById(id);
        if (livre != null) {
            return ResponseEntity.ok(livre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/livres/search/{motif}")
    public List<Livre> getBookByMotif(@PathVariable String motif) {
        return kpmWithCarryOverService.searchMotifInAllURLKMPSortByOcurrences(motif);
    }


    @GetMapping("/livres/advancedSearch/{regEx}")
    public List<Livre>  getBookByRegEx(@PathVariable String regEx) {
        return  automatonService.searchMotifInAllURLAutomatonSortByOcurrences(regEx);
    }

    @GetMapping("/livres/searchSortByClosenessCentrality/{motif}")
    public List<Livre> getBookByMotifAndSortByClosenessCentrality(@PathVariable String motif) throws Exception {
        return closenessCentralityService.sortByClosenessCentralityService(kpmWithCarryOverService.searchMotifInAllURLKMP(motif));
    }


    @GetMapping("/livres/advancedSearchSortByClosenessCentrality/{regEx}")
    public List<Livre>  getBookByRegExAndSortByClosenessCentrality(@PathVariable String regEx) throws Exception {
        return  closenessCentralityService.sortByClosenessCentralityService(automatonService.searchMotifInAllURLAutomaton(regEx));
    }


    @PostMapping("/livres/charge")
    public void chargeBook(){
        for(int i=1; i<10000;i++) {
            livreService.fetchBooks(i);
        }
    }
}
