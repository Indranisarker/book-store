package com.example.Online.Book.Store.service;

import com.example.Online.Book.Store.entity.User;
import com.example.Online.Book.Store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if(user == null){
            throw new UsernameNotFoundException("User not found 404");
        }
        else{
            return new UserPrinciple(user);
        }
    }
}
