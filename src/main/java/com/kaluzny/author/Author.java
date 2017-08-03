package com.kaluzny.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaluzny.address.Address;
import com.kaluzny.book.Book;
import com.kaluzny.domain.BaseEntity;
import com.kaluzny.publisher.Publisher;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "AUTHORS")
@AttributeOverride(name = "id", column = @Column(name = "author_id"))
public class Author extends BaseEntity {

    private int bookCount;
    private String name;
    private List<Book> books; //Books that author has written
    private Address address;
    private Set<Publisher> publisher;

    // With @formula don't create field in db...when add @attribute
    @Formula("(SELECT count(b.book_id) FROM BOOKS b WHERE b.author_fk = author_id)")
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "author")
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

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "Author_publisher",
            joinColumns = @JoinColumn(name = "author_fk"), inverseJoinColumns = @JoinColumn(name = "publisher_fk"))
    public Set<Publisher> getPublisher() {
        return publisher;
    }

    public void setPublisher(Set<Publisher> publisher) {
        this.publisher = publisher;
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
