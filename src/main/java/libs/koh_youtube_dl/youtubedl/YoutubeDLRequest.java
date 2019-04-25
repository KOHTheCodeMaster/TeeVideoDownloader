package libs.koh_youtube_dl.youtubedl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class YoutubeDLRequest {
    private String directory;
    private String url;
    private Map<String, String> options = new HashMap();

    public YoutubeDLRequest() {
    }

    public YoutubeDLRequest(String url) {
        this.url = url;
    }

    public YoutubeDLRequest(String url, String directory) {
        this.url = url;
        this.directory = directory;
    }

    public String getDirectory() {
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

    public Map<String, String> getOption() {
        return this.options;
    }

    public void setOption(String key) {
        this.options.put(key, (String) null);
    }

    public void setOption(String key, String value) {
        this.options.put(key, value);
    }

    public void setOption(String key, int value) {
        this.options.put(key, String.valueOf(value));
    }

    protected String buildOptions() {
        StringBuilder builder = new StringBuilder();
        if (this.url != null) {
            builder.append(this.url + " ");
        }

        Iterator it = this.options.entrySet().iterator();

        while (it.hasNext()) {
            Entry option = (Entry) it.next();
            String name = (String) option.getKey();
            String value = (String) option.getValue();
            if (value == null) {
                value = "";
            }

            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted + " ");
            it.remove();
        }

        return builder.toString().trim();
    }
}
