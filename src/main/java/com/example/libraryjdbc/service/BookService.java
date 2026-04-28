package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.BookCreateRequest;
import com.example.libraryjdbc.dto.BookResponse;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) { this.repo = repo; }

    @Transactional
    public BookResponse createBook(BookCreateRequest r) {
        if (repo.findByIsbn(r.getIsbn()) != null) throw new LibraryException("ISBN exists", 409);
        return repo.save(r);
    }

    public List<BookResponse> getAllBooks() { return repo.findAll(); }

    public BookResponse getBookById(Long id) {
        BookResponse b = repo.findById(id);
        if (b == null) throw new LibraryException("Book not found", 404);
        return b;
    }

    public List<BookResponse> searchBooks(String title) {
        return repo.searchByTitle(title);
    }

    public List<BookResponse> getOutOfStockBooks() {
        return repo.findOutOfStock();
    }

    public boolean bookExists(Long id) {
        return repo.existsById(id);
    }

    public void updateAvailableQuantity(Long bookId, int change) {
        repo.updateAvailableQuantity(bookId, change);
    }
}