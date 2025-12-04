package com.example.ftp.service.visitor;

import java.io.File;

public class MyFile implements FileSystemElement {
    private String name;
    private long size;

    public MyFile(File realFile) {
        this.name = realFile.getName();
        this.size = realFile.length();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitFile(this);
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}