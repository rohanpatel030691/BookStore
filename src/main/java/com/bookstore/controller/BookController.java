package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.services.interfaces.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final IBookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookController(IBookService bookService){
        this.bookService=bookService;
    }

    @PostMapping
    public ResponseEntity addBooks(@RequestBody BookDTO bookDTO){
        logger.info("Adding a new book: {}", bookDTO);
        BookDTO book=bookService.addBook(bookDTO);
        logger.info("Book added: {}", book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity updateBooks(@PathVariable Long bookId,@RequestBody BookDTO bookDTO){
        logger.info("Updating book with ID {}: {}", bookId, bookDTO);
        BookDTO book = bookService.updateBook(bookId, bookDTO);
        logger.info("Updated book: {}", book);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeBook(@PathVariable Long bookId){
        logger.info("Removing book with ID: {}", bookId);
        boolean isRemoved = bookService.removeBook(bookId);
        if(isRemoved){
            logger.info("Book with ID {} removed successfully", bookId);
            return ResponseEntity.ok("Book removed successfully");
        } else {
            logger.warn("Book with ID {} not found", bookId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(@RequestParam(value="type",required = false) String type,
                                                     @RequestParam(value="volumeGreaterThan",required = false) Integer volume,
                                                     @RequestParam(value="author",required = false) String author,
                                                     @RequestParam(value="genre",required = false) String genre,
                                                     @RequestParam(value="excludeGenre",required = false) String excludeGenre){

        // Handle null volume parameter
        int volumeValue = volume != null ? volume.intValue() : 0;

        logger.info("Fetching books with parameters: type={}, volumeGreaterThan={}, author={}, genre={}, excludeGenre={}",
                type, volumeValue, author, genre, excludeGenre);

        List<BookDTO> bookDTOS = bookService.getBooks(type, volumeValue, author, genre, excludeGenre);
        logger.info("Found {} books matching the criteria", bookDTOS.size());

        return ResponseEntity.ok().body(bookDTOS);
    }

}
