package com.kaluzny.book;

import com.kaluzny.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Validated
@Transactional
public class BookServiceBean implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private BookRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public BookServiceBean(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isExist(Book book) {
        return findById(book.getId()) != null;
    }

    @Override
    public Book save(Book book) {
        LOGGER.debug("Save {}", book);
        Book existing = repository.findOne(book.getId());
        if (existing != null) {
            throw new AlreadyExistsException(
                    String.format("There already exists a book with id = %s", book.getId()));
        }
        return repository.save(book);
    }

    @Override
    public Book findById(int id) {
        LOGGER.debug("Search book by id: " + id);
        return repository.findOne(id);
    }

    @Override
    public List<Book> findAll() {
        LOGGER.debug("Retrieve the list of all books!");
        return repository.findAll();
    }

    @Override
    public Book update(Book book) {
        LOGGER.debug("Book with id: " + book.getId() + " updated! ");
        if (!entityManager.contains(book))
            book = entityManager.merge(book);
        return book;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("Book by id: " + id + " Deleted!");
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("The list all books deleted!");
        repository.deleteAll();
    }
}
