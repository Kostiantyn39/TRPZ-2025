package com.example.ftp.service.visitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyDirectory implements FileSystemElement {
    private String name;
    private List<FileSystemElement> children = new ArrayList<>();

    public MyDirectory(File realDirectory) {
        this.name = realDirectory.getName();
        // Рекурсивно будуємо дерево
        File[] files = realDirectory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    children.add(new MyDirectory(f));
                } else {
                    children.add(new MyFile(f));
                }
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDirectory(this);
        // Проходимо по всіх дочірніх елементах
        for (FileSystemElement child : children) {
            child.accept(visitor);
        }
    }

    public String getName() {
        return name;
    }

    public List<FileSystemElement> getChildren() {
        return children;
    }
}