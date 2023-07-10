package com.bookstore.model;

import com.bookstore.dto.BookDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String author;
    private String bookName;
    private String type;
    private String genre;
    private int volume;

    public Book(BookDTO bookDTO) {
        this.author = bookDTO.getAuthor();
        this.bookName = bookDTO.getBookName();
        this.type = bookDTO.getType();
        this.genre = bookDTO.getGenre();
        this.volume = bookDTO.getVolume();
    }
}
