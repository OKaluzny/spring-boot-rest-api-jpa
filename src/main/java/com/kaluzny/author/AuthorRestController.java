package com.kaluzny.author;

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
@RestController("author")
@RequestMapping("/api/v1/")
public class AuthorRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRestController.class);

    private AuthorService authorService;

    @Inject
    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /* Create a author */
    @RequestMapping(
            value = "authors",
            method = RequestMethod.POST)
    public ResponseEntity<Author> createAuthor(@RequestBody Author author, UriComponentsBuilder ucBuilder) {
        LOGGER.debug(">>> Creating author with id: " + author.getId());
        if (authorService.isExist(author)) {
            LOGGER.debug("An author with name " + author.getId() + "exist.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        authorService.save(author);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("authors/{id}").buildAndExpand(author.getId()).toUri());
        return new ResponseEntity<>(author, headers, HttpStatus.CREATED);
    }

    /* Reading single author */
    @RequestMapping(
            value = "authors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthor(@PathVariable("id") int id) {
        LOGGER.debug("Fetching author with id: " + id);
        Author author = authorService.findById(id);
        if (author == null) {
            LOGGER.debug("Author with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /*Reads all authors*/
    @RequestMapping(
            value = "authors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> listAllAuthors() {
        LOGGER.debug("Received request to list all authors");
        List<Author> authors = authorService.findAll();
        if (authors.isEmpty()) {
            LOGGER.debug("authors do not have.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    /* Update a author*/
    @RequestMapping(
            value = "authors/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Author> updateFromDB(@PathVariable("id") int id,
                                               @RequestBody Author author) {
        LOGGER.debug(">>> Updating author with id: " + id);
        Author currentAuthor = authorService.findById(id);

        if (currentAuthor == null) {
            LOGGER.debug("<<< Authors with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentAuthor.setModified(author.getModified());
        currentAuthor.setName(author.getName());
        currentAuthor.setBooks(author.getBooks());
        currentAuthor.setAddress(author.getAddress());

        authorService.update(currentAuthor);
        return new ResponseEntity<>(currentAuthor, HttpStatus.OK);
    }

    /*Delete a author */
    @RequestMapping(
            value = "authors/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Author> deleteAuthorFromDB(@PathVariable("id") int id) {
        LOGGER.debug("Fetching & Deleting author with id: " + id + " is successfully removed from database!");

        Author author = authorService.findById(id);
        if (author == null) {
            LOGGER.debug("Unable to delete. Author with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

     /*Delete all authors*/
    @RequestMapping(
            value = "authors",
            method = RequestMethod.DELETE)
    public ResponseEntity<Author> deleteAllAuthors() {
        authorService.deleteAll();
        LOGGER.debug("Removed all authors from database!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAlreadyExistsException(AlreadyExistsException exception) {
        return exception.getMessage();
    }
}
