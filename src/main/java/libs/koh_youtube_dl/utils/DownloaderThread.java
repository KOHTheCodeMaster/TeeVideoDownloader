package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

public class DownloaderThread extends Thread {

    private final static int MAX_RETRY_ATTEMPTS = 5;
    private final int THREAD_SERIAL_ID;
    private File tempPartFile;
    private MyConnectionUtil myConnectionUtil;

    DownloaderThread(File tempPartFile, int THREAD_SERIAL_ID, MyConnectionUtil myConnectionUtil) {
        this.tempPartFile = tempPartFile;
        this.THREAD_SERIAL_ID = THREAD_SERIAL_ID;
        this.myConnectionUtil = myConnectionUtil;
    }

    @Override
    public void run() {

        /*
            Retrying Failed Downloads up-to 5 times to handle following Exception

            javax.net.ssl.SSLException:
            Connection reset at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:127)

         */
//        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Started.");

        long tempTotalBytesTransferred;
        FileChannel targetFileChannel = null;
        ReadableByteChannel readableByteChannel = null;

        int retryAttempts = 0;
        while (retryAttempts <= MAX_RETRY_ATTEMPTS) {
            try {
                targetFileChannel = new FileOutputStream(tempPartFile).getChannel();
                HttpURLConnection urlConnection = myConnectionUtil.acquireUrlConnectionWith206ResponseCode();
                readableByteChannel = Channels.newChannel(urlConnection.getInputStream());

                final long originalLength = Long.parseLong(urlConnection.getHeaderFields().get("Content-Length").get(0));
//                System.out.println("Original File Length : " + fileLength);

//                System.out.println("Downloading Data... : " + THREAD_SERIAL_ID);
                tempTotalBytesTransferred = 0;
                tempTotalBytesTransferred += targetFileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

                if (tempTotalBytesTransferred != originalLength)
                    System.out.println("ERROR | Exception : PARTIAL Download...");
                break;

            } catch (IOException e) {

                System.out.println("IO Exception | DownloaderThread | [01] |");
                System.out.println("Current OBJ State : " + this.toString());
                targetFileChannel = null;
                readableByteChannel = null;
                e.printStackTrace();
                deleteCurrentPartFile();
                System.out.println("Handled Download Failure : " + retryAttempts);
                retryAttempts++;
            }
        }

        try {
//            System.out.println("Closing Channels..!!");

            if (targetFileChannel != null) targetFileChannel.close();
            else System.out.println("TF Channel is NULL...");
            if (readableByteChannel != null) readableByteChannel.close();
            else System.out.println("RB Channel is NULL...");

        } catch (IOException e) {
            System.out.println("IO Exception | DownloaderThread | [02] |");
            e.printStackTrace();
        }

//        System.out.println("Thread [" + THREAD_SERIAL_ID + "] Completed." +
//                "\nBytes Downloaded : " + tempTotalBytesTransferred);

    }

    private void deleteCurrentPartFile() {

        try {
            Files.deleteIfExists(this.tempPartFile.toPath());
        } catch (IOException e) {
            System.out.println("IO Exception | DownloaderThread | [03] |");
            e.printStackTrace();
        }

    }

    File getTempPartFile() {
        return tempPartFile;
    }


}

class MyConnectionUtil {

    private long startingIndexForEachThread;
    private long dxFilePartLength;
    private String resourceUrl;

    MyConnectionUtil(String resourceUrl, long startingIndexForEachThread, long dxFilePartLength) {
        this.startingIndexForEachThread = startingIndexForEachThread;
        this.dxFilePartLength = dxFilePartLength;
        this.resourceUrl = resourceUrl;
    }

    static HttpURLConnection establishHttpUrlConnection(String resourceUrl) {

        HttpURLConnection urlConnection = null;
        int connectionAttempts = 0;

        while (connectionAttempts < 5) {
            try {

                final URL urlObj = new URL(resourceUrl);
                urlConnection = (HttpURLConnection) urlObj.openConnection();
                break;

            } catch (ConnectException e) {
                System.out.println("Exception [05] : Unable to establish Connection");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                System.out.println("MalformedException [01] : MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException [06] : IOException during establishing connection");
                e.printStackTrace();
            }
            connectionAttempts++;
        }

        return urlConnection;
    }

    HttpURLConnection acquireUrlConnectionWith206ResponseCode() {

        HttpURLConnection urlConnection = null;
        int connectionAttempts = 0;
        int responseCode;

        while (connectionAttempts < 5) {
            if (connectionAttempts > 0)
                System.out.println("Retry Attempt : " + connectionAttempts);
//            System.out.println("Acquiring Responsive Connection... : " + connectionAttempts);
            try {

                urlConnection = MyConnectionUtil.establishHttpUrlConnection(resourceUrl);
                urlConnection.addRequestProperty("Range", "bytes=" + startingIndexForEachThread + "-" + (startingIndexForEachThread + dxFilePartLength - 1));
                urlConnection.setConnectTimeout(0);

                //  TODO: Handle Connection Time Out Exception
                responseCode = urlConnection.getResponseCode();

                if (responseCode != 200 && responseCode != 206) {
                    System.out.println("Response Code is : " + responseCode);
                    connectionAttempts++;
                    continue;
                }
                return urlConnection;

            } catch (ConnectException e) {
                System.out.println("Exception [06] : Unable to establish Connection");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                System.out.println("MalformedException [02] : MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException [07] : IOException during establishing connection");
                e.printStackTrace();
            }
            connectionAttempts++;

        }

        return urlConnection;
    }

}