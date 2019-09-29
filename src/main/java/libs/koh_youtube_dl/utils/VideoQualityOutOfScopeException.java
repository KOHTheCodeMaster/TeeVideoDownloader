package libs.koh_youtube_dl.utils;

public class VideoQualityOutOfScopeException extends Exception {

    private String message;
    private VideoQuality videoQuality;

    VideoQualityOutOfScopeException(String message) {
        this.message = message;
    }

    VideoQualityOutOfScopeException(String message, VideoQuality videoQuality) {
        super(message);
        this.message = message;
        this.videoQuality = videoQuality;
    }

    public String getMessage() {
        return message;
    }

    public VideoQuality getVideoQuality() {
        return videoQuality;
    }
}
