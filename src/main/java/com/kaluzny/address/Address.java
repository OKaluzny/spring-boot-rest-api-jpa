package com.kaluzny.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaluzny.author.Author;
import com.kaluzny.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESSES")
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
public class Address extends BaseEntity {

    private String country;
    private String city;
    private Author author;

    @Column(name = "AUTHOR_COUNTRY")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "AUTHOR_CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", author=" + author +
                '}';
    }
}
