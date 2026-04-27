package com.example.libraryjdbc.dto;


import java.time.LocalDate;

public class BorrowResponse {

    private Long id;
    private String bookTitle;
    private String studentName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;

    public BorrowResponse(Long id, String bookTitle, String studentName,
                          LocalDate borrowDate, LocalDate returnDate, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.studentName = studentName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getBookTitle() { return bookTitle; }
    public String getStudentName() { return studentName; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

}
