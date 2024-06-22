package com.example.Online.Book.Store.controller;

import com.example.Online.Book.Store.dto.BookDTO;
import com.example.Online.Book.Store.dto.BookReviewDTO;
import com.example.Online.Book.Store.dto.ServiceReviewDTO;
import com.example.Online.Book.Store.dto.ShippingInfoDTO;
import com.example.Online.Book.Store.entity.Book;
import com.example.Online.Book.Store.entity.CartItem;
import com.example.Online.Book.Store.entity.User;
import com.example.Online.Book.Store.repository.BooksRepository;
import com.example.Online.Book.Store.repository.UserRepository;
import com.example.Online.Book.Store.service.CustomUserDetailsService;
import com.example.Online.Book.Store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BooksRepository booksRepository;

    @GetMapping("/view-books")
    public String getAllBook(Model model, Principal principal) {
        String userEmail = principal.getName(); // Get the email from Principal

        // Load user details using email (assuming email is used as username)
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        model.addAttribute("user", userDetails);

        // Assuming getBooks method is correctly implemented
        return getBooks(1, model);
    }
    private int calculateStartSerialForPage(int pageNo) {
        int pageSize = 3;
        return (pageNo - 1) * pageSize;
    }


    @GetMapping("/view-books/page/{pageNo}")
    public String getBooks(@PathVariable("pageNo") int pageNo, Model model){
        int pageSize = 3;
        int startSerial = calculateStartSerialForPage(pageNo);
        model.addAttribute("startSerial", startSerial);
        Page<Book> bookPage = userService.getBook(pageNo,pageSize);
        List<Book> books = bookPage.getContent();
        List<BookDTO> bookModels = books.stream().map(BookDTO::bookEntityToDTO).toList();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", bookPage.getTotalPages());
        model.addAttribute("books",bookModels);

        return "user/userHome";
    }
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        BookDTO book = null;
        if (keyword != null && !keyword.isEmpty()) {
            book = userService.searchBooks(keyword);
        }
        model.addAttribute("book", book);
        return "user/books-details";
    }
    @GetMapping("/book/details/{id}")
    public String bookDetails(@PathVariable("id") Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        model.addAttribute("user", user);
        model.addAttribute("book",userService.getBookDetails(id));
        model.addAttribute("ratingCount", userService.getTotalRatingsCount(id));
        model.addAttribute("reviewCount", userService.getTotalReviewCount(id));
        return "user/books-details";
    }
    @GetMapping("/checkout")
    public String checkOut(@RequestParam("quantity") int quantity, @RequestParam("amount") Float amount, Model model){
        System.out.println("Quantity: " + quantity);
        System.out.println("Amount: " + amount);

        // Ensure amount is not null
        if (amount == null) {
            amount = 0.0f;
        }
        model.addAttribute("info", new ShippingInfoDTO());
        model.addAttribute("quantity", quantity);
        model.addAttribute("amount", amount);
        return "user/checkout";
    }
    @PostMapping("/add-checkout-details")
    public String createCheckoutDetails(@ModelAttribute("info") ShippingInfoDTO shippingInfoDTO, @RequestParam("quantity") int quantity, @RequestParam("amount") Float amount, Model model) {
        userService.saveCheckoutDetails(shippingInfoDTO);
        // Pass the quantity and amount to the next view
        model.addAttribute("quantity", quantity);
        model.addAttribute("amount", amount);
        return "user/checkout-details";
    }

    @GetMapping("/checkout-details")
    public String checkoutDetails(@RequestParam("quantity") int quantity, @RequestParam("amount") Float amount, Model model){
        // Ensure amount is not null
        if (amount == null) {
            amount = 0.0f;
        }
        model.addAttribute("quantity", quantity);
        model.addAttribute("amount", amount);
        return "user/checkout-details";
    }
    @GetMapping("/payment")
    public String showPaymentPage(Model model) {
        return "user/payment";
    }
    @PostMapping("/processPayment")
    public String processPayment(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email).get();
        userService.processCheckout();
        userService.deleteCartItemsByUser(user);
        return "redirect:/user/order-success";
}
    @GetMapping("/order-success")
    public String showSuccessOrder(Model model){
        List<CartItem> cartItems = userService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        return "user/order-success";
    }
