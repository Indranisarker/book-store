package com.example.Online.Book.Store.repository;

import com.example.Online.Book.Store.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Book, Long> {
    Book findBookByNameIgnoreCase(String name);
    // Define a method to find a book by its ID along with its reviews
    @EntityGraph(attributePaths = "reviews")
    Optional<Book> findById(Long id);

}
