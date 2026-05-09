package com.example.libraryjdbc.service;


import com.example.libraryjdbc.dto.BorrowRequest;
import com.example.libraryjdbc.dto.BorrowResponse;
import com.example.libraryjdbc.entity.Book;
import com.example.libraryjdbc.entity.BorrowRecord;
import com.example.libraryjdbc.entity.Student;
import com.example.libraryjdbc.exception.LibraryException;
import com.example.libraryjdbc.mapper.BorrowRecordMapper;
import com.example.libraryjdbc.repository.BookRepository;
import com.example.libraryjdbc.repository.BorrowRecordRepository;
import com.example.libraryjdbc.repository.StudentRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRepo;
    private final BookRepository bookRepo;
    private final StudentRepository studentRepo;

    public BorrowService(BorrowRecordRepository borrowRepo, BookRepository bookRepo, StudentRepository studentRepo) {
        this.borrowRepo = borrowRepo;
        this.bookRepo = bookRepo;
        this.studentRepo = studentRepo;
    }

    public BorrowResponse borrowBook(BorrowRequest r) {
        Book book = bookRepo.findById(r.getBookId()).orElseThrow(() ->
                new LibraryException("Book not found", 404));
        Student student = studentRepo.findById(r.getStudentId()).orElseThrow(() ->
                new LibraryException("Student not found", 404));

        if (book.getAvailableQuantity() <= 0) {
            throw new LibraryException("Book not available", 400);
        }
        if (borrowRepo.existsByStudentIdAndStatus(r.getStudentId(), "BORROWED")) {
            throw new LibraryException("Has unreturned books", 400);
        }

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setStudent(student);

        BorrowRecord saved = borrowRepo.save(record);
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepo.save(book);

        return BorrowRecordMapper.toResponse(saved);  // <-- Mapper
    }

    public void returnBook(Long id) {
        BorrowRecord record = borrowRepo.findById(id).orElseThrow(() ->
                new LibraryException("Borrow not found", 404));

        if (record.getStatus().equals("RETURNED")) {
            throw new LibraryException("Book already returned", 400);
        }

        record.setStatus("RETURNED");
        record.setReturnDate(LocalDate.now());
        borrowRepo.save(record);

        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepo.save(book);
    }

    public List<BorrowResponse> getAllBorrows() {
        return borrowRepo.findAllWithDetails().stream()
                .map(BorrowRecordMapper::toResponse)  // <-- Mapper
                .toList();
    }

    public List<BorrowResponse> getActiveBorrows() {
        return borrowRepo.findActiveWithDetails().stream()
                .map(BorrowRecordMapper::toResponse)
                .toList();
    }

    public List<BorrowResponse> getByStudent(Long studentId) {
        if (!studentRepo.existsById(studentId)) {
            throw new LibraryException("Student not found", 404);
        }
        return borrowRepo.findByStudentIdWithDetails(studentId).stream()
                .map(BorrowRecordMapper::toResponse)
                .toList();
    }

    public List<BookBorrowCount> getMostBorrowed() {
        return borrowRepo.findMostBorrowedBooks().stream()
                .map(row -> new BookBorrowCount((String) row[0], (Long) row[1]))
                .toList();
    }

    public static class BookBorrowCount {
        private String title;
        private Long borrowCount;

        public BookBorrowCount(String title, Long borrowCount) {
            this.title = title;
            this.borrowCount = borrowCount;
        }

        public String getTitle() { return title; }
        public Long getBorrowCount() { return borrowCount; }
    }
}
