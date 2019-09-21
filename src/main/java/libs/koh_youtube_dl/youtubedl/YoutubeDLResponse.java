package libs.koh_youtube_dl.youtubedl;

import java.util.Map;

public class YoutubeDLResponse {
    private Map<String, String> options;
    private String command;
    private int exitCode;
    private String out;
    private String err;
    private String directory;
    private int elapsedTime;

    YoutubeDLResponse(String command, Map<String, String> options, String directory, int exitCode, int elapsedTime, String out, String err) {
        this.command = command;
        this.options = options;
        this.directory = directory;
        this.elapsedTime = elapsedTime;
        this.exitCode = exitCode;
        this.out = out;
        this.err = err;
    }

    public String getCommand() {
        return this.command;
    }

    public int getExitCode() {
        return this.exitCode;
    }

    public String getOut() {
        return this.out;
    }

    public String getErr() {
        return this.err;
    }

    public Map<String, String> getOptions() {
        return this.options;
    }

    public String getDirectory() {
        return this.directory;
    }

    public int getElapsedTime() {
        return this.elapsedTime;
    }

    @Override
    public String toString() {
        return "YoutubeDLResponse{" +
                "options=" + options + "\n" +
                "command='" + command + '\'' + "\n" +
                "exitCode=" + exitCode + "\n" +
                "out='" + out + '\'' + "\n" +
                "err='" + err + '\'' + "\n" +
                "directory='" + directory + '\'' + "\n" +
                "elapsedTime=" + elapsedTime + "\n" +
                '}';
    }
}
