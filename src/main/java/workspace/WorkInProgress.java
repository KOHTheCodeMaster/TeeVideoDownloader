package workspace;

import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;
import libs.koh_youtube_dl.youtubedl.YoutubeDLRequest;
import libs.koh_youtube_dl.youtubedl.YoutubeDLResponse;

import java.io.File;

public class WorkInProgress {

    private String videoUrl;
    private String destinationDirectory;

    public static void main(String[] args) {

        System.out.println("Begin.");

        WorkInProgress workInProgress = new WorkInProgress();
        workInProgress.major();

        System.out.println("End.");

    }

    private void major() {

        //  Initialize videoUrl & destinationDirectory
        init();

        //  Build the request
        YoutubeDLRequest request = buildRequest();

        //  Acquire the response after processing the request
        YoutubeDLResponse response = makeRequestForResponse(request);

        //  Extract the execution output from the response
        String execOutput = response.getOut();

        System.out.println(execOutput);

    }

    private YoutubeDLRequest buildRequest() {

        YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, destinationDirectory);
        request.setOption("ignore-errors");        // --ignore-errors
        request.setOption("output", "%(id)s");    // --output "%(id)s"
        request.setOption("retries", 10);        // --retries 10

        return request;

    }

    private YoutubeDLResponse makeRequestForResponse(YoutubeDLRequest request) {

        YoutubeDLResponse response = null;
        try {
            response = YoutubeDL.execute(request);
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }

        return response;

    }

    private void init() {

        //  Initialize videoUrl
//        this.videoUrl = "https://www.youtube.com/watch?v=HZ7PAyCDwEg";
        this.videoUrl = "https://www.youtube.com/watch?v=2Vv-BfVoq4g";

        //  Initialize destinationDirectory
        initializeDestinationDirectory();
    }

    private void initializeDestinationDirectory() {

        String defaultDestinationDirectory = "./res/downloaded/2";
        //  Check if defaultDestinationDirectory exists & is a dir.
        destinationDirectory = (new File(defaultDestinationDirectory).isDirectory())
                //  update destinationDirectory with defaultDestinationDirectory.
                ? defaultDestinationDirectory
                //  create necessary parent dirs if not exists.
                : (new File(defaultDestinationDirectory).mkdirs())
                //  update destinationDirectory with defaultDestinationDirectory.
                ? defaultDestinationDirectory
                //  when failed to create defaultDestinationDirectory dirs.
                //  update destinationDirectory with project root dir.
                : "./";
    }

}

/*
 *  Time Stamp: 25th April 2K19, 09:24 AM..!!
 *
 *  Project Name : Tee Video Downloader [TVD]
 *
 *  Status: Work In Progress
 *
 *  Warning:
 *  This project is solely for educational purpose.
 *  It doesn't supports any piracy or copyright violations.
 *
 *  Code Developed By,
 *  ~K.O.H..!! ^__^
 */
