package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloaderThread extends Thread {

    private final int THREAD_SERIAL_ID;
    private File tempPartFile;
    private ReadableByteChannel readableByteChannel;
    private FileChannel targetFileChannel;

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

        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Started.");

        long tempTotalBytesTransferred = 0;
        try {
            System.out.println("Downloading Data...");
            tempTotalBytesTransferred += targetFileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            targetFileChannel.close();
            readableByteChannel.close();
        } catch (IOException e) {
            System.out.println("IO Exception | DownloaderThread | [01] |");
            System.out.println("Current OBJ State : " + this.toString());
            e.printStackTrace();
        }

        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Completed." +
                "\nBytes Downloaded : " + tempTotalBytesTransferred);

    }

    public File getTempPartFile() {
        return tempPartFile;
    }
}
