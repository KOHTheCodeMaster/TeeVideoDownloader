package libs.koh_youtube_dl.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class VideoFormat {
    public int asr;
    public int tbr;
    public int abr;
    public String format;
    @JsonProperty("format_id")
    public String formatId;
    @JsonProperty("format_note")
    public String formatNote;
    public String ext;
    public int preference;
    public String vcodec;
    public String acodec;
    public int width;
    public int height;
    public long filesize;
    public int fps;
    public String url;

    public VideoFormat() {
    }

    @Override
    public String toString() {
        return "VideoFormat{\n" +
                " asr : " + asr +
                "\n tbr : " + tbr +
                "\n abr : " + abr +
                "\n format : '" + format + '\'' +
                "\n formatId : '" + formatId + '\'' +
                "\n formatNote : '" + formatNote + '\'' +
                "\n ext : '" + ext + '\'' +
                "\n preference : " + preference +
                "\n vcodec : '" + vcodec + '\'' +
                "\n acodec : '" + acodec + '\'' +
                "\n width : " + width +
                "\n height : " + height +
                "\n filesize : " + filesize +
                "\n fps : " + fps +
                "\n url : '" + url + '\'' +
                "}\n\n";
    }
}
