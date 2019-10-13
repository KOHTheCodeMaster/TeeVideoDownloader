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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TeeVideoDownloader {

    private static long sharedCurrentFilePointer;
    private VideoQuality preferredVideoQuality;
    private File srcDir;
    private String mainUrl;
    private VideoQuality foundTopScope;
    private File aFile;
    private File vFile;

    TeeVideoDownloader() {
        this(null, VideoQuality.Q_1080P, new File("."));
    }

    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File srcDir) {
        this.preferredVideoQuality = preferredVideoQuality;
        this.mainUrl = mainUrl;
        this.srcDir = srcDir;
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

    void start1() {

        System.out.println("Begin.");
        long i1 = System.nanoTime();

        begin();

        long i2 = System.nanoTime();
        System.out.println("\n\nTotal Time : " + (i2 - i1) / 1E9);

        System.out.println("End.");

    }

    private static void downloadOffUrl(String resourceUrl, File targetFile) {

        long i1 = System.nanoTime();

        int ONE_MB = (int) 1E6 * 2;
        int ONE_KB = (int) 1024;
        byte[] buffer = new byte[ONE_MB];

        System.out.println("===URL : " + resourceUrl);

        try {

            URL urlObj = new URL(resourceUrl);
            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            InputStream inputStream = urlObj.openStream();
//            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
//            FileChannel fileChannel = new FileOutputStream(targetFile)
//                    .getChannel();

/*
            for(String s : urlObj.openConnection().getHeaderFields().keySet())
                System.out.println(s);
            List<String> l1 = urlObj.openConnection().getHeaderFields().get("Content-Length");
            for(String s : l1)
                System.out.println(s);
*/
            System.out.println("File Length 1: " + fileLength);

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

            try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                 FileOutputStream fos = new FileOutputStream(targetFile)) {

                byte[] data = new byte[ONE_MB];
                int count;
                while ((count = bis.read(data, 0, ONE_MB)) != -1) {
                    fos.write(data, 0, count);
                    sharedCurrentFilePointer += count;
                }
            }

/*
            //  DAMAGE Entire File!
            for (long i = 0; i < fileLength; i += buffer.length) {
                fileChannel.transferFrom(readableByteChannel, i, buffer.length);
                sharedCurrentFilePointer = i;
            }
*/

            Thread.sleep(20);
            sharedCurrentFilePointer += buffer.length * 2;
            displayPercentageThread.join();
            sharedCurrentFilePointer = 0;

//            d1(fileLength);

//            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);

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
            vFile = new File(srcDir, title + "." + vf.ext);
            downloadOffUrl(vf.url, vFile);
        };

        Consumer<VideoFormat> consumerDownloadAudioStream = vf -> {
            aFile = new File(srcDir, title + "." + vf.ext);
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
        mergeVideoAndAudioStreams(srcDir, aFile, vFile);

        System.out.println("END!!!" + "\n\n\n===================================\n\n\n");

    }

    private void mergeVideoAndAudioStreams(File srcDir, File aFile, File vFile) {

        FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper(srcDir, aFile, vFile);

        try {
            ffmpegWrapper.startMerging();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
 *  3d Commit - [Downloading but Messed-up]
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