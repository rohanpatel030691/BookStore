package com.bookstore.services;

import com.bookstore.dto.BookDTO;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.services.implementation.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;


    @Test
    public void testAddBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Test Author");
        bookDTO.setBookName("Test Book");
        Book savedBook = new Book(bookDTO);
        savedBook.setId(1L);

        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(savedBook);

        BookDTO result = bookService.addBook(bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getBookName(), result.getBookName());
        verify(bookRepository, times(1)).save(Mockito.any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Updated Author");
        bookDTO.setBookName("Updated Book");
        Book existingBook = new Book();
        existingBook.setId(bookId);

        when(bookRepository.findById(Mockito.eq(bookId))).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(existingBook);

        BookDTO result = bookService.updateBook(bookId, bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getBookName(), result.getBookName());
        verify(bookRepository, times(1)).findById(Mockito.eq(bookId));
        verify(bookRepository, times(1)).save(Mockito.any(Book.class));
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBookNotFound() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();

        when(bookRepository.findById(Mockito.eq(bookId))).thenReturn(Optional.empty());

        bookService.updateBook(bookId, bookDTO);
    }

    @Test
    public void testRemoveBook_BookExists() {
        long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        boolean result = bookService.removeBook(bookId);

        verify(bookRepository, times(1)).delete(book);

        assertTrue(result);
    }

    @Test(expected = BookNotFoundException.class)
    public void testRemoveBook_BookNotExists() {
        long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        bookService.removeBook(bookId);
    }
}

