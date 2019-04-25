package libs.koh_youtube_dl.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class VideoThumbnail {
    public String url;
    public String id;

    public VideoThumbnail() {
    }
}
