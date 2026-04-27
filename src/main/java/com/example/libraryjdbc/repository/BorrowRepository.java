package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.dto.BorrowResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BorrowRepository {

    private final JdbcTemplate jdbcTemplate;

    public BorrowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BorrowResponse> borrowRowMapper = (rs, rowNum) -> new BorrowResponse(
            rs.getLong("id"),
            rs.getString("book_title"),
            rs.getString("student_name"),
            rs.getDate("borrow_date").toLocalDate(),
            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
            rs.getString("status")
    );

    public BorrowResponse save(Long bookId, Long studentId) {
        String sql = """
            INSERT INTO borrow_records (book_id, student_id, borrow_date, status)
            VALUES (?, ?, CURRENT_DATE, 'BORROWED')
            RETURNING id, book_id, student_id, borrow_date, return_date, status
            """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new BorrowResponse(
                rs.getLong("id"),
                rs.getString("book_title"),
                rs.getString("student_name"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                rs.getString("status")
        ));
    }

    public List<BorrowResponse> findAllWithDetails() {
        String sql = """
            SELECT 
                br.id,
                b.title as book_title,
                s.full_name as student_name,
                br.borrow_date,
                br.return_date,
                br.status
            FROM borrow_records br
            JOIN books b ON br.book_id = b.id
            JOIN students s ON br.student_id = s.id
            ORDER BY br.borrow_date DESC
            """;
        return jdbcTemplate.query(sql, borrowRowMapper);
    }

    public List<BorrowResponse> findActiveBorrows() {
        String sql = """
            SELECT 
                br.id,
                b.title as book_title,
                s.full_name as student_name,
                br.borrow_date,
                br.return_date,
                br.status
            FROM borrow_records br
            JOIN books b ON br.book_id = b.id
            JOIN students s ON br.student_id = s.id
            WHERE br.status = 'BORROWED'
            ORDER BY br.borrow_date DESC
            """;
        return jdbcTemplate.query(sql, borrowRowMapper);
    }

    public List<BorrowResponse> findByStudentId(Long studentId) {
        String sql = """
            SELECT 
                br.id,
                b.title as book_title,
                s.full_name as student_name,
                br.borrow_date,
                br.return_date,
                br.status
            FROM borrow_records br
            JOIN books b ON br.book_id = b.id
            JOIN students s ON br.student_id = s.id
            WHERE br.student_id = ?
            ORDER BY br.borrow_date DESC
            """;
        return jdbcTemplate.query(sql, borrowRowMapper, studentId);
    }

    public BorrowRecord findById(Long id) {
        String sql = "SELECT * FROM borrow_records WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new BorrowRecord(
                    rs.getLong("id"),
                    rs.getLong("book_id"),
                    rs.getLong("student_id"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                    rs.getString("status")
            ), id);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateReturnStatus(Long borrowId) {
        String sql = """
            UPDATE borrow_records 
            SET status = 'RETURNED', return_date = CURRENT_DATE 
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, borrowId);
    }

    public boolean hasActiveBorrow(Long studentId, Long bookId) {
        String sql = """
            SELECT COUNT(*) FROM borrow_records 
            WHERE student_id = ? AND book_id = ? AND status = 'BORROWED'
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId, bookId);
        return count != null && count > 0;
    }

    public boolean hasAnyActiveBorrow(Long studentId) {
        String sql = "SELECT COUNT(*) FROM borrow_records WHERE student_id = ? AND status = 'BORROWED'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId);
        return count != null && count > 0;
    }

    public List<BookBorrowCount> findMostBorrowedBooks() {
        String sql = """
            SELECT 
                b.title,
                COUNT(br.id) as borrow_count
            FROM borrow_records br
            JOIN books b ON br.book_id = b.id
            GROUP BY b.id, b.title
            ORDER BY borrow_count DESC
            LIMIT 10
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new BookBorrowCount(
                rs.getString("title"),
                rs.getLong("borrow_count")
        ));
    }


    public static class BorrowRecord{
        private Long id;
        private Long bookId;
        private Long studentId;
        private LocalDate borrowDate;
        private LocalDate returnDate;
        private String status;

        public BorrowRecord(Long id, Long bookId, Long studentId, LocalDate borrowDate, LocalDate returnDate, String status) {
            this.id = id;
            this.bookId = bookId;
            this.studentId = studentId;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
            this.status = status;
        }

        public Long getId() { return id; }
        public Long getBookId() { return bookId; }
        public Long getStudentId() { return studentId; }
        public LocalDate getBorrowDate() { return borrowDate; }
        public LocalDate getReturnDate() { return returnDate; }
        public String getStatus() { return status; }
    }

    public static class BookBorrowCount{
        private String title;
        private Long borrowCount;

        public BookBorrowCount(String title, Long borrowCount) {
            this.title = title;
            this.borrowCount = borrowCount;
        }

        public String getTitle() { return title; }
        public Long getBorrowCount() { return borrowCount; }
    }
}
