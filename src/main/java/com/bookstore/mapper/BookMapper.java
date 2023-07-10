package com.bookstore.mapper;

import com.bookstore.dto.BookDTO;
import com.bookstore.model.Book;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setBookName(book.getBookName());
        bookDTO.setType(book.getType());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setVolume(book.getVolume());
        bookDTO.setId(book.getId());
        return bookDTO;
    }

    public static Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setAuthor(bookDTO.getAuthor());
        book.setBookName(bookDTO.getBookName());
        book.setType(bookDTO.getType());
        book.setGenre(bookDTO.getGenre());
        book.setVolume(bookDTO.getVolume());
        book.setId(bookDTO.getId());
        return book;
    }

    public static void updateBookFromDTO(Book book, BookDTO bookDTO) {
        book.setAuthor(bookDTO.getAuthor());
        book.setBookName(bookDTO.getBookName());
        book.setType(bookDTO.getType());
        book.setGenre(bookDTO.getGenre());
        book.setVolume(bookDTO.getVolume());
    }
}
