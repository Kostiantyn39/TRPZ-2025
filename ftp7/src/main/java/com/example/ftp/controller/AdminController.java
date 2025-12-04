package com.example.ftp.controller;

import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.UserRepository;
import com.example.ftp.service.memento.ConfigHistoryCaretaker;
import com.example.ftp.service.memento.FtpUserMemento;
import com.example.ftp.service.memento.UserOriginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Memento components
    @Autowired
    private UserOriginator originator;
    @Autowired
    private ConfigHistoryCaretaker caretaker;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newUser", new FtpUser());
        return "users";
    }

    @PostMapping("/addUser")
    public String addUser(FtpUser user) {
        File home = new File(user.getHomeDirectory());
        if (!home.exists())
            home.mkdirs();
        userRepository.save(user);
        return "redirect:/";
    }

    // --- Нові методи для редагування та Memento ---

    // 1. Показати форму редагування
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        FtpUser user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        // Передаємо прапорець, чи є історія для цього юзера (чи можна натиснути Undo)
        model.addAttribute("canUndo", caretaker.hasHistory(id));
        return "edit_user";
    }

    // 2. Зберегти зміни (з попереднім збереженням знімка)
    @PostMapping("/updateUser")
    public String updateUser(FtpUser user) {
        // Отримуємо стару версію з БД перед оновленням
        FtpUser oldUserVersion = userRepository.findById(user.getId()).orElseThrow();

        // Зберігаємо ЗНІМОК старого стану (Memento Pattern)
        FtpUserMemento snapshot = originator.save(oldUserVersion);
        caretaker.saveSnapshot(user.getId(), snapshot);

        // Оновлюємо поля (пароль не чіпаємо для простоти, якщо він пустий)
        oldUserVersion.setUsername(user.getUsername());
        oldUserVersion.setHomeDirectory(user.getHomeDirectory());
        oldUserVersion.setCanWrite(user.isCanWrite());
        oldUserVersion.setMaxUploadRate(user.getMaxUploadRate());

        userRepository.save(oldUserVersion);
        return "redirect:/edit/" + user.getId();
    }

    // 3. Відкат змін (Undo)
    @GetMapping("/undo/{id}")
    public String undoUserChanges(@PathVariable Long id) {
        FtpUser user = userRepository.findById(id).orElseThrow();

        // Дістаємо останній знімок
        FtpUserMemento memento = caretaker.undo(id);

        if (memento != null) {
            // Відновлюємо стан (Memento Pattern)
            originator.restore(user, memento);
            userRepository.save(user);
        }

        return "redirect:/edit/" + id;
    }
}