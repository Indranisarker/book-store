package com.example.Online.Book.Store.repository;

import com.example.Online.Book.Store.entity.CartItem;
import com.example.Online.Book.Store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
//    List<CartItem> findByUser_User_id(Long userId);
     CartItem findByBookId(Long bookId);

     List<CartItem> findByUser(User user);

     void deleteCartItemsByUser(User user);

}
