package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.dto.BookCreateRequest;
import com.example.libraryjdbc.dto.BookResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookResponse> bookRowMapper = (rs, rowNum) -> new BookResponse(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getInt("quantity"),
            rs.getInt("available_quantity"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public BookResponse save(BookCreateRequest request) {
        String sql = """
            INSERT INTO books (title, author, isbn, quantity, available_quantity, created_at)
            VALUES (?, ?, ?, ?, ?, NOW())
            RETURNING *
            """;
        return jdbcTemplate.queryForObject(sql, bookRowMapper,
                request.getTitle(), request.getAuthor(), request.getIsbn(), request.getQuantity());
    }

    public List<BookResponse> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }


    public BookResponse findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, bookRowMapper, id);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<BookResponse> searchByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        return jdbcTemplate.query(sql, bookRowMapper, "%" + title + "%");
    }

    public void updateAvailableQuantity(Long bookId, int change) {
        String sql = "UPDATE books SET available_quantity = available_quantity + ? WHERE id = ?";
        jdbcTemplate.update(sql, change, bookId);
    }


    public List<BookResponse> findOutOfStock() {
        String sql = "SELECT * FROM books WHERE available_quantity = 0";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
