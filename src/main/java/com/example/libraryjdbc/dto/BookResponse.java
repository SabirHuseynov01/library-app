package com.example.libraryjdbc.dto;

import java.time.LocalDateTime;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int quantity;
    private int availableQuantity;
    private LocalDateTime createdAt;

    public BookResponse(Long id, String title, String author, String isbn,
                        int quantity, int availableQuantity, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getIsbn() { return isbn; }

    public int getQuantity() { return quantity; }

    public int getAvailableQuantity() { return availableQuantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
