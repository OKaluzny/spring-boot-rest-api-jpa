package com.kaluzny.publisher;

import java.util.List;

public interface PublisherService {

    boolean isExist(Publisher publisher);

    Publisher save(Publisher publisher);

    Publisher findById(int id);

    List<Publisher> findAll();

    Publisher update(Publisher publisher);

    void delete(int id);

    void deleteAll();
}