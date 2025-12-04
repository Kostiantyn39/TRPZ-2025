package com.example.ftp.service.template;

import java.io.File;

public abstract class FileProcessingTemplate {

    public final void processFile(File file, String uploaderName) {
        System.out.println("--- [TEMPLATE START] Processing " + file.getName() + " ---");

        if (!checkFileExists(file)) {
            System.out.println("File not found!");
            return;
        }

        if (isValidType(file)) {

            scanForViruses(file);

            extractMetadata(file);

            if (shouldCreateBackup()) {
                createBackup(file);
            }

            sendNotification(uploaderName, file.getName());
        } else {
            System.out.println("Skipping processing: Invalid file type for this processor.");
        }
        System.out.println("--- [TEMPLATE END] ---\n");
    }

    private boolean checkFileExists(File file) {
        return file.exists() && file.isFile();
    }

    private void scanForViruses(File file) {
        System.out.println(">> Security: Scanning " + file.getName() + " for viruses... OK.");
    }

    private void sendNotification(String user, String filename) {
        System.out.println(">> Notification: Informing admin that " + user + " uploaded " + filename);
    }

    private void createBackup(File file) {
        System.out.println(">> Backup: Creating backup copy of " + file.getName() + " to /backup folder.");
    }

    protected abstract boolean isValidType(File file);

    protected abstract void extractMetadata(File file);

    protected boolean shouldCreateBackup() {
        return false;
    }
}