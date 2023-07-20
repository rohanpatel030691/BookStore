package com.bookstore.services.implementation;

import com.bookstore.dto.BookDTO;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.services.interfaces.IBookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @PersistenceContext
    public EntityManager entityManager;

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        logger.info("Adding a new book: {}", bookDTO);
        Book book = new Book(bookDTO);
        return BookMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        logger.info("Updating book with ID {}: {}", bookId, bookDTO);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            BookMapper.updateBookFromDTO(existingBook, bookDTO);
            existingBook = bookRepository.save(existingBook);
            logger.info("Updated book: {}", existingBook);
            return BookMapper.toDTO(existingBook);
        } else {
            // Book with the given ID does not exist
            logger.warn("Book with ID {} not found", bookId);
            throw new BookNotFoundException("Book with given ID does not exist in the database.");
        }
    }

    @Override
    public Boolean removeBook(Long bookId) {
        logger.info("Removing book with ID: {}", bookId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            bookRepository.delete(existingBook);
            logger.info("Book with ID {} removed successfully", bookId);
            return true;
        } else {
            // Book with the given ID does not exist
            logger.warn("Book with ID {} not found", bookId);
            throw new BookNotFoundException("Book with given ID does not exist in the database.");
        }
    }

    @Override
    public List<BookDTO> getBooks(String type,int volume,String author,String genre,String excludeGenre) {
        logger.info("Fetching books with parameters: type={}, volume={}, author={}, genre={}, excludeGenre={}",
                type, volume, author, genre, excludeGenre);
        List<Book> books = filterBooks(type,volume,genre,author,excludeGenre);

        List<BookDTO> bookDTOs = new ArrayList<>();

        for (Book book : books) {
            BookDTO bookDTO = BookMapper.toDTO(book);
            bookDTOs.add(bookDTO);
        }
        logger.info("Found {} books matching the criteria", bookDTOs.size());
        return bookDTOs;
    }

    public List<Book> filterBooks(String type, int volume, String genre, String author,String excludeGenre) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();

        if (type != null) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        if (volume > 0) {
            predicates.add(criteriaBuilder.greaterThan(root.get("volume"), volume));
        }

        if (author != null) {
            predicates.add(criteriaBuilder.equal(root.get("author"), author));
        }

        if (genre != null && excludeGenre == null) {
            predicates.add(criteriaBuilder.equal(root.get("genre"), genre));
        }

        if (excludeGenre != null) {
            predicates.add(criteriaBuilder.notEqual(root.get("genre"), excludeGenre));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
