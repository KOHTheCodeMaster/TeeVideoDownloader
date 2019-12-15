package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    private static final int THREAD_COUNT = 100;
//    private static long sharedCurrentFilePointer;
private static long startingIndexForEachThread;
    private final String TEMPORARY_PARTS_DIR_PATH;
    private int indexOfFileParts;
    private List<File> filePartsList;
    private List<DownloaderThread> downloaderThreadList;

    public DownloadManager(String tempDirPath) {
        downloaderThreadList = new ArrayList<>();
        filePartsList = new ArrayList<>();
        TEMPORARY_PARTS_DIR_PATH = tempDirPath + "/Temp-Parts";

        if (!new File(TEMPORARY_PARTS_DIR_PATH).mkdirs()) {
            System.out.println("Failed to Create Temp-Parts Dir. to store temporary files");
        }

    }

    public void downloadOffUrl(String resourceUrl, File targetFile) {

        long i1 = System.nanoTime();
        boolean supportsMultiPartsDownload;

        System.out.println("URL : " + resourceUrl);

        HttpURLConnection urlConnection1 = MyConnectionUtil.establishHttpUrlConnection(resourceUrl);

        if (urlConnection1 == null) {
            System.out.println("Unable to connect to Url : " + resourceUrl +
                    "\nSkipping file : " + targetFile);
            return;
        }

/*
        Map<String, List<String>> headerFields1 = urlConnection1.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entries = headerFields1.entrySet();
        entries.forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
*/

        final long fileLength = Long.parseLong(urlConnection1.getHeaderFields().get("Content-Length").get(0));
//        System.out.println("Original File Length : " + fileLength);

        final long dxFilePartLength = fileLength / THREAD_COUNT;   //  Downloaded by Each Thread
        final long remainingDataToDownload = fileLength % THREAD_COUNT; //  Download At End

        Runnable downloaderRunnable = () -> {

            long j = startingIndexForEachThread;

            String fileName = "A-" + ++indexOfFileParts + ".part";
            File tempPartFile = new File(TEMPORARY_PARTS_DIR_PATH, fileName);

            MyConnectionUtil myConnectionUtil = new MyConnectionUtil(resourceUrl, j, dxFilePartLength);

            //  Initialize downloaderThread | indexOfFileParts --> Thread Serial ID
            DownloaderThread downloaderThread = new DownloaderThread(tempPartFile, indexOfFileParts, myConnectionUtil);
            downloaderThreadList.add(downloaderThread);

        };

        supportsMultiPartsDownload = checkMultiPartDownloadSupport(resourceUrl, targetFile);

        try {

            //  Begin Downloading for Each DownloaderThread
            System.out.println("Starting to Download : " + targetFile.getName());

            if (supportsMultiPartsDownload) {

                for (int i = 0; i < THREAD_COUNT; i++) {

//                    System.out.println("Thread # : " + i);

                    Thread thread = new Thread(downloaderRunnable);
                    thread.start();
                    thread.join();
                    startingIndexForEachThread += dxFilePartLength;

                }

                if (remainingDataToDownload != 0) {
//                    System.out.println("Creating New Thread to download the Remaining Data");
                    Thread thread = new Thread(downloaderRunnable);
                    thread.start();
                    thread.join();
                }

            } else {
                downloadWithSingleThread(resourceUrl, fileLength);
            }

            //  Initiate Downloading each thread
            for (DownloaderThread downloaderThread : downloaderThreadList) {
                downloaderThread.start();
//                downloaderThread.join();
            }

            //  Wait for each thread to join and then add each tempPartFile to filePartsList
            for (DownloaderThread downloaderThread : downloaderThreadList) {
                downloaderThread.join();
                filePartsList.add(downloaderThread.getTempPartFile());
            }

            System.out.println("Download Completed! | FL : " + fileLength +
                    "\nMerging Downloaded Files now...");
            combineFileParts(filePartsList, targetFile, fileLength);

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("InterruptedException ERROR [01] : thread.join method failed...");
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);

        reset();

    }

    private void downloadWithSingleThread(String resourceUrl, long fileLength) {

        System.out.println("Inside downloadWithSingleThread\nFileLength : " + fileLength);

        MyConnectionUtil myConnectionUtil = new MyConnectionUtil(resourceUrl, 0, Long.MAX_VALUE);

        String fileName = "A-" + ++indexOfFileParts + ".part";
        File tempPartFile = new File(TEMPORARY_PARTS_DIR_PATH, fileName);

        //  Initialize downloaderThread
        //  indexOfFileParts --> Thread Serial ID
        DownloaderThread downloaderThread = new DownloaderThread(tempPartFile, indexOfFileParts, myConnectionUtil);
        downloaderThreadList.add(downloaderThread);

    }

    private boolean checkMultiPartDownloadSupport(String resourceUrl, File targetFile) {

        int responseCode = -1;
        HttpURLConnection urlConnection;

        try {
            urlConnection = MyConnectionUtil.establishHttpUrlConnection(resourceUrl);

            if (urlConnection == null) {
                System.out.println("Unable to connect to Url : " + resourceUrl +
                        "\nSkipping file : " + targetFile);
                return false;
            }

            urlConnection.addRequestProperty("Range", "bytes=" + 0 + "-" + 1);
            responseCode = urlConnection.getResponseCode(); //  Makes HTTP Request & gets the response code

        } catch (IOException e) {
            e.printStackTrace();
        }

        //        System.out.println("isMultiPartDownloadSupportAvailable : " + responseCode == 206);

        return responseCode == 206;

    }

    private void reset() {

//        sharedCurrentFilePointer = 0;
        startingIndexForEachThread = 0;
        indexOfFileParts = 0;

        try {

            for (File f : filePartsList) {
                boolean isDeleted = Files.deleteIfExists(f.toPath());
                if (!isDeleted)
                    System.out.println("ERROR | Exception : Unable to delete : " + f.getAbsolutePath());
            }

        } catch (IOException e) {
            System.out.println("Unable to Clean Downloaded File Parts...");
            e.printStackTrace();
        }
        System.out.println("File Parts Cleaned Successfully..!!");

        filePartsList = null;
        filePartsList = new ArrayList<>();
        downloaderThreadList = null;
        downloaderThreadList = new ArrayList<>();

    }

    private void combineFileParts(List<File> filePartsList, File destCombinedFile, long fileLength) {

        long i1 = System.nanoTime();
        long pos = 0;

        for (int i = 1; i <= filePartsList.size(); i++) {

            File f = filePartsList.get(i - 1);

            try (ReadableByteChannel rbc = Channels.newChannel(new FileInputStream(f));
                 FileChannel fc = new FileOutputStream(destCombinedFile, true).getChannel()) {

                fc.position(fileLength + 1);
                pos += fc.transferFrom(rbc, pos, Long.MAX_VALUE);

            } catch (IOException e) {
                System.out.println("IO Exception [03] - Failed to Combine File Parts...");
                e.printStackTrace();
            }

        }
        System.out.println("Completed Combining File Parts.");
        System.out.println("Combined File Size : " + destCombinedFile.length());

        System.out.println((System.nanoTime() - i1) / 1E9 + " seconds");

    }

}
