package libs.koh_youtube_dl.youtubedl;

public class YoutubeDLException extends Exception {
    private String message;

    YoutubeDLException(String message) {
        this.message = message;
    }

    YoutubeDLException(Exception e) {
        this.message = e.getMessage();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
