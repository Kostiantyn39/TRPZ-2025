package com.example.ftp.service.visitor;

public interface FileSystemElement {
    void accept(Visitor visitor);
}
