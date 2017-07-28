package com.kaluzny.author;

import java.util.List;

public interface AuthorService {

    boolean isExist(Author author);

    Author save(Author author);

    Author findById(int id);

    List<Author> findAll();

    Author update(Author author);

    void delete(int id);

    void deleteAll();
}