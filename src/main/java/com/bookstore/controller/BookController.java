package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.services.interfaces.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final IBookService bookService;

    @Autowired
    public BookController(IBookService bookService){
        this.bookService=bookService;
    }

    @PostMapping
    public ResponseEntity addBooks(@RequestBody BookDTO bookDTO){
        BookDTO book=bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity updateBooks(@PathVariable Long bookId,@RequestBody BookDTO bookDTO){
        BookDTO book = bookService.updateBook(bookId,bookDTO);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeBook(@PathVariable Long bookId){
       boolean isRemoved= bookService.removeBook(bookId);
       if(isRemoved){
           return ResponseEntity.ok("Book removed successfully");
       }else{
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

        List<BookDTO> bookDTOS = bookService.getBooks(type,volumeValue,author,genre,excludeGenre);
        return ResponseEntity.ok().body(bookDTOS);
    }

}
