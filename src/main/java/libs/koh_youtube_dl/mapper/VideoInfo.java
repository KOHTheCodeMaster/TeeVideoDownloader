package libs.koh_youtube_dl.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class VideoInfo {
    public String id;
    public String fulltitle;
    public String title;
    @JsonProperty("upload_date")
    public String uploadDate;
    @JsonProperty("display_id")
    public String displayId;
    public int duration;
    public String description;
    public String thumbnail;
    public String license;
    @JsonProperty("uploader_id")
    public String uploaderId;
    public String uploader;
    @JsonProperty("player_url")
    public String playerUrl;
    @JsonProperty("webpage_url")
    public String webpageUrl;
    @JsonProperty("webpage_url_basename")
    public String webpageUrlBasename;
    public String resolution;
    public int width;
    public int height;
    public String format;
    public String ext;
    @JsonProperty("http_headers")
    public HttpHeader httpHeader;
    public ArrayList<String> categories;
    public ArrayList<String> tags;
    public ArrayList<VideoFormat> formats;
    public ArrayList<VideoThumbnail> thumbnails;

    public VideoInfo() {
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", fulltitle='" + fulltitle + '\'' +
                ", title='" + title + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                ", displayId='" + displayId + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", license='" + license + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", uploader='" + uploader + '\'' +
                ", playerUrl='" + playerUrl + '\'' +
                ", webpageUrl='" + webpageUrl + '\'' +
                ", webpageUrlBasename='" + webpageUrlBasename + '\'' +
                ", resolution='" + resolution + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", format='" + format + '\'' +
                ", ext='" + ext + '\'' +
                ", httpHeader=" + httpHeader +
                ", categories=" + categories +
                ", tags=" + tags +
                ", formats=" + formats +
                ", thumbnails=" + thumbnails +
                '}';
    }
}
