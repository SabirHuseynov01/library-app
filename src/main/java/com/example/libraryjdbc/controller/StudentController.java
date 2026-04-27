package com.example.libraryjdbc.controller;

import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.dto.StudentCreateRequest;
import com.example.libraryjdbc.dto.StudentResponse;
import com.example.libraryjdbc.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest request){
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

}
