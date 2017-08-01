package com.kaluzny.publisher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaluzny.author.Author;
import com.kaluzny.book.Book;
import com.kaluzny.domain.BaseEntity;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PUBLISHERS")
public class Publisher extends BaseEntity {

    private int bookCount;
    private String name;
    private List<Book> books;
    private Set<Author> author;

    @Formula("(" +
            "SELECT count(books.id) " +
            "FROM BOOKS books " +
            "WHERE books.publisher_fk = id)")
    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    @Column(name = "PUBLISHER_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "publisher")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "publisher")
    public Set<Author> getAuthor() {
        return author;
    }

    public void setAuthor(Set<Author> author) {
        this.author = author;
    }
}
