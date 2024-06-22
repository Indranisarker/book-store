package com.example.Online.Book.Store.service;


import com.example.Online.Book.Store.dto.BookDTO;
import com.example.Online.Book.Store.dto.ServiceReviewDTO;
import com.example.Online.Book.Store.entity.Book;
import com.example.Online.Book.Store.entity.OrderDetails;
import com.example.Online.Book.Store.entity.ServiceReview;
import com.example.Online.Book.Store.entity.User;
import com.example.Online.Book.Store.repository.BooksRepository;
import com.example.Online.Book.Store.repository.OrderDetailsRepository;
import com.example.Online.Book.Store.repository.ServiceReviewRepository;
import com.example.Online.Book.Store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private ServiceReviewRepository serviceReviewRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    public Page<Book> getBooks(int pageNo, int pageSize, String sortBy, String sortDirection) {
       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNo - 1,pageSize,sort);
        return booksRepository.findAll(paging);

    }

    @Transactional
    public void createBook(BookDTO bookDTO) {
        try {
            // Save the file locally
            MultipartFile file = bookDTO.getImage();
            if (file == null || file.isEmpty()) {
                throw new IOException("File is empty or not provided.");
            }

            String uploadDir = "uploads/"; // Ensure the directory path has a trailing slash
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new IOException("File name is not provided.");
            }

            Path filePath = uploadPath.resolve(fileName);
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }
            // Convert file to byte array
            byte[] imageBytes = file.getBytes();

            // Create and save the book entity
            Book book = new Book();
            book.setName(bookDTO.getName());
            book.setAuthor(bookDTO.getAuthor());
            book.setCategory(bookDTO.getCategory());
            book.setImage(imageBytes);
            book.setImagePath(filePath.toString());
            book.setPrice(bookDTO.getPrice());
            book.setQuantity(bookDTO.getQuantity());
            book.setPages(bookDTO.getPages());
            book.setEdition(bookDTO.getEdition());
            book.setPublication(bookDTO.getPublication());
            book.setISBN(bookDTO.getISBN());

            Book savedBook = booksRepository.save(book);
            BookDTO.bookEntityToDTO(savedBook);
        } catch (IOException e) {
            e.printStackTrace();
            RedirectAttributes redirectAttributes = null;
            redirectAttributes.addFlashAttribute("successMessage","Books not Added!");
        }
    }

    public Book updateBook(Long id) {
        Optional<Book> book = booksRepository.findById(id);
        Book newBook = null;
        if(book.isPresent()){
            newBook = book.get();
            booksRepository.save(newBook);
        }
        return newBook;
    }

    public BookDTO updateBookById(Long id, BookDTO bookDTO) {
        Optional<Book> existingBook = booksRepository.findById(id);
        Book presentBook = null;
        try {
            if (existingBook == null) {
                throw new RuntimeException("Book not found.");
            }
            MultipartFile file = bookDTO.getImage();
            if (file != null && !file.isEmpty()) {
                // Define the upload directory relative to the current working directory
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);

                // Ensure the upload directory exists
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                if (fileName == null || fileName.isEmpty()) {
                    throw new IOException("File name is not provided.");
                }

                Path filePath = uploadPath.resolve(fileName);

                // Copy the file to the target location
                try (var inputStream = file.getInputStream()) {
                    Files.copy(inputStream, filePath);
                }

                // Convert file to byte array
                byte[] imageBytes = file.getBytes();

                if (existingBook.isPresent()) {
                    presentBook = existingBook.get();
                    presentBook.setId(id);
                    presentBook.setName(bookDTO.getName());
                    presentBook.setAuthor(bookDTO.getAuthor());
                    presentBook.setCategory(bookDTO.getCategory());
                    presentBook.setImage(imageBytes);
                    presentBook.setImagePath(filePath.toString());
                    presentBook.setPrice(bookDTO.getPrice());
                    presentBook.setPages(bookDTO.getPages());
                    presentBook.setEdition(bookDTO.getEdition());
                    presentBook.setPublication(bookDTO.getPublication());
                    presentBook.setISBN(bookDTO.getISBN());
                    booksRepository.save(presentBook);
                }

            }
            return BookDTO.bookEntityToDTO(presentBook);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public void deleteBookById(Long id) {
        booksRepository.deleteById(id);
    }


    public List<ServiceReview> getAllReviews() {
        return serviceReviewRepository.findAll();
    }

    public List<OrderDetails> getAllOrders() {
        return orderDetailsRepository.findAll();
    }
}