@GetMapping("/review")
public String showReviewForm(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).get();
    System.out.println("User ID (Security): " + user.getUser_id());
    ServiceReviewDTO serviceReviewDTO = new ServiceReviewDTO();
    serviceReviewDTO.setUser(user);
    model.addAttribute("review", serviceReviewDTO);
    model.addAttribute("user", user);
    return "user/add-review";
}
    @PostMapping("/add-review/{userId}")
    public String addReview(@PathVariable("userId") Long userId, @ModelAttribute("review") ServiceReviewDTO serviceReviewDTO, Model model){
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            // Handle the case where the user is not found
            System.out.printf("user not found");
            throw new RuntimeException("User not found");

        }
        User user = userOptional.get();
        serviceReviewDTO.setUser(user);
        System.out.println("Selected Rating: " + serviceReviewDTO.getRatings()); // Debug statement to verify rating value
        userService.createReviews(userId, serviceReviewDTO);
        model.addAttribute("user", user);
        model.addAttribute("review", serviceReviewDTO);
        return "user/userHome";
    }
    @GetMapping("/book-review/{id}")
    public String showBookReviewForm(@PathVariable("id") Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        System.out.println("User ID (Security): " + user.getUser_id());
        BookReviewDTO bookReviewDTO = new BookReviewDTO();
        bookReviewDTO.setUser(user);
        model.addAttribute("book", userService.getBookDetails(id));
        model.addAttribute("bookReview", bookReviewDTO);
        model.addAttribute("user", user);
        return "user/book-review";
    }

    @PostMapping("/add-book-review/{userId}/{bookId}")
    public String createBookReview(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId, @ModelAttribute("bookReview")BookReviewDTO bookReviewDTO, Model model){
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");

        }
        User user = userOptional.get();
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Book not found");

        }
        Book book = bookOptional.get();
        bookReviewDTO.setUser(user);
        bookReviewDTO.setBook(book);
        userService.createBookReview(userId,bookId, bookReviewDTO);
        model.addAttribute("user", user);
        model.addAttribute("book", book);
        model.addAttribute("bookReview", bookReviewDTO);
        return "redirect:/user/view-books";
    }

    @GetMapping("/review-details/{bookId}")
    public String viewReviews(@PathVariable("bookId") Long bookId, Model model){
        model.addAttribute("bookReviews", userService.getBookReviews(bookId));
        return "user/book-review-details";
    }
    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email).get();
        List<CartItem> cartItems = userService.getCartItemsForUser(user);
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (CartItem i : cartItems) {
            bookDTOs.add(BookDTO.bookEntityToDTO(i.getBook()));
        }
        model.addAttribute("books", bookDTOs);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("quantity", 1);
        return "user/cart";
    }
    @PostMapping("/cart/add/{userId}")
    public String addToCart(@PathVariable("userId") Long userId, @RequestParam Long bookId,
                            @ModelAttribute("cartItems") CartItem cartItem, Model model, Principal principal) {
        String email = principal.getName();
        User user1 = userRepository.findByEmail(email).get();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        model.addAttribute("user", user);
        cartItem.setUser(user1);
        userService.addToCart(bookId, userId);
        return "redirect:/user/view-modal/" + bookId;
    }
    @PostMapping("/cart/update/{bookId}")
    public String updateCart(@PathVariable Long bookId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
         userService.updateCartItem(bookId, quantity);
        redirectAttributes.addFlashAttribute("message", "Quantity updated successfully!");

        return "redirect:/user/cart";
    }
    @GetMapping("/view-modal/{id}")
    public String viewModal(@PathVariable("id") Long id, Model model){
        model.addAttribute("book", userService.getBookDetails(id));
        return "user/cartModalView";
    }

    @GetMapping("/delete-cart-item/{id}")
    public String deleteCartItem(@PathVariable("id") Long cartId){
        userService.deleteCartItem(cartId);
        return "redirect:/user/cart";
    }
}
