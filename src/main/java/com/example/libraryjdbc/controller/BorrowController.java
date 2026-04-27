package com.example.libraryjdbc.controller;

import com.example.libraryjdbc.dto.BorrowRequest;
import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.repository.BorrowRepository;
import com.example.libraryjdbc.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrows")
    public ResponseEntity<BorrowResponse> borrowBook(BorrowRequest request){
        return ResponseEntity.ok(borrowService.borrowBook(request));
    }

    @PostMapping("/borrows/{borrowId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowId){
        borrowService.returnBook(borrowId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/borrows")
    public ResponseEntity<List<BorrowResponse>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @GetMapping("/borrows/active")
    public ResponseEntity<List<BorrowResponse>> getActiveBorrows() {
        return ResponseEntity.ok(borrowService.getActiveBorrows());
    }

    @GetMapping("/{studentId}/borrows")
    public ResponseEntity<List<BorrowResponse>> getStudentBorrows(@PathVariable Long studentId){
        return ResponseEntity.ok(borrowService.getStudentBorrows(studentId));
    }

    @GetMapping("/reports/most-borrowed-books")
    public ResponseEntity<List<BorrowRepository.BookBorrowCount>> getMostBorrowedBooks() {
        return ResponseEntity.ok(borrowService.getMostBorrowedBooks());
    }
}
