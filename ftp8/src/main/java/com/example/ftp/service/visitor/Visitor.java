package com.example.ftp.service.visitor;

// Visitor Interface
public interface Visitor {
    void visitFile(MyFile file);

    void visitDirectory(MyDirectory directory);
}
