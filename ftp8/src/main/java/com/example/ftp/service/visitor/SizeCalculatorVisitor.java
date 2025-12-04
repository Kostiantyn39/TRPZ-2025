package com.example.ftp.service.visitor;

import org.springframework.stereotype.Component;

@Component
public class SizeCalculatorVisitor implements Visitor {
    private long totalSize = 0;
    private int fileCount = 0;

    // Скидаємо стан перед новим використанням
    public void reset() {
        totalSize = 0;
        fileCount = 0;
    }

    @Override
    public void visitFile(MyFile file) {
        totalSize += file.getSize();
        fileCount++;
    }

    @Override
    public void visitDirectory(MyDirectory directory) {
        // Для папки розмір не додаємо, просто враховуємо структуру
    }

    public String getReport() {
        return "Total Files: " + fileCount + ", Total Size: " + (totalSize / 1024) + " KB";
    }
}
