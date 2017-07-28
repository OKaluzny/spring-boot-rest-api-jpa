package com.kaluzny.book;

import com.kaluzny.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;

@Service
@RestController("book")
@RequestMapping("/api/v1/")
public class BookRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

    private BookService bookService;

    @Inject
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /* Create a book */
    @RequestMapping(
            value = "books",
            method = RequestMethod.POST)
    public ResponseEntity<Book> createBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {
        LOGGER.debug(">>> Creating book with id: " + book.getId());
        if (bookService.isExist(book)) {
            LOGGER.debug("A book with name " + book.getId() + "exist.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        bookService.save(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("books/{id}").buildAndExpand(book.getId()).toUri());
        return new ResponseEntity<>(book, headers, HttpStatus.CREATED);
    }

    /* Reading single book */
    @RequestMapping(
            value = "books/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable("id") int id) {
        LOGGER.debug("Fetching book with id: " + id);
        Book book = bookService.findById(id);
        if (book == null) {
            LOGGER.debug("Book with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

     /*Reads all books*/
    @RequestMapping(
            value = "books",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> listAllBooks() {
        LOGGER.debug("Received request to list all books");
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            LOGGER.debug("Books do not have.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

     /*Update a book*/
    @RequestMapping(
            value = "books/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Book> updateBookFromDB(@PathVariable("id") int id,
                                                 @RequestBody Book book) {
        LOGGER.debug(">>> Updating book with id: " + id);
        Book currentBook = bookService.findById(id);

        if (currentBook == null) {
            LOGGER.debug("<<< Book with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentBook.setModified(book.getModified());
        currentBook.setName(book.getName());
        currentBook.setAuthor(book.getAuthor());

        currentBook.setPublisher(book.getPublisher());
        currentBook.setYear(book.getYear());
        currentBook.setPages(book.getPages());

        bookService.update(currentBook);
        return new ResponseEntity<>(currentBook, HttpStatus.OK);
    }

     /*Delete a book */
    @RequestMapping(
            value = "books/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Book> deleteBookFromDB(@PathVariable("id") int id) {
        LOGGER.debug("Fetching & Deleting book with id: " + id + " is successfully removed from database!");

        Book book = bookService.findById(id);
        if (book == null) {
            LOGGER.debug("Unable to delete. Book with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

     /*Delete all books*/
    @RequestMapping(
            value = "books",
            method = RequestMethod.DELETE)
    public ResponseEntity<Book> deleteAllBooks() {
        bookService.deleteAll();
        LOGGER.debug("Removed all books from database!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExistsException(AlreadyExistsException exception) {
        return exception.getMessage();
    }
}
