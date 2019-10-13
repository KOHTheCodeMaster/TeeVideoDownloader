package workspace;

import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.utils.FFMPEGWrapper;
import libs.koh_youtube_dl.utils.VideoQuality;
import libs.koh_youtube_dl.utils.VideoQualityOutOfScopeException;
import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;
import libs.koh_youtube_dl.youtubedl.YoutubeDLRequest;
import libs.koh_youtube_dl.youtubedl.YoutubeDLResponse;

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
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static workspace.A1.filePartsList;

public class TeeVideoDownloader {

    private static final int THREAD_COUNT = 4;
    private static long sharedCurrentFilePointer;
    private static long startingIndexForEachThread;
    private final String TEMP_DIR_PATH;
    private int indexFileName;
    private volatile boolean shouldCreateNewThread = true;
    private String mainUrl;
    private File srcDir;
    private File aFile;
    private File vFile;
    private VideoQuality foundTopScope;
    private VideoQuality preferredVideoQuality;

    TeeVideoDownloader() {
        this(null, VideoQuality.Q_1080P, new File("."));
    }

    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File srcDir) {
        this.preferredVideoQuality = preferredVideoQuality;
        this.mainUrl = mainUrl;
        this.srcDir = srcDir;

        TEMP_DIR_PATH = srcDir + "/.temp";
    }

    public static void main(String[] args) {

//        TeeVideoDownloader obj = new TeeVideoDownloader();
//        obj.start1();
//        major();

    }

    private static void f1() {
//        String url = "https://www.youtube.com/watch?v=thwrK4tHbwo";
//        String url = "https://www.youtube.com/watch?v=HHYcxYVM1DM";
//        String url = "https://www.youtube.com/watch?v=LXb3EKWsInQ&t=19s";
//        String url = "https://www.youtube.com/watch?v=1La4QzGeaaQ";
//        String url = "https://www.youtube.com/watch?v=LRJmUWfI0ZY"; //  10 sec
//        String url = "https://www.youtube.com/watch?v=Wjrrgrvq1ew"; //  10 sec
//        String url = "https://www.youtube.com/watch?v=fPrixQcSPyM"; //  Avengers
//        String url = "https://www.youtube.com/watch?v=CXjjGE3k5PY"; //  16 sec
//        String url = "https://www.youtube.com/watch?v=zJ7hUvU-d2Q"; //  16 sec
        String url = "https://www.youtube.com/watch?v=2uMc3rNnTo4"; //  1 hr

        try {

            VideoInfo videoInfo = YoutubeDL.getVideoInfoList(url).get(0);

//            System.out.println(videoInfo.formats);
//            System.out.println(videoInfo);
//            Predicate<VideoFormat> predicate = vf -> vf.formatId.equals("22");
//            Predicate<VideoFormat> filterWidthBelow600 = vf -> vf.width > 600;
            Comparator<VideoFormat> comparatorHighToLowResolution =
                    Comparator.comparingInt(vf -> -1 * (vf.width + vf.height));
//            Consumer<VideoFormat> consumer = c -> c.
            List<VideoFormat> filteredVideoFormats = videoInfo.formats
                    .stream()
//                    .filter(filterWidthBelow600)
                    .sorted(comparatorHighToLowResolution)
                    .collect(Collectors.toList());

            System.out.println(filteredVideoFormats);


        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }

    }

    private static void major() {

        // Video url to download
        String videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

        // Destination directory
        String directory = "res/downloaded/2";

        // Build request
        YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, directory);
        request.setOption("ignore-errors");        // --ignore-errors
        request.setOption("output", "%(title)s.mp4");    // --output "%(id)s"
        request.setOption("retries", 10);        // --retries 10

        // Make request and return response
        YoutubeDLResponse response = null;
        try {
            response = YoutubeDL.execute(request);
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }

        // Response
