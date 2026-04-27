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

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        StudentResponse existing = studentRepository.findByEmail(request.getEmail());
        if (existing != null){
            throw new LibraryException("Email artıq mövcuddur", 409); // Conflict Error
        }
        return studentRepository.save(request);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll();
    }

    public StudentResponse getStudentById(Long id) {
        StudentResponse student = studentRepository.findById(id);
        if (student == null){
            throw new LibraryException("Student tapilmadi", 404);
        }
        return student;
    }

    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }
}
