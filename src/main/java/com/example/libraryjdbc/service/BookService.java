package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.BookCreateRequest;
import com.example.libraryjdbc.dto.BookResponse;
import com.example.libraryjdbc.entity.Book;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.mapper.BookMapper;
import com.example.libraryjdbc.repository.BookRepository;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) { this.repo = repo; }

    public BookResponse create(BookCreateRequest r) {
        repo.findByIsbn(r.getIsbn()).ifPresent(b -> {
            throw new LibraryException("Book already exists", 409);
        });

        Book book = BookMapper.toEntity(r);  // <-- Mapper kullanıldı
        return BookMapper.toResponse(repo.save(book));  // <-- Mapper kullanıldı
    }

    public List<BookResponse> getAllBooks() {
        return repo.findAll().stream()
                .map(BookMapper::toResponse)  // <-- Mapper kullanıldı
                .toList();
    }

    public BookResponse getBookById(Long id) {
        return repo.findById(id)
                .map(BookMapper::toResponse)  // <-- Mapper kullanıldı
                .orElseThrow(() -> new LibraryException("Book not found", 404));
    }

    public List<BookResponse> searchBooks(String title) {
        return repo.findByTitleContainingIgnoreCase(title).stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    public List<BookResponse> getOutOfStockBooks() {
        return repo.findByAvailableQuantity(0).stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    public boolean bookExists(Long id) {
        return repo.existsById(id);
    }

    public void updateAvailableQuantity(Long id, int change) {
        Book book = repo.findById(id).orElseThrow();
        book.setAvailableQuantity(book.getAvailableQuantity() + change);
        repo.save(book);
    }
}