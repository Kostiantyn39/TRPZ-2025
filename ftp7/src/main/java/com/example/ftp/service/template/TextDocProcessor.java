package com.example.ftp.service.template;

import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class TextDocProcessor extends FileProcessingTemplate {

    @Override
    protected boolean isValidType(File file) {
        return file.getName().endsWith(".txt") || file.getName().endsWith(".log");
    }

    @Override
    protected void extractMetadata(File file) {
        System.out.println(">> Text Processor: Counting lines and words in " + file.getName());
        System.out.println("   -> Result: 150 lines found (Simulated).");
    }

    @Override
    protected boolean shouldCreateBackup() {
        return true;
    }
}