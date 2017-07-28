package com.kaluzny.author;

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
public class AuthorServiceBean implements AuthorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceBean.class);

    private AuthorRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public AuthorServiceBean(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isExist(Author author) {
        return findById(author.getId()) != null;
    }

    @Override
    public Author save(Author author) {
        LOGGER.debug("Save {}", author);
        Author existing = repository.findOne(author.getId());
        if (existing != null) {
            throw new AlreadyExistsException(
                    String.format("There already exists a author with id = %s", author.getId()));
        }
        return repository.save(author);
    }

    @Override
    public Author findById(int id) {
        LOGGER.debug("Search author by id: " + id);
        return repository.findOne(id);
    }

    @Override
    public List<Author> findAll() {
        LOGGER.debug("Retrieve the list of all authors!");
        return repository.findAll();
    }

    @Override
    public Author update(Author author) {
        LOGGER.debug("Author with id: " + author.getId() + " updated! ");
        if (!entityManager.contains(author))
            author = entityManager.merge(author);
        return author;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("Author by id: " + id + " Deleted!");
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("The list all authors deleted!");
        repository.deleteAll();
    }
}