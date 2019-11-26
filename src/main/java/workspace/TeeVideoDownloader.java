package workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.koh.stdlib.utils.MyTimer;
import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.utils.*;
import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The type Tee video downloader.
 */
public class TeeVideoDownloader {

    private String EXTENSION;
    private String tempDirPath;
    private int serialNumPrefix;
    private int totalNumOfVids;
    private boolean isPlaylist;
    private boolean mustKeepMP4;
    private String mainUrl;
    private File defaultDownloadDir;
    private File aFile;
    private File vFile;
    private VideoQuality foundTopScope;
    private VideoQuality preferredVideoQuality;
    private DownloadManager downloadManager;
    private MyTimer myTimer;

    /**
     * Instantiates a new Tee video downloader.
     *
     * @param mainUrl               main url for youtube video or playlist
     * @param preferredVideoQuality the preferred video quality for downloads
     * @param defaultDownloadDir    default save directory for downloads
     */
    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File defaultDownloadDir) {
        this(mainUrl, preferredVideoQuality, defaultDownloadDir, false);
    }

    /**
     * Instantiates a new Tee video downloader.
     *
     * @param mainUrl               main url for youtube video or playlist
     * @param preferredVideoQuality the preferred video quality for downloads
     * @param defaultDownloadDir    default save directory for downloads
     * @param mustKeepMP4           must keep the files in MP4 format & if not found then forcefully convert in MP4 using ffmpeg
     */
    TeeVideoDownloader(String mainUrl, VideoQuality preferredVideoQuality, File defaultDownloadDir, boolean mustKeepMP4) {
        this.preferredVideoQuality = preferredVideoQuality;
        this.mainUrl = mainUrl;
        this.defaultDownloadDir = defaultDownloadDir;
        this.tempDirPath = defaultDownloadDir + "/.temp";
        this.mustKeepMP4 = mustKeepMP4;
        this.EXTENSION = mustKeepMP4 ? "mp4" : "mkv";
        this.myTimer = new MyTimer();
    }

    /**
     * Begin TVD.
     */
    void begin() {

        System.out.println("Begin.");
        long i1 = System.nanoTime();

        init();
        parseUrl(mainUrl);

        long i2 = System.nanoTime();
        System.out.println("\n\nTotal Time : " + (i2 - i1) / 1E9);

        System.out.println("End.");

    }

    private void init() {

        initializeDataMembers();
        try {
            fixMainUrl();
        } catch (InvalidUrlException e) {
            System.out.println("InvalidUrlException [01] | Unable to Fix MainUrl");
            e.printStackTrace();
        }

        downloadManager = new DownloadManager(tempDirPath);
        serialNumPrefix = 1;

        File file = new File(tempDirPath);
        if (!file.isDirectory() && !file.mkdirs()) {
            System.out.println("Failed to Create Temp Dir. to store temporary files");
            System.exit(-17);
        }

    }

    /**
     * Fix the mainUrl for single video url by stripping off the additional characters.
     * Single Video url consists of "youtube.com/watch?v=" followed by 11 characters of unique video id.
     */
    private void fixMainUrl() throws InvalidUrlException {

        if (!mainUrl.startsWith("https://www.youtube.com") || mainUrl.length() < 43)
            throw new InvalidUrlException(mainUrl);

        if (mainUrl.startsWith("https://www.youtube.com/playlist?list=")) {
            isPlaylist = true;
            return;
        }

        final int YOUTUBE_VIDEO_ID_LENGTH = 11;
        final int MINIMUM_VALID_URL_LENGTH = 43;
        final String singleVideoUrlPreset = "youtube.com/watch?v=";
        final int pos = mainUrl.indexOf(singleVideoUrlPreset);

        //  https://www.youtube.com/watch?v=123456789AB     -   43 Characters
        if (pos > 0 && mainUrl.length() >= MINIMUM_VALID_URL_LENGTH) {  //  mainUrl contains singleVideoUrlPreset
            mainUrl = mainUrl.substring(0, pos + singleVideoUrlPreset.length() + YOUTUBE_VIDEO_ID_LENGTH);
            isPlaylist = false;
        }

    }

    private void initializeDataMembers() {

        Scanner scanner = new Scanner(System.in);
        if (mainUrl == null) {
            System.out.println("Enter MAIN URL: ");
            mainUrl = scanner.nextLine();
            System.out.println("Enter Src Dir.: ");
            defaultDownloadDir = new File(scanner.nextLine());
            tempDirPath = defaultDownloadDir + "/.temp";

            System.out.println("Download in MP4 Format Only? [Y/N] : ");
            mustKeepMP4 = scanner.nextLine().toLowerCase().charAt(0) == 'y';
            this.EXTENSION = mustKeepMP4 ? "mp4" : "mkv";

            System.out.println("Enter Preferred Quality.: ");
            System.out.print("4000 | 2000 | 1080 | 720 |  480 | 360 | 240 | 144\n[?] : ");
            preferredVideoQuality = VideoQuality.chooseVideoQuality();
        }

    }

    private void parseUrl(String url) {

        Consumer<VideoInfo> consumerParseVideoInfo = vi ->
                downloadVI(vi.formats, preferredVideoQuality, vi.title);

        List<VideoInfo> videoInfoList;

        try {

            if (isPlaylist) {

                //  Instantiate youtubePlaylistPOJO
                YoutubePlaylistPOJO youtubePlaylistPOJO = new ScrapperYoutubePlaylist().acquireYTPLPOJO(url);
                String fullyQualifiedPlaylistName = youtubePlaylistPOJO.acquireFullyQualifiedPlaylistName();
                totalNumOfVids = youtubePlaylistPOJO.getVideosCount();

                //  Update defaultDownloadDir to fullyQualifiedPlaylistName
                this.defaultDownloadDir = new File(defaultDownloadDir, fullyQualifiedPlaylistName);

                //  Create defaultDownloadDir
                if (this.defaultDownloadDir.mkdirs()) System.out.println("Subfolder Created Successfully : "
                        + this.defaultDownloadDir.getAbsolutePath());
                else System.out.println("ERROR : Failed to create Subfolder : "
                        + this.defaultDownloadDir.getAbsolutePath());

                //  Save VideoInfoList as Json file in directory : defaultDownloadDir/.temp/JSONs/
                File jsonDir = new File(tempDirPath, "JSONs");

                if (!jsonDir.exists() && !jsonDir.mkdirs())
                    System.out.println("Unable to create JSONs Directory...");

                File ytplJsonFile = new File(jsonDir, fullyQualifiedPlaylistName + ".json");
                YoutubeDL.saveVideoInfoListToJsonFile(url, ytplJsonFile);
                videoInfoList = Arrays.asList(new ObjectMapper().readValue(ytplJsonFile, VideoInfo[].class));

            } else {
                videoInfoList = YoutubeDL.getVideoInfoList(mainUrl);
                System.out.println("Found Single Video!");
            }

            videoInfoList.forEach(consumerParseVideoInfo);

        } catch (YoutubeDLException e) {
            System.out.println("YoutubeDLException [01] | Parsing URL Failed...");
            e.printStackTrace();
            System.exit(-17);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void downloadVI(ArrayList<VideoFormat> formats, VideoQuality preferredVideoQuality, String title) {

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
//                    System.out.println("FTS : " + foundTopScope);
                    return foundTopScope == null;
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
/*
        System.out.println("Before Filter :" + divider);
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .forEach(System.out::println);

        System.out.println("After Filter :" + divider);
*/

        Consumer<VideoFormat> consumerDownloadVideoStream = vf -> {

            //  Check for MP4 format
            if (vf.ext.toLowerCase().equals("mp4") || !mustKeepMP4) shouldDirectlyCopyVideoStream.set(true);

            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            vFile = new File(tempDirPath, fName);
            downloadManager.downloadOffUrl(vf.url, vFile);
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
            downloadManager.downloadOffUrl(vf.url, aFile);

            if (isAudioExtMp4) {
                targetExt = vf.acodec.contains("mp4a") ? "aac" : "m4a";
                try {
                    File tempFile = FFMPEGWrapper.extractAudioFileOffVideo(aFile, targetExt);
                    Files.deleteIfExists(aFile.toPath());
                    aFile = tempFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        System.out.println("Downloading Audio Now!");
        //  Download Audio Stream..!!
        formats.stream()
                .filter(filterAudioCodecs)
                .sorted(comparatorHighToLowABR)
                .limit(1)
                .forEach(consumerDownloadAudioStream);

        System.out.println("Audio Part Downloaded Successfully!");
        System.out.println("Downloading Video Now!");
        //  Download Video Stream..!!
        formats.stream()
                .sorted(comparatorHighToLowResolution)
                .filter(filterQuality)
                .sorted(comparatorHighToLowTBR)
                .limit(1)
                .forEach(consumerDownloadVideoStream);
        System.out.println("Video Part Downloaded Successfully!");
        System.out.println("Merging Audio and Video Files now!");

        //  Merge Video & Audio Streams (files) together using FFMPEG
        int dotIndex = vFile.getName().lastIndexOf('.');
        String ext = ".mkv";
        String targetFileName = "";

        if (isPlaylist) {
            if (totalNumOfVids < 10)
                targetFileName = String.format("%d. ", serialNumPrefix);
            else if (totalNumOfVids < 100)
                targetFileName = String.format("%02d. ", serialNumPrefix);
            else if (totalNumOfVids < 1000)
                targetFileName = String.format("%03d. ", serialNumPrefix);
            else
                targetFileName = String.format("%04d. ", serialNumPrefix);
            serialNumPrefix++;
        }

        targetFileName += vFile.getName().substring(0, dotIndex) + ext;
        targetFileName = targetFileName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
        File targetFile = new File(this.defaultDownloadDir, targetFileName);

        //  Merge by using ffmpeg
        mergeVideoAndAudioStreams(targetFile, aFile, vFile, shouldDirectlyCopyAudioStreams.get(), shouldDirectlyCopyVideoStream.get());
        System.out.println("END..!!" + "\n\n\n===================================\n\n\n");

    }

    private void mergeVideoAndAudioStreams(File targetFile, File aFile, File vFile, boolean shouldDirectlyCopyAudioStream, boolean shouldDirectlyCopyVideoStream) {

        FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper(targetFile, aFile, vFile, shouldDirectlyCopyAudioStream, shouldDirectlyCopyVideoStream, this.EXTENSION);
//        System.out.println("EXTENSION : " + EXTENSION);
//        System.out.println("tf : " + targetFile.getAbsolutePath());
        try {
            ffmpegWrapper.startMerging();
            Files.delete(vFile.toPath());
            Files.delete(aFile.toPath());

        } catch (IOException e) {
            System.out.println("IOException [04] : Failed to Merge Audio & Video Streams");
            e.printStackTrace();
        }

    }

    private static class InvalidUrlException extends Throwable {

        InvalidUrlException(String exceptionMsg) {
            super("Invalid Youtube URL Found : " + exceptionMsg);
        }
    }
}
