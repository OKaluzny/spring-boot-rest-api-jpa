package com.kaluzny.book;

import java.util.List;

public interface BookService {

    boolean isExist(Book book);

    Book save(Book book);

    Book findById(int id);

    List<Book> findAll();

    Book update(Book book);

    void delete(int id);

    void deleteAll();
}