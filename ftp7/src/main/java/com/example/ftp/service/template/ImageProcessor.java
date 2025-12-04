package com.example.ftp.service.template;

import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class ImageProcessor extends FileProcessingTemplate {

    @Override
    protected boolean isValidType(File file) {
        return file.getName().endsWith(".jpg") || file.getName().endsWith(".png");
    }

    @Override
    protected void extractMetadata(File file) {
        System.out.println(">> Image Processor: Analyzing dimensions of " + file.getName());
        System.out.println("   -> Result: Resolution 1920x1080.");
    }
}