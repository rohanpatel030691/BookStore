package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.services.interfaces.IBookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private IBookService bookService;

    @Test
    public void testAddBooks() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Test Author");
        bookDTO.setBookName("Test Book");
        bookDTO.setGenre("Fiction");
        bookDTO.setType("eBook");
        bookDTO.setVolume(4);
        Mockito.when(bookService.addBook(Mockito.any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<?> response = bookController.addBooks(bookDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
        Mockito.verify(bookService, Mockito.times(1)).addBook(Mockito.any(BookDTO.class));
    }

    @Test
    public void testUpdateBooks() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Updated Author");
        bookDTO.setBookName("Updated Book");
        bookDTO.setGenre("Fiction");
        bookDTO.setType("eBook");
        bookDTO.setVolume(4);
        Mockito.when(bookService.updateBook(Mockito.eq(bookId), Mockito.any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<?> response = bookController.updateBooks(bookId, bookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
        Mockito.verify(bookService, Mockito.times(1)).updateBook(Mockito.eq(bookId), Mockito.any(BookDTO.class));
    }

    @Test
    public void testRemoveBook() {
        Long bookId = 1L;
        boolean isRemoved = true;
        Mockito.when(bookService.removeBook(Mockito.eq(bookId))).thenReturn(isRemoved);

        ResponseEntity<String> response = bookController.removeBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book removed successfully", response.getBody());
        Mockito.verify(bookService, Mockito.times(1)).removeBook(Mockito.eq(bookId));
    }

    @Test
    public void testRemoveBookNotFound() {
        Long bookId = 1L;
        boolean isRemoved = false;
        Mockito.when(bookService.removeBook(Mockito.eq(bookId))).thenReturn(isRemoved);

        ResponseEntity<String> response = bookController.removeBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(bookService, Mockito.times(1)).removeBook(Mockito.eq(bookId));
    }

    @Test
    public void testGetAllBooks() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Test Author");
        bookDTO.setBookName("Test Book");
        bookDTO.setGenre("Fiction");
        bookDTO.setType("eBook");
        bookDTO.setVolume(4);
        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(bookDTO);
        Mockito.when(bookService.getBooks(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString())).thenReturn(bookDTOList);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks("", 0, "", "", "");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTOList, response.getBody());
        Mockito.verify(bookService, Mockito.times(1)).getBooks(Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

}

