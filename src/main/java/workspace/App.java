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
        String url = "https://www.youtube.com/playlist?list=PLAwxTw4SYaPl8TaY7QKWfLc16FZOsXHuP"; //  14 Udacity Vid PL
//        String url = "https://www.linkedin.com/learning/learning-sql-server-2017"; //  4 Vid PL
//        String url = "https://www.youtube.com/playlist?list=PLoJSah60cTP57mDXOO46BZ18RJNap0yvK"; //  3 Vid PL
//        String url = "https://www.voot.com/shows/bigg-boss-s13/13/839463/sanskaari-playboy-vs-the-bb-house/854477"; //  BB13
//        String url = "https://www.youtube.com/watch?v=fPrixQcSPyM"; //  Avengers
//        String url = "https://www.voot.com/shows/bigg-boss-s13/13/839463/bigg-boss-13-season-premiere/850572"; //  BB13
//        String url = "https://www.youtube.com/watch?v=47ovZhiddoA"; //  1280x534
//        String url = "https://www.youtube.com/watch?v=HyNW_4w9IQ0"; //  360p

        File file = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\b\\app");
        TeeVideoDownloader obj = new TeeVideoDownloader(url, VideoQuality.Q_144P, file);
        obj.start1();

    }

}

/*
 *  Date Created: 25th April 2K19, 09:48 AM..!!
 *  Time Stamp: 14th October 2K19, 06:16 PM..!!
 *
 *  Project Name : Tee Video Downloader [TVD]
 *
 *  Status: Unstable - Connection Timeout
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