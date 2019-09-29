package workspace;

import libs.koh_youtube_dl.utils.VideoQuality;

import java.io.File;

public class App {

    public static void main(String[] args) {

//        String url = "https://www.youtube.com/watch?v=CXjjGE3k5PY"; //  16 sec
//        String url = "https://www.youtube.com/playlist?list=PLoJSah60cTP57mDXOO46BZ18RJNap0yvK"; //  3 Vid PL
        String url = "https://www.youtube.com/watch?v=fPrixQcSPyM"; //  Avengers
//        String url = "https://www.youtube.com/watch?v=47ovZhiddoA"; //  1280x534
//        String url = "https://www.youtube.com/watch?v=HyNW_4w9IQ0"; //  360p

        File file = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2");
        TeeVideoDownloader obj = new TeeVideoDownloader(url, VideoQuality.Q_720P, file);
        obj.start1();

    }

}

/*
 *  Date Created: 25th April 2K19, 09:48 AM..!!
 *  Time Stamp: 29th September 2K19, 09:25 PM..!!
 *
 *  Project Name : Tee Video Downloader [TVD]
 *
 *  Status: Work In Progress
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