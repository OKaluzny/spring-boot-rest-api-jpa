package com.kaluzny.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaluzny.address.Address;
import com.kaluzny.book.Book;
import com.kaluzny.domain.BaseEntity;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AUTHORS")
public class Author extends BaseEntity {

    private int bookCount;
    private String name;
    private List<Book> books; //Books that author has written
    private Address address;

    @Formula("(" +
            "SELECT count(books.id) " +
            "FROM BOOKS books " +
            "WHERE books.author_fk = id)")
    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    @Column(name = "AUTHOR_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "author")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    @OneToOne
    @JoinColumn(name = "address_fk")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Author{" +
                "bookCount=" + bookCount +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
