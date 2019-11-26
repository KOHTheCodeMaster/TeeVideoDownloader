package libs.koh_youtube_dl.youtubedl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.koh.stdlib.utils.KOHFilesUtil;
import libs.koh_youtube_dl.mapper.VideoFormat;
import libs.koh_youtube_dl.mapper.VideoInfo;
import libs.koh_youtube_dl.mapper.VideoThumbnail;
import libs.koh_youtube_dl.utils.StreamGobbler;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YoutubeDL {

    private static String executablePath = "youtube-dl";

    public YoutubeDL() {
    }

    private static String buildCommand(String command) {
        return String.format("%s %s", executablePath, command);
    }

    public static YoutubeDLResponse execute(YoutubeDLRequest request) throws YoutubeDLException {

        StringBuffer outBuffer = new StringBuffer();
        StringBuffer errBuffer = new StringBuffer();

        String command = buildCommand(request.buildOptions());
        String directory = request.getDirectory();
        Map<String, String> options = request.getOption();
        long startTime = System.nanoTime();
        String[] split = command.split(" ");

        ProcessBuilder processBuilder = new ProcessBuilder(split);
        if (directory != null && new File(directory).isDirectory()) {
            processBuilder.directory(new File(directory));
        }

/*
        System.out.println("\nCommand: " + command);
        System.out.println("\nSplit: ");
        for (String s : split)
            System.out.print(s + " | ");
*/

        Process process;
        try {
            process = processBuilder.start();
            System.out.println("\nprocessBuilder started...");
        } catch (IOException var19) {
            throw new YoutubeDLException(var19);
        }

        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

/*
        try {
            List<String> result = readOutput(outStream);
            System.out.println(" |== Result ==|");
            result.forEach(s -> System.out.println());

        } catch (IOException e) {
            e.printStackTrace();
        }
*/

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
            return new YoutubeDLResponse(command, options, directory, exitCode, elapsedTime, out, err);
        }
    }

    public static String getVersion() throws YoutubeDLException {
        YoutubeDLRequest request = new YoutubeDLRequest();
        request.setOption("version");
        return execute(request).getOut();
    }

    public static void saveVideoInfoListToJsonFile(String url, File jsonFile) throws YoutubeDLException {

        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("dump-json");

        String strJsonArray = processJson(execute(request).getOut());
        KOHFilesUtil.writeStrToFile(strJsonArray, jsonFile);

    }

    public static List<VideoInfo> getVideoInfoList(String url) throws YoutubeDLException {

        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("dump-json");

        YoutubeDLResponse response = execute(request);
        List<VideoInfo> videoInfoList;
        try {

            String strJsonArray = processJson(response.getOut());
            videoInfoList = Arrays.asList(new ObjectMapper().readValue(strJsonArray, VideoInfo[].class));
            return videoInfoList;

        } catch (IOException var6) {
            throw new YoutubeDLException("Unable to parse video information: " + var6.getMessage());
        }
    }

    private static String processJson(String jsonUnprocessed) {

        /*
         *  Time Stamp : 25th September 2K19, 10:16 PM..!!
         *  jsonUnprocessed => Array of Json String with missing pair of [ ]
         *  & ',' after every element (ending with new line '\n')
         */

        return "[" +
                jsonUnprocessed.substring(0, jsonUnprocessed.lastIndexOf('\n'))
                        .replaceAll("\\n", ",\n")
                + "]";

    }

    public static List<VideoFormat> getFormats(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfoList(url).get(0);
        return info.formats;
    }

    public static List<VideoThumbnail> getThumbnails(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfoList(url).get(0);
        return info.thumbnails;
    }

    public static List<String> getCategories(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfoList(url).get(0);
        return info.categories;
    }

    public static List<String> getTags(String url) throws YoutubeDLException {
        VideoInfo info = getVideoInfoList(url).get(0);
        return info.tags;
    }

    public static String getExecutablePath() {
        return executablePath;
    }

    public static void setExecutablePath(String path) {
        executablePath = path;
    }

    private static List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }
}
