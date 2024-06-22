package com.example.Online.Book.Store.controller;

import com.example.Online.Book.Store.dto.UserDTO;
import com.example.Online.Book.Store.entity.User;
import com.example.Online.Book.Store.service.CustomUserDetailsService;
import com.example.Online.Book.Store.service.LoginService;
import com.example.Online.Book.Store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String home(Model model){
        return "home";
    }
    //admin login
    @GetMapping("/user-login")
    public String showAdminLogin(User user, Model model){
        model.addAttribute("user", user);
//        if(success){
//            model.addAttribute("message", "Registered Successfully!");
//        }

        return "userLogin";
    }

    @GetMapping("/register")
    public String showRegisterForm(User user, Model model){
        model.addAttribute("user", user);
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", null);
        }
        return "register";
    }

    public static boolean success;
    @PostMapping("/register-user")
    public String registerUser(@ModelAttribute("user")UserDTO userDTO, Model model){
        model.addAttribute("user", userDTO);
        success = loginService.createUser(userDTO);
        model.addAttribute("message", "Registration Successfully Complete!");
        return "redirect:/user-login";
    }
}