//        String stdOut = response.getOut(); // Executable output

        System.out.println(response);

    }

    private static synchronized void updateSharedCurrentFilePointer(long f) {
        sharedCurrentFilePointer += f;
    }

    void start1() {

        System.out.println("Begin.");
        long i1 = System.nanoTime();

        init();
        begin();

        long i2 = System.nanoTime();
        System.out.println("\n\nTotal Time : " + (i2 - i1) / 1E9);

        System.out.println("End.");

    }

    private void begin() {

        Scanner scanner = new Scanner(System.in);

        if (mainUrl == null) {
            System.out.println("Enter MAIN URL: ");
            mainUrl = scanner.nextLine();
            System.out.println("Enter Src Dir.: ");
            srcDir = new File(scanner.nextLine());
            System.out.println("Enter Preferred Quality.: ");
            preferredVideoQuality = VideoQuality.chooseVideoQuality();
        }

        parseUrl(mainUrl);

    }

    private void init() {

        filePartsList = new ArrayList<>();

        File file = new File(TEMP_DIR_PATH);
        if (!file.isDirectory() && !file.mkdirs()) {
            System.out.println("Failed to Create Temp Dir. to store temporary files");
            System.exit(-17);
        }

    }

    private void parseUrl(String url) {

        try {

            Consumer<VideoInfo> consumerParseVideoInfo = vi ->
                    downloadVI(vi.formats, srcDir, preferredVideoQuality, vi.title);

            List<VideoInfo> videoInfoList = YoutubeDL.getVideoInfoList(url);
            videoInfoList.forEach(consumerParseVideoInfo);

        } catch (YoutubeDLException e) {
            e.printStackTrace();
            System.out.println("URL Not Supported!\n" +
                    "Try Again with Another Website...");
            System.exit(-17);
        }

    }

    private void downloadVI(ArrayList<VideoFormat> formats, File srcDir, VideoQuality preferredVideoQuality, String title) {

        foundTopScope = null;

        Comparator<VideoFormat> comparatorHighToLowResolution = Comparator.comparingInt(vf -> -1 * vf.width);
        Comparator<VideoFormat> comparatorHighToLowTBR = Comparator.comparingInt(vf -> -1 * vf.tbr);

        Predicate<VideoFormat> filterQuality = vf -> {
            try {

                //  In case of width <= 0 i.e. No Video Codec found,
                //  discard current vf by returning false
                if (vf.width <= 0)
                    return false;

                VideoQuality currentVQ = VideoQuality.parseWidthToVideoQuality(vf.width);

                if (foundTopScope == null) {
                    foundTopScope = preferredVideoQuality.acquireTopmostPreferredVQ(currentVQ);

                    //  When currentVQ > preferredVideoQuality
                    if (foundTopScope == null)
                        return false;

                    System.out.println("FTS : " + foundTopScope);
                    return true;
                }

                return (currentVQ.equals(foundTopScope));

            } catch (VideoQualityOutOfScopeException e) {
                e.printStackTrace();
                return false;
            }
        };

        Predicate<VideoFormat> filterAudioCodecs = vf -> {

            //  Discard if Acodec doesn't exists or if Vcodec does exists
            if (vf.acodec.contains("none") || !vf.vcodec.contains("none"))
                return false;

            return !vf.acodec.toLowerCase().contains("opus");
        };

        Predicate<VideoFormat> filterOffVP9VCodec = vf -> !vf.vcodec.toLowerCase().contains("vp9");

        String divider = "\n\n\n===================================\n\n\n";
        System.out.println("Before Filter :" + divider);
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .forEach(System.out::println);

        System.out.println("After Filter :" + divider);

//        Consumer<VideoFormat> consumer = System.out::println;
        Consumer<VideoFormat> consumerDownloadVideoStream = vf -> {
            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            vFile = new File(TEMP_DIR_PATH, fName);
            downloadOffUrl(vf.url, vFile);
        };

        Consumer<VideoFormat> consumerDownloadAudioStream = vf -> {
            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            aFile = new File(TEMP_DIR_PATH, fName);
            downloadOffUrl(vf.url, aFile);
        };

        //  Download Video Stream..!!
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .filter(filterQuality)
                .sorted(comparatorHighToLowTBR)
                .filter(filterOffVP9VCodec)
                .limit(1)
                .forEach(consumerDownloadVideoStream);

        //  Download Audio Stream..!!
        formats.stream()
                .filter(filterAudioCodecs)
                .sorted(comparatorHighToLowTBR)
                .limit(1)
                .forEach(consumerDownloadAudioStream);

        //  Merge Video & Audio Streams (files) together using FFMPEG
        int dotIndex = vFile.getName().lastIndexOf('.');
        String ext = ".mkv";
        String targetFileName = vFile.getName().substring(0, dotIndex) + ext;
//        targetFileName = targetFileName.replaceAll("\\\\/", "-");
        File targetFile = new File(srcDir, targetFileName);

        mergeVideoAndAudioStreams(targetFile, aFile, vFile);

        System.out.println("END!!!" + "\n\n\n===================================\n\n\n");

    }

    private void downloadOffUrl(String resourceUrl, File targetFile) {

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

                    String fileName = "A-" + ++indexFileName + ".part";
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
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR!!!");
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
                shouldCreateNewThread = false;
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
            combineFileParts(filePartsList, targetFile);

            Thread.sleep(20);
            displayPercentageThread.join();
            System.out.println("fp : " + sharedCurrentFilePointer);
//            sharedCurrentFilePointer = 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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

            String fileName = "A-" + ++indexFileName + ".part";
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

    private void combineFileParts(List<File> filePartsList, File destinationFile) {

        long i1 = System.nanoTime();

        long pos = 0;

        for (int i = 1; i <= filePartsList.size(); i++) {

            String fName = "A-" + i + ".part";
            File f = new File(TEMP_DIR_PATH, fName);
            System.out.println("f : " + f.getName());
            System.out.println("pos : " + pos);

            try (FileInputStream fis = new FileInputStream(f);
                 ReadableByteChannel rbc = Channels.newChannel(fis);
                 FileOutputStream fos = new FileOutputStream(destinationFile, true);
                 FileChannel fc = fos.getChannel()) {

                fc.position(pos * pos);
                fc.transferFrom(rbc, pos, Long.MAX_VALUE);
                pos += f.length();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Target File Size : " + destinationFile.length());


        System.out.println((System.nanoTime() - i1) / 1E9 + " seconds");

    }

    private void mergeVideoAndAudioStreams(File targetFile, File aFile, File vFile) {

        FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper(targetFile, aFile, vFile);

        try {
            ffmpegWrapper.startMerging();
            Files.delete(vFile.toPath());
            Files.delete(aFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reset() {

        sharedCurrentFilePointer = 0;
        startingIndexForEachThread = 0;
        indexFileName = 0;

        try {

            for (File f : filePartsList) Files.delete(f.toPath());

        } catch (IOException e) {
            System.out.println("Unable to Clean Downloaded File Parts...");
            e.printStackTrace();
        }
        System.out.println("File Parts Cleaned Successfully..!!");

        filePartsList = null;
        filePartsList = new ArrayList<>();

    }

}

/*
 *  Date Created: 25th April 2K19, 09:48 AM..!!
 *  Time Stamp: 21st September 2K19, 08:47 PM..!!
 *
 *  Project Name : Tee Video Downloader [TVD]
 *
 *  Status: Work In Progress
 *
 *  4th Commit - [Stable Downloading Playlist]
 *
 *  3rd Commit - [Downloading but Messed-up]
 *
 *  2nd Commit - [Debugging to Understand]
 *
 *  Init Commit. [Download Video]
 *
 *  Warning:
 *  This project is solely for educational purpose.
 *  It doesn't supports any piracy or copyright violations.
 *
 *  Code Developed By,
 *  ~K.O.H..!! ^__^
 */