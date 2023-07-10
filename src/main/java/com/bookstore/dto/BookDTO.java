package com.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String author;
    private String bookName;
    private String type;
    private String genre;
    private int volume;
    private long id;
}
