package com.example.libraryjdbc.exception;

public class LibraryException extends RuntimeException {

    private final int status;

    public LibraryException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() { return status; }
}
