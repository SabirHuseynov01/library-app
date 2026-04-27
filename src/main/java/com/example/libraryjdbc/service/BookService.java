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

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        BookResponse existing = bookRepository.save(request);
        if (existing != null) {
            throw new LibraryException("ISBN artıq mövcuddur", 409); // Conflict Error
        }
        return bookRepository.save(request);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookResponse getBookById(Long id) {
        BookResponse book = bookRepository.findById(id);
        if (book == null){
            throw new LibraryException("Kitab tapilmadi", 404);
        }
        return book;
    }

    public List<BookResponse> searchBooks(String title) {
        return bookRepository.searchByTitle(title);
    }

    public List<BookResponse> getOutOfStockBooks() {
        return bookRepository.findOutOfStock();
    }

    public boolean bookExists(Long id) {
        return bookRepository.existsById(id);
    }

    public void updateAvailableQuantity(Long bookId, int change) {
        bookRepository.updateAvailableQuantity(bookId, change);
    }
}
