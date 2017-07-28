package com.kaluzny.book;

import com.kaluzny.domain.BaseEntity;
import com.kaluzny.author.Author;
import com.kaluzny.publisher.Publisher;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book extends BaseEntity {

    private String name;
    private Author author;
    private Publisher publisher;
    private int year; //Publishing year
    private int pages; //Total number of pages

    @Column(name = "BOOK_NAME", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_fk") //, nullable = false
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_fk")
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    @Column(name = "publishing_year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(columnDefinition = "bigint")
    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author=" + author +
                ", publisher=" + publisher +
                ", year=" + year +
                ", pages=" + pages +
                '}';
    }
}
