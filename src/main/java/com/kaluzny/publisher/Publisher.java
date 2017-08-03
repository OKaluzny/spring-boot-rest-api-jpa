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
@AttributeOverride(name = "id", column = @Column(name = "publisher_id"))
public class Publisher extends BaseEntity {

    private int bookCount;
    private String name;
    private List<Book> books;
    private Set<Author> author;

    @Formula("(SELECT count(b.book_id) FROM BOOKS b WHERE b.publisher_fk = publisher_id)")
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "publisher")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher)) return false;

        Publisher publisher = (Publisher) o;

        if (bookCount != publisher.bookCount) return false;
        if (!name.equals(publisher.name)) return false;
        if (!books.equals(publisher.books)) return false;
        return author.equals(publisher.author);

    }

    @Override
    public int hashCode() {
        int result = bookCount;
        result = 31 * result + name.hashCode();
        result = 31 * result + books.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "bookCount=" + bookCount +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", author=" + author +
                '}';
    }
}
