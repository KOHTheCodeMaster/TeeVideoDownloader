package workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.koh.stdlib.utils.KOHStringUtil;
import dev.koh.stdlib.utils.MyTimer;
import dev.koh.stdlib.utils.enums.StringOptions;
import exceptions.ProcessIncompleteException;
import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.utils.*;
import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private File downloadPlaylistDir;
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
        this.tempDirPath = defaultDownloadDir + "/.Temp";
        this.mustKeepMP4 = mustKeepMP4;
        this.EXTENSION = mustKeepMP4 ? "mp4" : "mkv";
        this.myTimer = new MyTimer();
    }

    /**
     * Begin TVD.
     */
    void begin() {

        System.out.println("Begin.");
        myTimer.startTimer();

        init();
        parseUrl(mainUrl);
        cleanTempDirs();

        myTimer.stopTimer(true);
        System.out.println("End.");

    }

    private void cleanTempDirs() {

        //  Create Sub-Dirs.
        try {

            if (Files.deleteIfExists(Paths.get(tempDirPath, "Output")) &&
                    Files.deleteIfExists(Paths.get(tempDirPath, "Temp-Parts")) &&
                    Files.deleteIfExists(Paths.get(tempDirPath, "Failed-To-Merge")) &&
                    Files.deleteIfExists(Paths.get(tempDirPath, "Audio-Streams")) &&
                    Files.deleteIfExists(Paths.get(tempDirPath, "Video-Streams")) &&
                    Files.deleteIfExists(Paths.get(tempDirPath))) {

                System.out.println("\nCleaned Temporary Dirs. Successfully.\n");
            } else System.out.println("Failed to Clean Temp Dirs...");

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        //  Create Sub-Dirs.
        if (new File(tempDirPath, "Output").mkdirs() &&
//                new File(tempDirPath, "Temp-Parts").mkdirs() &&
                new File(tempDirPath, "Failed-To-Merge").mkdirs() &&
                new File(tempDirPath, "Audio-Streams").mkdirs() &&
                new File(tempDirPath, "Video-Streams").mkdirs()) {
            System.out.println("Sub Dirs. Created Successfully.");
        } else {
            System.out.println("Failedd to Create Temp Dirs. to store temporary files");
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
            String promptSrcDir = "Enter Src Dir.: ";
            //  TODO:   Create the Dirs. on-fly if doesn't exists but ensure that the given path is a Valid Dir. Path
            defaultDownloadDir = new File(KOHStringUtil.userInputString(promptSrcDir, StringOptions.NOWHITESPACE, myTimer));
            tempDirPath = defaultDownloadDir + "/.Temp";

            String promptKeepMP4 = "Download in MP4 Format Only? [Y/N] : ";
            mustKeepMP4 = KOHStringUtil.userInputString(promptKeepMP4,
                    StringOptions.YES_OR_NO, myTimer).charAt(0) == 'y';
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

                //  Initialize downloadPlaylistDir to defaultDownloadDir/fullyQualifiedPlaylistName
                this.downloadPlaylistDir = new File(defaultDownloadDir, fullyQualifiedPlaylistName);

                //  Create downloadPlaylistDir
                if (downloadPlaylistDir.mkdirs()) System.out.println("Subfolder Created Successfully : "
                        + downloadPlaylistDir.getAbsolutePath());
                else System.out.println("ERROR : Failed to create Subfolder : "
                        + downloadPlaylistDir.getAbsolutePath());

                //  Save VideoInfoList as Json file in directory : defaultDownloadDir/downloadPlaylistDir/JSON/*.json
                File jsonDir = new File(downloadPlaylistDir, "JSON");

                if (!jsonDir.exists() && !jsonDir.mkdirs())
                    System.out.println("Unable to create JSON Directory...");

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

            /*
                Accept only if Acodec exists AND Vcodec doesn't exists
                i.e. when downloading Audio Streams, it allows only those formats that contains only Audio Streams
                & not the ones with Audio & Video streams both.
            */
            return !vf.acodec.contains("none") && vf.vcodec.contains("none");

        };

//        Predicate<VideoFormat> filterOffVP9VCodec = vf -> !vf.vcodec.toLowerCase().contains("vp9");

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

            String videoStreamDirPath = tempDirPath + "/Video-Streams";
            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
            vFile = new File(videoStreamDirPath, fName);
            downloadManager.downloadOffUrl(vf.url, vFile);
        };

        Consumer<VideoFormat> consumerDownloadAudioStream = vf -> {

            //  Directly Copy Audio Stream if doesn't intends to follow mustKeepMP4
            //  Avoid explicit Audio-codec conversion by FFMPEG when mustKeepMP4 is False
            if (!mustKeepMP4 || vf.ext.equals("m4a") || vf.ext.equals("mp4")) shouldDirectlyCopyAudioStreams.set(true);

            String audioStreamDirPath = tempDirPath + "/Audio-Streams";
            String fName = title + "." + vf.ext;
            fName = fName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");

            aFile = new File(audioStreamDirPath, fName);
            downloadManager.downloadOffUrl(vf.url, aFile);

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

        //  Initialize the targetFileName with serialNumPrefix
        int dotIndex = vFile.getName().lastIndexOf('.');
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
        }

        targetFileName += vFile.getName().substring(0, dotIndex) + "." + EXTENSION;
        targetFileName = targetFileName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
        File targetFile = new File(tempDirPath + "/Output", targetFileName);

        //  Merge Video & Audio Streams (files) together using FFMPEG
        mergeVideoAndAudioStreams(targetFile, aFile, vFile, shouldDirectlyCopyAudioStreams.get(), shouldDirectlyCopyVideoStream.get());

        serialNumPrefix++;
        System.out.println("END..!!" + "\n\n\n===================================\n\n\n");

    }

    private void mergeVideoAndAudioStreams(File targetFile, File aFile, File vFile, boolean shouldDirectlyCopyAudioStream, boolean shouldDirectlyCopyVideoStream) {

        FFMPEGWrapper ffmpegWrapper = new FFMPEGWrapper(targetFile, aFile, vFile, shouldDirectlyCopyAudioStream, shouldDirectlyCopyVideoStream, this.EXTENSION);

        try {

            ffmpegWrapper.startMerging();

            //  Finally, Move the Stable Output targetFile from .Temp/Output to defaultDownloadDir/PLDir
            Files.move(targetFile.toPath(), Paths.get(downloadPlaylistDir.getAbsolutePath(), targetFile.getName()));
            deleteIndividualStreamFiles(aFile, vFile);
            System.out.println("Audio & Video Streams Merged Successfully..!!\n");

        } catch (ProcessIncompleteException e) {

            System.out.println("ProcessIncompleteException [01] : Failed to Complete startMerging Process");
            System.out.println("Unable to Merge Audio & Video Streams...");
            System.out.println("Exception Message : " + e.getMessage());
            e.printStackTrace();
            handleStreamsMergeFailed(aFile, vFile);

        } catch (IOException e) {
            System.out.println("IOException [01] : Failed to Merge Audio & Video Streams");
            e.printStackTrace();
        }

    }

    private void handleStreamsMergeFailed(File aFile, File vFile) {

        try {
            Files.move(aFile.toPath(), Paths.get(tempDirPath, "Failed-To-Merge", serialNumPrefix + "", "Audio-Streams", aFile.getName()));
            Files.move(vFile.toPath(), Paths.get(tempDirPath, "Failed-To-Merge", serialNumPrefix + "", "Video-Streams", vFile.getName()));
        } catch (IOException e) {
            System.out.println("IOException [03] - Unable to Move Audio & Video Stream Files - aFile & vFile");
            e.printStackTrace();
        }

    }

    private void deleteIndividualStreamFiles(File aFile, File vFile) {

        try {

            Files.delete(aFile.toPath());
            Files.delete(vFile.toPath());
        } catch (IOException e) {
            System.out.println("IOException [02] - Unable to Delete Temporary Files - aFile & vFile");
            e.printStackTrace();
        }

    }

    private static class InvalidUrlException extends Throwable {

        InvalidUrlException(String exceptionMsg) {
            super("Invalid Youtube URL Found : " + exceptionMsg);
        }
    }
}
