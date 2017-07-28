package com.kaluzny.publisher;

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
public class PublisherServiceBean implements PublisherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherServiceBean.class);

    private PublisherRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public PublisherServiceBean(PublisherRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isExist(Publisher publisher) {
        return findById(publisher.getId()) != null;
    }

    @Override
    public Publisher save(Publisher publisher) {
        LOGGER.debug("Save {}", publisher);
        Publisher existing = repository.findOne(publisher.getId());
        if (existing != null) {
            throw new AlreadyExistsException(
                    String.format("There already exists a publisher with id = %s", publisher.getId()));
        }
        return repository.save(publisher);
    }

    @Override
    public Publisher findById(int id) {
        LOGGER.debug("Search publisher by id: " + id);
        return repository.findOne(id);
    }

    @Override
    public List<Publisher> findAll() {
        LOGGER.debug("Retrieve the list of all publishers!");
        return repository.findAll();
    }

    @Override
    public Publisher update(Publisher publisher) {
        LOGGER.debug("Publisher with id: " + publisher.getId() + " updated! ");
        if (!entityManager.contains(publisher))
            publisher = entityManager.merge(publisher);
        return publisher;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("Publisher by id: " + id + " Deleted!");
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("The list all publishers deleted!");
        repository.deleteAll();
    }
}