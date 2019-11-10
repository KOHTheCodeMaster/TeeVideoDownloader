package workspace;

import libs.koh_youtube_dl.utils.VideoQuality;

import java.io.File;

public class App {

    public static void main(String[] args) {
// https://r2---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1571074080&ei=v1ukXc_pLcaw1Abm-7HgBA&ip=103.137.85.1&id=o-AIDVPt9KKxW5B4vv_mO7Bp65RQJiEdTVrOe6iG8eOKOV&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=1&pl=24&initcwndbps=455000&mime=audio%2Fmp4&gir=yes&clen=3089481&dur=190.844&lmt=1563408377351714&mt=1571052344&fvip=4&keepalive=yes&fexp=23842630&c=WEB&txp=2311222&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&sig=ALgxI2wwRQIhAIh_Sp6ardlz3-ASPiCbw3CjS8tg2tjeM9prz1EIvTm-AiAkRdlVZ-wXo0VziKkNECIXwU_Ubvvca699IezAr7aZbw%3D%3D&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIhAPfVvOVKOKo82U50UcVm3F286ZoLr0sTMApk37bRqSs7AiBRkONBVKMjWsnrPhgPPUYwBqnj38emjud-DY23Jhh2EQ%3D%3D&ratebypass=yes
//        String url = "https://www.youtube.com/watch?v=CXjjGE3k5PY"; //  16 sec
//        String url = "https://www.youtube.com/playlist?list=PL6gx4Cwl9DGCSvv2N_6jhnEOZnq_419XJ"; //  4 Vid PL
//        String url = "https://www.youtube.com/playlist?list=PLAwxTw4SYaPnMwH5-FNkErnnq_aSy706S"; //  416 Udacity Vid PL
//        String url = "https://www.youtube.com/watch?v=SgcCqnYt5O8"; //  416 Udacity Vid PL
//        String url = "https://www.youtube.com/playlist?list=PLAwxTw4SYaPl8TaY7QKWfLc16FZOsXHuP"; //  8 Udacity Vid PL
//        String url = "https://www.linkedin.com/learning/learning-sql-server-2017"; //  4 Vid PL
//        String url = "https://www.voot.com/shows/bigg-boss-s13/13/839463/sanskaari-playboy-vs-the-bb-house/854477"; //  BB13
//        String url = "https://www.youtube.com/playlist?list=PLrnPJCHvNZuA80lNWNCLICR3qYzhw3iPI"; //  Constraint PL
//        String url = "https://www.youtube.com/watch?v=Ske1_LuymRI"; //  Constraint
//        String url = "https://www.youtube.com/playlist?list=PLoJSah60cTP57mDXOO46BZ18RJNap0yvK"; //  3 Vid PL
//        String url = "https://www.youtube.com/watch?v=FsParg61xGw"; //  3rd Vid  of PL  [Slow URL]
//        String url = "https://www.youtube.com/watch?v=fPrixQcSPyM"; //  Avengers
//        String url = "https://www.voot.com/shows/bigg-boss-s13/13/839463/bigg-boss-13-season-premiere/850572"; //  BB13
//        String url = "https://www.youtube.com/watch?v=47ovZhiddoA"; //  1280x534
        String url = "https://www.youtube.com/watch?v=HyNW_4w9IQ0"; //  360p

        File file = new File("F:\\UNSORTED\\b\\00");
        TeeVideoDownloader obj = new TeeVideoDownloader(null, VideoQuality.Q_4K, file, true);
//        TeeVideoDownloader obj = new TeeVideoDownloader();
        obj.start1();

    }

}

/*
 *  Date Created: 25th April 2K19, 09:48 AM..!!
 *  Time Stamp: 11th November 2K19, 00:22 AM..!!
 *
 *  Project Name : Tee Video Downloader [TVD]
 *
 *  Status: Stable - Accurate Extensions Conversion
 *
 *  8th Commit - [TVD-Test-02.jar]
 *  1. Updated DownloadManager with support to download files which do not support downloading in chunks
 *  2. Using DownloaderThread class to handle each downloading thread separately and maintain modularity
 *  3. Downloading Best Available Streams for both Audio & Video and later converting according to the desired EXTENSION
 *  4. Optimized FFMPEGWrapper by Avoiding redundant conversion of Audio & Video streams by handling the extensions accordingly
 *  5. Additional static method FFMPEGWrapper.extractAudioFileOffVideo to extract Audio File from the particular file
 *     with both audio & video streams i.e. generally  a-codec : "mp4a" with extension : ".mp4"
 *  6. Added option to select if want to directly Copy Streams [mkv] or convert incompatible extensions like webm mp4
 *
 *  Bugs To Fix:
 *  1. Unresolved SSL Exception due to Timeout when Downloading for a long time by single thread
 *  2. Uncertainly slow downloading speed at specific urls including "https://www.youtube.com/watch?v=FsParg61xGw"
 *  3. Handle the scenario when File with the same name already exists in srcDir or tempDirPath
 *
 *  7th Commit - [TVD-Test-01.jar]
 *  1. Stabilized App with default constructor to let user choose URL, SrcDir & Quality.
 *  2. Testing JAR File - 'TVD-Test-01.jar' released.
 *  3. AsyncHTTP client experimented in A1.
 *
 *  6th Commit - [Added Serial Numbers]
 *  1. Added Serial Numbers to the Downloaded Files in case of Playlist.
 *
 *  Bugs Found : Due to Synchronous Http Request Handling,
 *               there's a chance of Connection Timeout Exception to occur.
 *
 *  5th Commit - [Stable Playlist Download]
 *  1. Downloading Video as well as Audio Files in parts then combined into Single Video/Audio File.
 *  2. Deleted Temporary file parts after Merging the Video & Audio Files.
 *
 *  4th Commit - [Downloading In Parts & finally Merged into 1]
 *  1. Unclean | Messed-Up code for test purposes
 *  2. Tests have been implemented in A1.java file
 *
 *  3rd Commit - [Merging Audio & Video Streams using FFMPEG]
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