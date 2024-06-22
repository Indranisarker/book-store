package com.example.Online.Book.Store.service;

import com.example.Online.Book.Store.dto.BookDTO;
import com.example.Online.Book.Store.dto.BookReviewDTO;
import com.example.Online.Book.Store.dto.ServiceReviewDTO;
import com.example.Online.Book.Store.dto.ShippingInfoDTO;
import com.example.Online.Book.Store.entity.*;
import com.example.Online.Book.Store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceReviewRepository serviceReviewRepository;

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    public Page<Book> getBook(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo - 1, pageSize);
        return booksRepository.findAll(paging);
    }

    public BookDTO getBookDetails(Long id) {
        Optional<Book> book = booksRepository.findById(id);
        Book b = book.get();
        return BookDTO.bookEntityToDTO(b);
    }

    public void createReviews(Long userId, ServiceReviewDTO serviceReviewDTO) {
        Optional<User> user = userRepository.findById(userId);
        ServiceReview serviceReview = new ServiceReview();
        serviceReview.setUser(user.get());
        serviceReview.setRatings(serviceReviewDTO.getRatings());
        serviceReview.setComments(serviceReviewDTO.getComments());
        serviceReview.setCreateTime(new Date());
        serviceReviewRepository.save(serviceReview);
        ServiceReviewDTO.reviewEntityToDTO(serviceReview);
    }

    @Transactional
    public BookDTO searchBooks(String keyword) {
        Book book = booksRepository.findBookByNameIgnoreCase(keyword);
        return book != null ? BookDTO.bookEntityToDTO(book) : null;
    }

    @Transactional
    public void processCheckout() {
        List<CartItem> cartItems = cartRepository.findAll();
        // Assume payment is processed here
        for (CartItem cartItem : cartItems) {
            Book book = booksRepository.findById(cartItem.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            if (book.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for book: " + book.getName());
            }
            book.setQuantity(book.getQuantity() - cartItem.getQuantity());
            booksRepository.save(book);
        }
    }

    public List<CartItem> getCartItems() {
        return cartRepository.findAll();
    }

    @Transactional
    public void addToCart(Long bookId, Long userId) {
        Optional<Book> getBook = booksRepository.findById(bookId);
        Optional<User> userOptional = userRepository.findById(userId);
        Book book = getBook.get();
        User user = userOptional.get();
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setUser(user);
        CartItem savedItem = cartRepository.save(cartItem);
        BookDTO.bookEntityToDTO(savedItem.getBook());

    }


    @Transactional
    public void updateCartItem(Long bookId, int quantity) {
        CartItem cartItem = cartRepository.findByBookId(bookId);

        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        } else {
            Optional<Book> book = booksRepository.findById(bookId);
            if (book.isPresent()) {
                Book book1 = book.get();
                cartItem = new CartItem();
                cartItem.setBook(book1);
                cartItem.setQuantity(quantity);
                cartRepository.save(cartItem);
            } else {
                throw new RuntimeException("Book not found");
            }
        }
    }

    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }


    public void createBookReview(Long userId, Long bookId, BookReviewDTO bookReviewDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = booksRepository.findById(bookId);
        BookReview bookReview = new BookReview();
        bookReview.setUser(user.get());
        bookReview.setBook(book.get());
        bookReview.setRating(bookReviewDTO.getRating());
        bookReview.setReview(bookReviewDTO.getReview());
        bookReview.setCreateDate(new Date());
        bookReviewRepository.save(bookReview);
        BookReviewDTO.bookReviewEntityToDTO(bookReview);
    }

    public int getTotalRatingsCount(Long bookId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            List<BookReview> ratings = book.getRating();
            int totalRatings = 0;
            for (BookReview rating : ratings) {
                totalRatings += rating.getRating();
            }
            return totalRatings;
        }
        return 0;
    }

    public int getTotalReviewCount(Long bookId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            List<BookReview> reviews = book.getReviews();
            return reviews.size();
        }
        return 0;
    }

    public List<BookReview> getBookReviews(Long bookId) {
        return bookReviewRepository.findByBookId(bookId);
    }

    public void saveCheckoutDetails(ShippingInfoDTO shippingInfoDTO) {
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setFull_name(shippingInfoDTO.getFull_name());
        shippingInfo.setPhone_no(shippingInfoDTO.getPhone_no());
        shippingInfo.setCity(shippingInfoDTO.getCity());
        shippingInfo.setAddress(shippingInfoDTO.getAddress());
        shippingInfoRepository.save(shippingInfo);
        ShippingInfoDTO.infoToDTO(shippingInfo);
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsForUser(User user ) {
        return cartRepository.findByUser(user);

    }

    @Transactional
    public void deleteCartItemsByUser(User user) {
        cartRepository.deleteCartItemsByUser(user);
    }
}
