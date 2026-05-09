package com.example.libraryjdbc.controller;


import com.example.libraryjdbc.dto.BorrowRequest;
import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowController {

    private final BorrowService service;

    public BorrowController(BorrowService service) { this.service = service; }

    @PostMapping("/borrows")
    public ResponseEntity<BorrowResponse> borrow(@Valid @RequestBody BorrowRequest r) {
        return ResponseEntity.ok(service.borrowBook(r));
    }

    @PostMapping("/borrows/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        service.returnBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/borrows")
    public ResponseEntity<List<BorrowResponse>> getAll() {
        return ResponseEntity.ok(service.getAllBorrows());
    }

    @GetMapping("/borrows/active")
    public ResponseEntity<List<BorrowResponse>> getActive() {
        return ResponseEntity.ok(service.getActiveBorrows());
    }

    @GetMapping("/students/{studentId}/borrows")
    public ResponseEntity<List<BorrowResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getByStudent(studentId));
    }

    @GetMapping("/reports/most-borrowed-books")
    public ResponseEntity<List<BorrowService.BookBorrowCount>> getMostBorrowed() {
        return ResponseEntity.ok(service.getMostBorrowed());
    }
}