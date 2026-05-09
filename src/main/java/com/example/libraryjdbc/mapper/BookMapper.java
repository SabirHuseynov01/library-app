package com.example.libraryjdbc.mapper;


import com.example.libraryjdbc.dto.BookCreateRequest;
import com.example.libraryjdbc.dto.BookResponse;
import com.example.libraryjdbc.entity.Book;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookMapper {

    public static BookResponse toResponse(Book book) {
        if (book == null) return null;

        return new BookResponse(
                book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getQuantity(), book.getAvailableQuantity(), book.getCreatedAt()
        );
    }

    public static Book toEntity(BookCreateRequest request) {
        if (request == null) return null;

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setQuantity(request.getQuantity());
        book.setAvailableQuantity(request.getQuantity());
        return book;
    }
}
