package com.example.ftp.controller;

import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.ConnectionLogRepository;
import com.example.ftp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionLogRepository logRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newUser", new FtpUser());
        return "users";
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        // Показуємо останні 50 записів
        model.addAttribute("logs", logRepository.findAll());
        return "stats";
    }

    @PostMapping("/addUser")
    public String addUser(FtpUser user) {
        // Створюємо фізичну папку
        File home = new File(user.getHomeDirectory());
        if (!home.exists()) {
            home.mkdirs();
        }
        userRepository.save(user);
        return "redirect:/";
    }
}