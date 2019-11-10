package workspace;

import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.utils.DownloadManager;
import libs.koh_youtube_dl.utils.FFMPEGWrapper;
import libs.koh_youtube_dl.utils.VideoQuality;
import libs.koh_youtube_dl.utils.VideoQualityOutOfScopeException;
import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;
import libs.koh_youtube_dl.youtubedl.YoutubeDLRequest;
import libs.koh_youtube_dl.youtubedl.YoutubeDLResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TeeVideoDownloader {

    private final String EXTENSION;
    private String tempDirPath;
    private int serialNumPrefix;
    private int totalNumOfVids;
    private volatile boolean isPlaylist;
    private boolean mustKeepMP4;
    private String mainUrl;
    private File srcDir;
    private File aFile;
    private File vFile;
    private VideoQuality foundTopScope;
    private VideoQuality preferredVideoQuality;
    private DownloadManager downloadManager;

    TeeVideoDownloader() {
        this(null, VideoQuality.Q_1080P, new File("."));
    }

    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File srcDir) {
        this(mainUrl, preferredVideoQuality, srcDir, false);
    }

    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File srcDir, boolean mustKeepMP4) {
        this.preferredVideoQuality = preferredVideoQuality;
        this.mainUrl = mainUrl;
        this.srcDir = srcDir;
        this.tempDirPath = srcDir + "/.temp";
        this.mustKeepMP4 = mustKeepMP4;
        this.EXTENSION = mustKeepMP4 ? "mp4" : "mkv";
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

        init();
        begin();

        long i2 = System.nanoTime();
        System.out.println("\n\nTotal Time : " + (i2 - i1) / 1E9);

        System.out.println("End.");

    }

    private void begin() {

        parseUrl(mainUrl);

    }

    private void initializeDataMembers() {

        Scanner scanner = new Scanner(System.in);
        if (mainUrl == null) {
            System.out.println("Enter MAIN URL: ");
            mainUrl = scanner.nextLine();
            System.out.println("Enter Src Dir.: ");
            srcDir = new File(scanner.nextLine());
            tempDirPath = srcDir + "/.temp";
            System.out.println("Download in MP4 Format Only? [Y/N] : ");
            mustKeepMP4 = scanner.nextLine().toLowerCase().charAt(0) == 'y';

            System.out.println("Enter Preferred Quality.: ");
            System.out.print("4000 | 2000 | 1080 | 720 |  480 | 360 | 240 | 144\n[?] : ");
            preferredVideoQuality = VideoQuality.chooseVideoQuality();
        }

    }

    private void init() {

        initializeDataMembers();

        downloadManager = new DownloadManager(tempDirPath);
        serialNumPrefix = 1;

        File file = new File(tempDirPath);
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

            if (videoInfoList.size() > 1) {
                isPlaylist = true;
                totalNumOfVids = videoInfoList.size();
            }

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
        AtomicBoolean shouldDirectlyCopyVideoStream = new AtomicBoolean(false);
        AtomicBoolean shouldDirectlyCopyAudioStreams = new AtomicBoolean(false);

        Comparator<VideoFormat> comparatorHighToLowResolution = Comparator.comparingInt(vf -> -1 * vf.width);
        Comparator<VideoFormat> comparatorHighToLowTBR = Comparator.comparingInt(vf -> -1 * vf.tbr);
        Comparator<VideoFormat> comparatorHighToLowABR = Comparator.comparingInt(vf -> -1 * vf.abr);

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
            /*if (!vf.acodec.contains("none") || !vf.vcodec.contains("none"))
                return false;*/
            return !vf.acodec.contains("none");

//            return vf.acodec.toLowerCase().contains("opus");
        };

//        Predicate<VideoFormat> filterOffVP9VCodec = vf -> !vf.vcodec.toLowerCase().contains("vp9");

        String divider = "\n\n\n===================================\n\n\n";
        System.out.println("Before Filter :" + divider);
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .forEach(System.out::println);

        System.out.println("After Filter :" + divider);

//        Consumer<VideoFormat> consumer = System.out::println;
        Consumer<VideoFormat> consumerDownloadVideoStream = vf -> {

            System.out.println("==vf : " + vf.ext);
            //  Check for MP4 format
            if (vf.ext.toLowerCase().equals("mp4") || !mustKeepMP4) shouldDirectlyCopyVideoStream.set(true);

            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            vFile = new File(tempDirPath, fName);
            downloadManager.downloadOffUrl2(vf.url, vFile);
        };

        Consumer<VideoFormat> consumerDownloadAudioStream = vf -> {

            boolean isAudioExtMp4 = vf.ext.equals("mp4");
            String targetExt;

            //  Check for M4A format
            if (vf.ext.equals("m4a") || isAudioExtMp4) shouldDirectlyCopyAudioStreams.set(true);

//            String audioExt = isAudioExtMp4 ? "_DUAL" : vf.ext;
            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            aFile = new File(tempDirPath, fName);
            downloadManager.downloadOffUrl2(vf.url, aFile);

            if (isAudioExtMp4) {
                targetExt = vf.acodec.contains("mp4a") ? "aac" : "m4a";
                try {
                    File tempFile = FFMPEGWrapper.extractAudioFileOffVideo(aFile, targetExt);
//                System.out.println("===!!!!");
                    Files.deleteIfExists(aFile.toPath());
                    aFile = tempFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //  Download Audio Stream..!!
        formats.stream()
                .filter(filterAudioCodecs)
                .sorted(comparatorHighToLowABR)
                .limit(1)
                .forEach(consumerDownloadAudioStream);

        //  Download Video Stream..!!
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .filter(filterQuality)
                .sorted(comparatorHighToLowTBR)
//                .filter(filterOffVP9VCodec)
                .limit(1)
                .forEach(consumerDownloadVideoStream);

//        System.out.println("== vFile : " + vFile.getAbsolutePath());
        //  Merge Video & Audio Streams (files) together using FFMPEG
        int dotIndex = vFile.getName().lastIndexOf('.');
        String ext = ".mkv";
        String targetFileName = "";

        if (isPlaylist) {
            if (totalNumOfVids < 10)
                targetFileName = String.format("%d. ", serialNumPrefix);
            else if (totalNumOfVids < 100)
                targetFileName = String.format("%2d. ", serialNumPrefix);
            else if (totalNumOfVids < 1000)
                targetFileName = String.format("%3d. ", serialNumPrefix);
            else
                targetFileName = String.format("%4d. ", serialNumPrefix);
            serialNumPrefix++;
        }

        targetFileName += vFile.getName().substring(0, dotIndex) + ext;
        File targetFile = new File(srcDir, targetFileName);

        //  Merge by using ffmpeg
        mergeVideoAndAudioStreams(targetFile, aFile, vFile, shouldDirectlyCopyAudioStreams.get(), shouldDirectlyCopyVideoStream.get());
        System.out.println("END!!!" + "\n\n\n===================================\n\n\n");

    }

    private void mergeVideoAndAudioStreams(File targetFile, File aFile, File vFile, boolean shouldDirectlyCopyAudioStream, boolean shouldDirectlyCopyVideoStream) {

        FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper(targetFile, aFile, vFile, shouldDirectlyCopyAudioStream, shouldDirectlyCopyVideoStream, this.EXTENSION);

        try {
            ffmpegWrapper.startMerging();
            Files.delete(vFile.toPath());
            Files.delete(aFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
