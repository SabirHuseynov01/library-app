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

    private final BorrowRepository borrowRepository;
    private final BookService bookService;
    private final StudentService studentService;

    public BorrowService(BorrowRepository borrowRepository, BookService bookService, StudentService studentService) {
        this.borrowRepository = borrowRepository;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    @Transactional
    public BorrowResponse borrowBook(BorrowRequest request) {
        if (!bookService.bookExists(request.getBookId())){
            throw new LibraryException("Kitap tapilmadi", 404);
        }

        if (!studentService.studentExists(request.getStudentId())){
            throw new LibraryException("Student tapilmadi", 404);
        }

        BookResponse book = bookService.getBookById(request.getBookId());
        if (book.getAvailableQuantity() <= 0){
            throw new LibraryException("Kitap artıq mövcud deyil", 400);
        }

        if (borrowRepository.hasAnyActiveBorrow(request.getStudentId())) {
            throw new LibraryException("Tələbənin geri qaytarılmamış kitabları var", 400);
        }

        BorrowResponse borrow =  borrowRepository.save(request.getBookId(), request.getStudentId());
        bookService.updateAvailableQuantity(request.getBookId(), -1);
        return borrow;
    }

    @Transactional
    public void returnBook(Long borrowId) {

        BorrowRepository.BorrowRecord record = borrowRepository.findById(borrowId);
        if (record == null) {
            throw new LibraryException("Borrow record tapilmadi", 404);
        }


        if ("RETURNED".equals(record.getStatus())) {
            throw new LibraryException("Kitab artıq geri qaytarılıb", 400);
        }

        borrowRepository.updateReturnStatus(borrowId);
        bookService.updateAvailableQuantity(record.getBookId(), 1);
    }

    public List<BorrowResponse> getAllBorrows() {
        return borrowRepository.findAllWithDetails();
    }

    public List<BorrowResponse> getActiveBorrows() {
        return borrowRepository.findActiveBorrows();
    }

    public List<BorrowResponse> getStudentBorrows(Long studentId) {
        if (!studentService.studentExists(studentId)) {
            throw new LibraryException("Student not found", 404);
        }
        return borrowRepository.findByStudentId(studentId);
    }

    public List<BorrowRepository.BookBorrowCount> getMostBorrowedBooks() {
        return borrowRepository.findMostBorrowedBooks();
    }
}
