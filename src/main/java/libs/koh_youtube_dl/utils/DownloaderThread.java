package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

public class DownloaderThread extends Thread {

    private final static int MAX_RETRY_ATTEMPTS = 5;
    private final int THREAD_SERIAL_ID;
    private File tempPartFile;
    private ReadableByteChannel readableByteChannel;
    private FileChannel targetFileChannel;
    private int retryAttempts;

/*
    DownloaderThread(File tempPartFile) {
        this.tempPartFile = tempPartFile;
    }*/

    DownloaderThread(File tempPartFile, ReadableByteChannel rbc, FileChannel fileChannel, int THREAD_SERIAL_ID) {
        this.tempPartFile = tempPartFile;
        this.readableByteChannel = rbc;
        this.targetFileChannel = fileChannel;
        this.THREAD_SERIAL_ID = THREAD_SERIAL_ID;
    }

    public void run() {

        /*
            Retrying Failed Downloads upto 5 times to handle following Exception

            javax.net.ssl.SSLException:
            Connection reset at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:127)

         */
        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Started.");

        long tempTotalBytesTransferred = 0;
        try {
            System.out.println("Downloading Data...");
            tempTotalBytesTransferred += targetFileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {

            System.out.println("IO Exception | DownloaderThread | [01] |");
            System.out.println("Current OBJ State : " + this.toString());
            e.printStackTrace();
            handleDownloadFailure();

        } finally {
            try {
                targetFileChannel.close();
                readableByteChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Completed." +
                "\nBytes Downloaded : " + tempTotalBytesTransferred);

    }

    private void handleDownloadFailure() {

        if (++retryAttempts > DownloaderThread.MAX_RETRY_ATTEMPTS) return;
        System.out.println("Retrying Attempt : " + retryAttempts);

        try {
            Files.deleteIfExists(this.tempPartFile.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.start();
    }

    public File getTempPartFile() {
        return tempPartFile;
    }
}
