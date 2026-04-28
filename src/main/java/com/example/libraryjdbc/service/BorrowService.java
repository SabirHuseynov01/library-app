package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.BookResponse;
import com.example.libraryjdbc.dto.BorrowRequest;
import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.repository.BorrowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepo;
    private final BookService bookService;
    private final StudentService studentService;

    public BorrowService(BorrowRepository borrowRepo, BookService bookService, StudentService studentService) {
        this.borrowRepo = borrowRepo;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    @Transactional
    public BorrowResponse borrowBook(BorrowRequest r) {
        if (!bookService.bookExists(r.getBookId())) throw new LibraryException("Book not found", 404);
        if (!studentService.studentExists(r.getStudentId())) throw new LibraryException("Student not found", 404);

        BookResponse book = bookService.getBookById(r.getBookId());
        if (book.getAvailableQuantity() <= 0) throw new LibraryException("Book not available", 400);
        if (borrowRepo.hasAnyActiveBorrow(r.getStudentId())) throw new LibraryException("Has unreturned books", 400);

        BorrowResponse borrow = borrowRepo.save(r.getBookId(), r.getStudentId());
        bookService.updateAvailableQuantity(r.getBookId(), -1);
        return borrow;
    }

    @Transactional
    public void returnBook(Long borrowId) {
        BorrowRepository.BorrowRecord rec = borrowRepo.findById(borrowId);
        if (rec == null) throw new LibraryException("Borrow not found", 404);
        if ("RETURNED".equals(rec.getStatus())) throw new LibraryException("Already returned", 400);

        borrowRepo.updateReturnStatus(borrowId);
        bookService.updateAvailableQuantity(rec.getBookId(), 1);
    }

    public List<BorrowResponse> getAllBorrows() {
        return borrowRepo.findAllWithDetails();
    }

    public List<BorrowResponse> getActiveBorrows() {
        return borrowRepo.findActiveBorrows();
    }

    public List<BorrowResponse> getStudentBorrows(Long studentId) {
        if (!studentService.studentExists(studentId)) throw new LibraryException("Student not found", 404);
        return borrowRepo.findByStudentId(studentId);
    }

    public List<BorrowRepository.BookBorrowCount> getMostBorrowedBooks() {
        return borrowRepo.findMostBorrowedBooks();
    }
}