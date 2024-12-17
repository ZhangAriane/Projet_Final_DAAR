package org.example.projet_final_daar.repository;

import org.example.projet_final_daar.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
