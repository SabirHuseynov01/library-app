package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import com.example.libraryjdbc.entity.Student;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.mapper.StudentMapper;
import com.example.libraryjdbc.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public StudentResponse createStudent(StudentCreateRequest r) {
        repo.findByEmail(r.getEmail()).ifPresent(s -> {
            throw new LibraryException("Email exists", 409);
        });

        Student student = StudentMapper.toEntity(r);  // <-- Mapper
        return StudentMapper.toResponse(repo.save(student));  // <-- Mapper
    }

    public List<StudentResponse> getAllStudents() {
        return repo.findAll().stream()
                .map(StudentMapper::toResponse)
                .toList();
    }

    public StudentResponse getStudentById(Long id) {
        return repo.findById(id)
                .map(StudentMapper::toResponse)
                .orElseThrow(() -> new LibraryException("Student not found", 404));
    }

    public boolean studentExists(Long id) {
        return repo.existsById(id);
    }
}