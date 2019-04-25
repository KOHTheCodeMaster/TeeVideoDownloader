package workspace;

import libs.koh_youtube_dl.youtubedl.YoutubeDL;
import libs.koh_youtube_dl.youtubedl.YoutubeDLException;
import libs.koh_youtube_dl.youtubedl.YoutubeDLRequest;
import libs.koh_youtube_dl.youtubedl.YoutubeDLResponse;

public class Original {

    public static void main(String[] args) {

        System.out.println("Begin.");

// Video url to download
        String videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

// Destination directory
        String directory = ".";

// Build request
        YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, directory);
        request.setOption("ignore-errors");        // --ignore-errors
        request.setOption("output", "%(id)s");    // --output "%(id)s"
        request.setOption("retries", 10);        // --retries 10

// Make request and return response
        YoutubeDLResponse response = null;
        try {
            response = YoutubeDL.execute(request);
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }

// Response
        String stdOut = response.getOut(); // Executable output

        System.out.println(stdOut);

        System.out.println("End.");

    }

}
