package com.kaluzny.address;

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
public class AddressServiceBean implements AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceBean.class);

    private AddressRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public AddressServiceBean(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isExist(Address address) {
        return findById(address.getId()) != null;
    }

    @Override
    public Address save(Address address) {
        LOGGER.debug("Save {}", address);
        Address existing = repository.findOne(address.getId());
        if (existing != null) {
            throw new AlreadyExistsException(
                    String.format("There already exists a address with id = %s", address.getId()));
        }
        return repository.save(address);
    }

    @Override
    public Address findById(int id) {
        LOGGER.debug("Search address by id: " + id);
        return repository.findOne(id);
    }

    @Override
    public List<Address> findAll() {
        LOGGER.debug("Retrieve the list of all address!");
        return repository.findAll();
    }

    @Override
    public Address update(Address address) {
        LOGGER.debug("Address with id: " + address.getId() + " updated! ");
        if (!entityManager.contains(address))
            address = entityManager.merge(address);
        return address;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("Address by id: " + id + " Deleted!");
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("The list all address deleted!");
        repository.deleteAll();
    }
}