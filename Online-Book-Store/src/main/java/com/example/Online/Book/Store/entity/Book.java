package com.example.Online.Book.Store.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "allBooks")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String category;
    @Lob
    private byte[] image;
    private String imagePath;
    private double price;
    private int quantity;
    private int pages;
    private int edition;
    private String publication;
    private int ISBN;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookReview> reviews;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookReview> rating;
}
