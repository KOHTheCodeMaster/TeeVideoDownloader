package libs.koh_youtube_dl.youtubedl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class YoutubeDLRequest {
    private String directory;
    private String url;
    private Map<String, String> options;

    YoutubeDLRequest() {
    }

    YoutubeDLRequest(String url) {
        this.url = url;
    }

    public YoutubeDLRequest(String url, String directory) {
        this.url = url;
        this.directory = directory;
        options = new HashMap<>();
    }

    String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    Map<String, String> getOption() {
        return this.options;
    }

    public void setOption(String key) {
        this.options.put(key, null);
    }

    public void setOption(String key, String value) {
        this.options.put(key, value);
    }

    public void setOption(String key, int value) {
        this.options.put(key, String.valueOf(value));
    }

    String buildOptions() {

        StringBuilder builder = new StringBuilder();
        if (this.url != null) builder.append(this.url).append(" ");

        Iterator<Entry<String, String>> iterator = this.options.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, String> option = iterator.next();
            String name = option.getKey();
            String value = option.getValue();

            if (value == null) value = "";
            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted).append(" ");
            iterator.remove();
        }

        System.out.println("buildOptions: " + builder);
        return builder.toString().trim();
    }
}
