package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadManager {

    private static final int THREAD_COUNT = 4;
    private static long sharedCurrentFilePointer;
    private static long startingIndexForEachThread;
    private final String TEMP_DIR_PATH;
    private int indexOfFileParts;
    private List<File> filePartsList;
    private List<DownloaderThread> downloaderThreadList;

    public DownloadManager(String tempDirPath) {
        downloaderThreadList = new ArrayList<>();
        filePartsList = new ArrayList<>();
        TEMP_DIR_PATH = tempDirPath;
    }

    public void downloadOffUrl2(String resourceUrl, File targetFile) {

        long i1 = System.nanoTime();
        boolean supportsMultiPartsDownload;

        System.out.println("URL : " + resourceUrl);

        try {

            final URL urlObj = new URL(resourceUrl);
            Map<String, List<String>> headerFields1 = urlObj.openConnection().getHeaderFields();
            Set<Map.Entry<String, List<String>>> entries = headerFields1.entrySet();
            entries.forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));


            final long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            System.out.println("Original File Length : " + fileLength);

            final long dxFilePartLength = fileLength / THREAD_COUNT;   //  Downloaded by Each Thread
            final long remainingDataToDownload = fileLength % THREAD_COUNT; //  Download At End

            System.out.println("dx : " + dxFilePartLength);
            Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
                while (sharedCurrentFilePointer < fileLength) {
                    System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                    try {
                        Thread.sleep(100);
//                        this.wait(1000);
                        System.out.print("\b\b\b");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("\b\b\b");
                System.out.println("100%\nFile Downloaded Successfully!");

            };
//            Thread displayPercentageThread = new Thread(runnable);
//            displayPercentageThread.start();

            supportsMultiPartsDownload = checkMultiPartDownloadSupport(urlObj);

            Runnable downloaderRunnable = () -> {

//                long i3 = System.nanoTime();
                long j = startingIndexForEachThread;

                try {

                    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                    urlConnection.addRequestProperty("Range", "bytes=" + j + "-" + (j + dxFilePartLength - 1));
                    urlConnection.setConnectTimeout(0);

                    String fileName = "A-" + ++indexOfFileParts + ".part";
                    File tempPartFile = new File(TEMP_DIR_PATH, fileName);

                    //  urlConnection.getResponseCode() --> Makes HTTP Request & gets the response code
                    int responseCode = urlConnection.getResponseCode();
                    System.out.println("\nHTTP Response Status Code : " + responseCode);

//                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
//                    for(Set.Entry<String, String> e : headerFields.entrySet())

                    ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream());
                    FileChannel fileChannel = new FileOutputStream(tempPartFile).getChannel();

                    /*
                        Time Stamp: 24th October 2K19, 07:29 AM..!!

                        Sets the upper bound limit for the file i.e. Maximum File size
                        Any Write Operation beyond this position will lead to IOException
                        So, limiting the each file part size equal to the dxFilePartLength

                     */
                    fileChannel.position(dxFilePartLength);

//                    System.out.println("Thread started with FP : " + j);

                    //  Initialize downloaderThread
                    //  indexOfFileParts --> Thread Serial ID
                    DownloaderThread downloaderThread = new DownloaderThread(tempPartFile, rbc, fileChannel, indexOfFileParts);
                    downloaderThreadList.add(downloaderThread);

                } catch (IOException e) {
                    System.out.println("IO ERROR [01] !!!");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("ERROR!!!");
                    e.printStackTrace();
                }
//                System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");
            };

            if (supportsMultiPartsDownload) {

                for (int i = 0; i < THREAD_COUNT; i++) {

                    System.out.println("Thread # : " + i);

//                DownloaderThread downloaderThread = new DownloaderThread(tempPartFile);
                    Thread thread = new Thread(downloaderRunnable);
                    thread.start();
//                threads[i] = acquireNewDownloadThread();
//                Thread.sleep(2000);
                    thread.join();
                    startingIndexForEachThread += dxFilePartLength;

                }

                if (remainingDataToDownload != 0) {
                    Thread thread = new Thread(downloaderRunnable);
                    thread.start();
                    thread.join();
                }

            } else {
                downloadWithSingleThread(urlObj, fileLength);
            }

            for (DownloaderThread downloaderThread : downloaderThreadList) {
                downloaderThread.start();
                downloaderThread.join();
                filePartsList.add(downloaderThread.getTempPartFile());
                System.out.println("Downloaded tempPartFile : " + downloaderThread.getTempPartFile());
            }

            System.out.println("Download Completed! | FL : " + fileLength +
                    "\nMerging Downloaded Files now...");
            combineFileParts(filePartsList, targetFile, fileLength);

//            Thread.sleep(20);
//            displayPercentageThread.join();
//            System.out.println("fp : " + sharedCurrentFilePointer);
//            sharedCurrentFilePointer = 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("IO ERROR [02] !!!");
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);

        reset();

    }

    private void downloadWithSingleThread(URL urlObj, long fileLength) {


//        long i3 = System.nanoTime();
//        long j = startingIndexForEachThread;

        System.out.println("Inside downloadWithSingleThread\nFileLength : " + fileLength);
        try {

            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setConnectTimeout(0);

            String fileName = "A-" + ++indexOfFileParts + ".part";
            File tempPartFile = new File(TEMP_DIR_PATH, fileName);

            //  urlConnection.getResponseCode() --> Makes HTTP Request & gets the response code
            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nHTTP Response Status Code : " + responseCode);

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            for (Map.Entry<String, List<String>> e : headerFields.entrySet())
                System.out.println(e.getKey() + " : " + e.getValue().get(0));
            System.out.println("=====================");


            ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream());
            FileChannel fileChannel = new FileOutputStream(tempPartFile).getChannel();

                    /*
                        Time Stamp: 24th October 2K19, 07:29 AM..!!

                        Sets the upper bound limit for the file i.e. Maximum File size
                        Any Write Operation beyond this position will lead to IOException
                        So, limiting the each file part size equal to the dxFilePartLength

                     */
//            fileChannel.position(fileLength);

//                    System.out.println("Thread started with FP : " + j);

            //  Initialize downloaderThread
            //  indexOfFileParts --> Thread Serial ID
            DownloaderThread downloaderThread = new DownloaderThread(tempPartFile, rbc, fileChannel, indexOfFileParts);
            downloaderThreadList.add(downloaderThread);

        } catch (IOException e) {
            System.out.println("IO ERROR [01] !!!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("ERROR!!!");
            e.printStackTrace();
        }
//                System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");

    }

    private boolean checkMultiPartDownloadSupport(URL urlObj) {

        int responseCode = -1;
        HttpURLConnection urlConnection;

        try {
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.addRequestProperty("Range", "bytes=" + 0 + "-" + 1);
            responseCode = urlConnection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //  urlConnection.getResponseCode() --> Makes HTTP Request & gets the response code
        System.out.println("Checking MultiPartDownloadSupport\nHTTP Response Status Code : " + responseCode);

        return responseCode == 206;

    }

    public void downloadOffUrl(String resourceUrl, File targetFile) {

        long i1 = System.nanoTime();

        System.out.println("URL : " + resourceUrl);

        try {

            final URL urlObj = new URL(resourceUrl);
            final long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            System.out.println("File Length 0: " + fileLength);

            final long dx = fileLength / THREAD_COUNT;   //  Downloaded by Each Thread
            final long remainingDataToDownload = fileLength % THREAD_COUNT; //  Download At End

            Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
                while (sharedCurrentFilePointer < fileLength) {
                    System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                    try {
                        Thread.sleep(100);
//                        this.wait(1000);
                        System.out.print("\b\b\b");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("\b\b\b");
                System.out.println("100%\nFile Downloaded Successfully!");

            };
            Thread displayPercentageThread = new Thread(runnable);
            displayPercentageThread.start();

            Runnable downloaderRunnable = () -> {

                long i3 = System.nanoTime();
                try {

                    long j = startingIndexForEachThread;

                    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                    urlConnection.addRequestProperty("Range", "bytes=" + j + "-" + (j + dx - 1));
                    urlConnection.setConnectTimeout(0);

                    String fileName = "A-" + ++indexOfFileParts + ".part";
                    File tempTargetFile1 = new File(TEMP_DIR_PATH, fileName);

                    int rc = urlConnection.getResponseCode();
                    System.out.println("\nnum1 : " + rc);

                    try (ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(tempTargetFile1);
                         FileChannel fileChannel = fileOutputStream.getChannel()) {

                        System.out.println("Thread started with FP : " + j);
                        long tempTotalBytesTransferred = 0;
                        tempTotalBytesTransferred += fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
                        System.out.println("temp 1: " + tempTotalBytesTransferred);

                        updateSharedCurrentFilePointer(tempTotalBytesTransferred);
                        filePartsList.add(tempTargetFile1);
                        System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);

                    }


                } catch (IOException e) {
                    System.out.println("IO ERROR [01] !!!");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("ERROR!!!");
                    e.printStackTrace();
                }
                System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");
            };

            Thread[] threads = new Thread[THREAD_COUNT];

            for (int i = 0; i < THREAD_COUNT; i++) {

//                Thread.sleep(50);
                System.out.println("Thread # : " + i);
                Thread thread = new Thread(downloaderRunnable);
//                while (!shouldCreateNewThread);
                thread.start();
//                shouldCreateNewThread = false;
                threads[i] = thread;
                Thread.sleep(2000);
//                thread.join();
                startingIndexForEachThread += dx;

            }

            for (Thread t : threads) {
                t.join();
//                System.out.println("f : " + sharedCurrentFilePointer);
            }

            if (remainingDataToDownload != 0)
                downloadLeftover(resourceUrl);

            System.out.println("Download Completed!" +
                    "\nMerging Downloaded Files now...");
            combineFileParts(filePartsList, targetFile, fileLength);

            Thread.sleep(20);
            displayPercentageThread.join();
            System.out.println("fp : " + sharedCurrentFilePointer);
//            sharedCurrentFilePointer = 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("IO ERROR [02] !!!");
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);

        reset();

    }

    private void downloadLeftover(String resourceUrl) {

        /*
            orig :      14765858
            dx =         3691464        7382928         11074392            14765856
            downloaded   0-3691463  3691464-7382927    7382928-11074391  11074392-14765855
            remaining :

         */
        long i3 = System.nanoTime();
        try {

            long j = startingIndexForEachThread;
            System.out.println("jjjj == >  " + j);
            URL urlObj0 = new URL(resourceUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj0.openConnection();
            urlConnection.addRequestProperty("Range", "bytes=" + j + "-");// /*j + */"-" + ( remainingDataToDownload));

            String fileName = "A-" + ++indexOfFileParts + ".part";
            File tempTargetFile1 = new File(TEMP_DIR_PATH, fileName);

            int rc = urlConnection.getResponseCode();
            if (rc < 0)
                System.out.println("\nnum1 : " + rc);
            else
                System.out.println("\nnum2 : " + rc);

            try (ReadableByteChannel readableByteChannel = Channels.newChannel(urlConnection.getInputStream());
                 FileChannel fileChannel = new FileOutputStream(tempTargetFile1).getChannel()) {

                long tempTotalBytesTransferred = 0;
                System.out.println("Thread started with FP : " + j);

                tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                System.out.println("temp 1: " + tempTotalBytesTransferred);
                updateSharedCurrentFilePointer(tempTotalBytesTransferred);
                filePartsList.add(tempTargetFile1);
                System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);
            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO ERROR!!!");
        }
        System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");

    }

    private static synchronized void updateSharedCurrentFilePointer(long f) {
        sharedCurrentFilePointer += f;
    }

    private void reset() {

        sharedCurrentFilePointer = 0;
        startingIndexForEachThread = 0;
        indexOfFileParts = 0;

        try {

            for (File f : filePartsList) Files.delete(f.toPath());

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

//            String fName = "A-" + i + ".part";
            File f = filePartsList.get(i - 1);
//            System.out.println("f : " + f.getName());
//            System.out.println("pos : " + pos);

            try (ReadableByteChannel rbc = Channels.newChannel(new FileInputStream(f));
                 FileChannel fc = new FileOutputStream(destCombinedFile, true).getChannel()) {

                System.out.println("Combining Part - " + i);
                //  Although stable but Uncertain about logic for position method with pos*pos as an arg.
//                System.out.println("after pos * pos");

                try {
                    fc.position(fileLength + 1);
//                    System.out.println("Current Part Length : " + f.length());
                    fc.transferFrom(rbc, pos, Long.MAX_VALUE);
                } catch (IOException e) {
                    System.out.println("IO Exception During Transfer [Merging]" +
                            "\nError Msg. : " + e.getMessage());
                    e.printStackTrace();
                }

//                System.out.println("After transfer");
                pos += f.length();

            } catch (IOException e) {
                System.out.println("IO Exception [03]");
                e.printStackTrace();
            }

        }
        System.out.println("Completed Combining File Parts.");
        System.out.println("Combined File Size : " + destCombinedFile.length());

        System.out.println((System.nanoTime() - i1) / 1E9 + " seconds");

    }

}
