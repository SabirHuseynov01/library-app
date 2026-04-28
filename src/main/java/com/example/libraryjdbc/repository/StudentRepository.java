package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<StudentResponse> mapper = (rs, row) -> new StudentResponse(
            rs.getLong("id"), rs.getString("full_name"), rs.getString("email"),
            rs.getString("phone"), rs.getTimestamp("created_at").toLocalDateTime()
    );

    public StudentRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public StudentResponse save(StudentCreateRequest r) {
        String sql = "INSERT INTO students (full_name, email, phone, created_at) VALUES (?, ?, ?, NOW()) RETURNING *";
        return jdbc.queryForObject(sql, mapper, r.getFullName(), r.getEmail(), r.getPhone());
    }

    public List<StudentResponse> findAll() {
        return jdbc.query("SELECT * FROM students", mapper);
    }

    public StudentResponse findById(Long id) {
        try {
            return jdbc.queryForObject("SELECT * FROM students WHERE id = ?", mapper, id);
        } catch (EmptyResultDataAccessException e) { return null; }
    }

    public StudentResponse findByEmail(String email) {
        try {
            return jdbc.queryForObject("SELECT * FROM students WHERE email = ?", mapper, email);
        } catch (EmptyResultDataAccessException e) { return null; }
    }

    public boolean existsById(Long id) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM students WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }
}