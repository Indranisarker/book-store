package com.example.Online.Book.Store.dto;


import com.example.Online.Book.Store.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String name;
    private String author;
    private String category;
    private MultipartFile image;
    private String imageBase64;
    private double price;
    private int quantity;
    private int pages;
    private int edition;
    private String publication;
    private int ISBN;

    public static BookDTO bookEntityToDTO(Book book){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setImageBase64(Base64.getEncoder().encodeToString(book.getImage()));
        bookDTO.setPrice(book.getPrice());
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setPages(book.getPages());
        bookDTO.setEdition(book.getEdition());
        bookDTO.setPublication(book.getPublication());
        bookDTO.setISBN(book.getISBN());
        return bookDTO;
    }
}
