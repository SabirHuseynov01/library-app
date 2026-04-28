package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.dto.BookCreateRequest;
import com.example.libraryjdbc.dto.BookResponse;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<BookResponse> mapper = (rs, row) -> new BookResponse(
            rs.getLong("id"), rs.getString("title"), rs.getString("author"),
            rs.getString("isbn"), rs.getInt("quantity"),
            rs.getInt("available_quantity"), rs.getTimestamp("created_at").toLocalDateTime()
    );

    public BookRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public BookResponse save(BookCreateRequest r) {
        String sql = "INSERT INTO books (title, author, isbn, quantity, available_quantity, created_at) VALUES (?, ?, ?, ?, ?, NOW()) RETURNING *";
        return jdbc.queryForObject(sql, mapper, r.getTitle(), r.getAuthor(), r.getIsbn(), r.getQuantity(), r.getQuantity());
    }

    public List<BookResponse> findAll() {
        return jdbc.query("SELECT * FROM books", mapper);
    }

    public BookResponse findById(Long id) {
        try {
            return jdbc.queryForObject("SELECT * FROM books WHERE id = ?", mapper, id);
        } catch (EmptyResultDataAccessException e) { return null; }
    }

    public BookResponse findByIsbn(String isbn) {
        try {
            return jdbc.queryForObject("SELECT * FROM books WHERE isbn = ?", mapper, isbn);
        } catch (EmptyResultDataAccessException e) { return null; }
    }

    public List<BookResponse> searchByTitle(String title) {
        return jdbc.query("SELECT * FROM books WHERE title ILIKE ?", mapper, "%" + title + "%");
    }

    public void updateAvailableQuantity(Long bookId, int change) {
        jdbc.update("UPDATE books SET available_quantity = available_quantity + ? WHERE id = ?", change, bookId);
    }

    public List<BookResponse> findOutOfStock() {
        return jdbc.query("SELECT * FROM books WHERE available_quantity = 0", mapper);
    }

    public boolean existsById(Long id) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM books WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }
}