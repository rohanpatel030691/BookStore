package com.bookstore.services.interfaces;

import com.bookstore.dto.BookDTO;

import java.util.List;

public interface IBookService {
    BookDTO addBook(BookDTO book);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    Boolean removeBook(Long bookId);

    List<BookDTO> getBooks(String type,int volume,String author,String genre,String excludeGenre);
}
