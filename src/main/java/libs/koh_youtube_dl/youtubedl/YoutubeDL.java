package libs.koh_youtube_dl.youtubedl;

import com.fasterxml.jackson.databind.ObjectMapper;
import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.mapper.VideoThumbnail;
import libs.koh_youtube_dl.utils.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class YoutubeDL {
    protected static String executablePath = "youtube-dl";

    public YoutubeDL() {
    }

    protected static String buildCommand(String command) {
        return String.format("%s %s", executablePath, command);
    }

    public static YoutubeDLResponse execute(YoutubeDLRequest request) throws YoutubeDLException {
        String command = buildCommand(request.buildOptions());
        String directory = request.getDirectory();
        Map<String, String> options = request.getOption();
        StringBuffer outBuffer = new StringBuffer();
        StringBuffer errBuffer = new StringBuffer();
        long startTime = System.nanoTime();
        String[] split = command.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(split);
        if (directory != null) {
            processBuilder.directory(new File(directory));
        }

        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException var19) {
            throw new YoutubeDLException(var19);
        }

        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();
        new StreamGobbler(outBuffer, outStream);
        new StreamGobbler(errBuffer, errStream);

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException var18) {
            throw new YoutubeDLException(var18);
        }

        String out = outBuffer.toString();
        String err = errBuffer.toString();
        if (exitCode > 0) {
            throw new YoutubeDLException(err);
        } else {
            int elapsedTime = (int) ((System.nanoTime() - startTime) / 1000000L);
            YoutubeDLResponse youtubeDLResponse = new YoutubeDLResponse(command, options, directory, exitCode, elapsedTime, out, err);
            return youtubeDLResponse;
        }
    }

    public static String getVersion() throws YoutubeDLException {
        YoutubeDLRequest request = new YoutubeDLRequest();
        request.setOption("version");
        return execute(request).getOut();
    }

    public static VideoInfo getVideoInfo(String url) throws YoutubeDLException {
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("dump-json");
        request.setOption("no-playlist");
        YoutubeDLResponse response = execute(request);
        ObjectMapper objectMapper = new ObjectMapper();
        VideoInfo videoInfo = null;

        try {
            videoInfo = (VideoInfo) objectMapper.readValue(response.getOut(), VideoInfo.class);
            return videoInfo;
        } catch (IOException var6) {
            throw new YoutubeDLException("Unable to parse video information: " + var6.getMessage());
        }
    }

    public static List<VideoFormat> getFormats(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfo(url);
        return info.formats;
    }

    public static List<VideoThumbnail> getThumbnails(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfo(url);
        return info.thumbnails;
    }

    public static List<String> getCategories(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfo(url);
        return info.categories;
    }

    public static List<String> getTags(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfo(url);
        return info.tags;
    }

    public static String getExecutablePath() {
        return executablePath;
    }

    public static void setExecutablePath(String path) {
        executablePath = path;
    }
}
