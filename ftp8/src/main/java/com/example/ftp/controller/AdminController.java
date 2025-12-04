package com.example.ftp.controller;

import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.UserRepository;
import com.example.ftp.service.memento.ConfigHistoryCaretaker;
import com.example.ftp.service.memento.FtpUserMemento;
import com.example.ftp.service.memento.UserOriginator;
// Імпорти для Visitor
import com.example.ftp.service.visitor.MyDirectory;
import com.example.ftp.service.visitor.SizeCalculatorVisitor;
import com.example.ftp.service.visitor.StructurePrintVisitor;

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
    @Autowired
    private UserOriginator originator;
    @Autowired
    private ConfigHistoryCaretaker caretaker;

    // Inject Visitors (Lab 8)
    @Autowired
    private SizeCalculatorVisitor sizeVisitor;
    @Autowired
    private StructurePrintVisitor structureVisitor;

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

    // --- Lab 6: Memento (Edit/Undo) ---
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        FtpUser user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("canUndo", caretaker.hasHistory(id));
        return "edit_user";
    }

    @PostMapping("/updateUser")
    public String updateUser(FtpUser user) {
        FtpUser oldUserVersion = userRepository.findById(user.getId()).orElseThrow();
        FtpUserMemento snapshot = originator.save(oldUserVersion);
        caretaker.saveSnapshot(user.getId(), snapshot);

        oldUserVersion.setUsername(user.getUsername());
        oldUserVersion.setHomeDirectory(user.getHomeDirectory());
        oldUserVersion.setCanWrite(user.isCanWrite());
        oldUserVersion.setMaxUploadRate(user.getMaxUploadRate());

        userRepository.save(oldUserVersion);
        return "redirect:/edit/" + user.getId();
    }

    @GetMapping("/undo/{id}")
    public String undoUserChanges(@PathVariable Long id) {
        FtpUser user = userRepository.findById(id).orElseThrow();
        FtpUserMemento memento = caretaker.undo(id);
        if (memento != null) {
            originator.restore(user, memento);
            userRepository.save(user);
        }
        return "redirect:/edit/" + id;
    }

    // --- Lab 8: Visitor (Analyze) ---
    @GetMapping("/analyze/{id}")
    public String analyzeUserFiles(@PathVariable Long id, Model model) {
        FtpUser user = userRepository.findById(id).orElseThrow();

        File homeDir = new File(user.getHomeDirectory());
        if (!homeDir.exists()) {
            model.addAttribute("report", "Directory does not exist.");
            model.addAttribute("structure", "");
            model.addAttribute("user", user);
            return "analysis";
        }

        // 1. Створюємо структуру елементів
        MyDirectory rootNode = new MyDirectory(homeDir);

        // 2. Запускаємо SizeVisitor
        sizeVisitor.reset();
        rootNode.accept(sizeVisitor);
        String sizeReport = sizeVisitor.getReport();

        // 3. Запускаємо StructureVisitor
        structureVisitor.reset();
        rootNode.accept(structureVisitor);
        String structureReport = structureVisitor.getOutput();

        model.addAttribute("user", user);
        model.addAttribute("report", sizeReport);
        model.addAttribute("structure", structureReport);

        return "analysis";
    }
}