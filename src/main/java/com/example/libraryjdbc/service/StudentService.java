package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    @Transactional
    public StudentResponse createStudent(StudentCreateRequest r) {
        if (repo.findByEmail(r.getEmail()) != null) throw new LibraryException("Email exists", 409);
        return repo.save(r);
    }

    public List<StudentResponse> getAllStudents() { return repo.findAll(); }

    public StudentResponse getStudentById(Long id) {
        StudentResponse s = repo.findById(id);
        if (s == null) throw new LibraryException("Student not found", 404);
        return s;
    }

    public boolean studentExists(Long id) {
        return repo.existsById(id);
    }
}