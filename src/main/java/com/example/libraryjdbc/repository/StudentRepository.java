package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StudentResponse> studentRowMapper = (rs, rowNum) -> new StudentResponse(
            rs.getLong("id"),
            rs.getString("full_name"),  // Bu ne la? Veritabanında snake_case, Java'da camelCase
            rs.getString("email"),
            rs.getString("phone"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public StudentResponse save(StudentCreateRequest request) {
        String sql = """
            INSERT INTO students (full_name, email, phone, created_at)
            VALUES (?, ?, ?, NOW())
            RETURNING *
            """;
        return jdbcTemplate.queryForObject(sql, studentRowMapper,
                request.getFullName(),
                request.getEmail(),
                request.getPhone()
        );
    }

    public List<StudentResponse> findAll() {
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    public StudentResponse findById(Long id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public StudentResponse findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM students WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
