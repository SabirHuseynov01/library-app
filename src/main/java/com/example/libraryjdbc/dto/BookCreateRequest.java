package com.example.libraryjdbc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookCreateRequest {

    @NotBlank
    private String title;
    private String author;

    @NotBlank
    private String isbn;

    @Min(0)
    private int quantity;

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getQuantity() { return quantity; }


    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
