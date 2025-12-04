package com.example.ftp.service.memento;

public class FtpUserMemento {
    private final String homeDirectory;
    private final boolean canWrite;
    private final boolean canRead;
    private final int maxUploadRate;
    private final int maxDownloadRate;

    public FtpUserMemento(String homeDirectory, boolean canWrite, boolean canRead, int maxUploadRate,
            int maxDownloadRate) {
        this.homeDirectory = homeDirectory;
        this.canWrite = canWrite;
        this.canRead = canRead;
        this.maxUploadRate = maxUploadRate;
        this.maxDownloadRate = maxDownloadRate;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public int getMaxUploadRate() {
        return maxUploadRate;
    }

    public int getMaxDownloadRate() {
        return maxDownloadRate;
    }
}