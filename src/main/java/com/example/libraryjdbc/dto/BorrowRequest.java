package com.example.libraryjdbc.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequest {

    @NotNull(message = "Kitab ID`si teleb olunur")
    private Long bookId;

    @NotNull(message = "Telebe ID`si teleb olunur")
    private Long studentId;

    public Long getBookId() { return bookId; }
    public Long getStudentId() { return studentId; }

    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
}
