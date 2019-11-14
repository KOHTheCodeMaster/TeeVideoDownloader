package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FFMPEGWrapper {

    private static final String executablePath = "ffmpeg";

    private File targetFile;
    private File audioStreamFile;
    private File videoStreamFile;
    private final String ext;
    private boolean shouldDirectlyCopyAudioStreams;
    private boolean shouldDirectlyCopyVideoStreams;

    public FFMPEGWrapper(File targetFile, File audioStreamFile, File videoStreamFile) {
        this(targetFile, audioStreamFile, videoStreamFile, true, true, "mp4");
    }

    public FFMPEGWrapper(File targetFile, File audioStreamFile, File videoStreamFile, boolean shouldDirectlyCopyAudioStreams, boolean shouldDirectlyCopyVideoStreams) {
        this(targetFile, audioStreamFile, videoStreamFile, shouldDirectlyCopyAudioStreams, shouldDirectlyCopyVideoStreams, "mp4");
    }

    public FFMPEGWrapper(File targetFile, File audioStreamFile, File videoStreamFile, boolean shouldDirectlyCopyAudioStreams, boolean shouldDirectlyCopyVideoStreams, String ext) {
        this.targetFile = targetFile;
        this.audioStreamFile = audioStreamFile;
        this.videoStreamFile = videoStreamFile;
        this.shouldDirectlyCopyAudioStreams = shouldDirectlyCopyAudioStreams;
        this.shouldDirectlyCopyVideoStreams = shouldDirectlyCopyVideoStreams;
        this.ext = ext;
    }

    public static File extractAudioFileOffVideo(File srcFile, String targetExt) throws IOException {

        long startTime = System.nanoTime();

        //  If srcFile has same extension as targetExt, return null
        int dotIndex = srcFile.getAbsolutePath().lastIndexOf('.');
        if (srcFile.getAbsolutePath().substring(dotIndex + 1).equals(targetExt))
            return null;

        System.out.println("\nExtracting Audio File!");
        System.out.println("====================================\n");
//        System.out.println("dotIndex : " + dotIndex);
//        System.out.println("vsf : " + srcFile.getAbsolutePath());

        String[] commandArr = new String[]{
                executablePath,
                "-i",
                srcFile.getAbsolutePath(),
                "-vn",
                "-c:a",
                "copy",
//                srcFile.getAbsolutePath(),
                srcFile.getAbsolutePath().substring(0, dotIndex + 1) + targetExt
        };

        System.out.println("Command: ");
        Arrays.stream(commandArr).forEach(System.out::println);

        //  inheritIO() method ensures that the process
        //  Output is redirected to the Java Program Console at realtime
        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder(commandArr);//.inheritIO();

//        if (targetFile != null && targetFile.isDirectory())
//            processBuilder.directory(targetFile);

        StringBuffer outBuffer = new StringBuffer();
        StringBuffer errBuffer = new StringBuffer();

        System.out.println("\nMerging Process Initialized!");
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert process != null;
        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

//        new StreamGobbler(outBuffer, outStream);
        new StreamGobbler(outBuffer, outStream, true);
        new StreamGobbler(errBuffer, errStream, true);

        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException var18) {
            var18.printStackTrace();
        }

        String out = outBuffer.toString();
        String err = errBuffer.toString();
        if (exitCode > 0) {
            throw new IOException(err);
        } else {
            System.out.println("Merging Successful..!!");
            System.out.println("Out Buffer : " + out);
            System.out.println("Err Buffer : " + err);
            long processTime = ((System.nanoTime() - startTime) / (long) 1E9);
            System.out.println("Process Time : " + processTime + " seconds.");
        }

        System.out.println("\nAudio File Extracted Successfully");
        System.out.println("====================================\n");

        File targetFile = new File(srcFile.getAbsolutePath().substring(0, dotIndex + 1) + targetExt);
        if (targetFile.isFile())
            return targetFile;
        return null;

    }

    public void startMerging() throws IOException {

        long startTime = System.nanoTime();
        String[] commandArr = buildCommand();

        //  inheritIO() method ensures that the process
        //  Output is redirected to the Java Program Console at realtime
        Process process;
        ProcessBuilder processBuilder = new ProcessBuilder(commandArr);//.inheritIO();

        StringBuffer outBuffer = new StringBuffer();
        StringBuffer errBuffer = new StringBuffer();

        System.out.println("\nMerging Process Initialized!");
        process = processBuilder.start();

        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

//        new StreamGobbler(outBuffer, outStream);
        new StreamGobbler(outBuffer, outStream, true);
        new StreamGobbler(errBuffer, errStream, true);

        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException var18) {
            var18.printStackTrace();
        }

        String out = outBuffer.toString();
        String err = errBuffer.toString();
        if (exitCode > 0) {
            throw new IOException(err);
        } else {
            System.out.println("Merging Successful..!!");
            System.out.println("Out Buffer : " + out);
            System.out.println("Err Buffer : " + err);
            long processTime = ((System.nanoTime() - startTime) / (long) 1E9);
            System.out.println("Process Time : " + processTime + " seconds.");
        }

    }

    private String[] buildCommand() {

        //  Time Stamp : 10th November 2K19, 08:24 PM..!!

        String[] commandArr;
        int dotIndex = targetFile.getAbsolutePath().lastIndexOf('.') + 1;

        if (shouldDirectlyCopyAudioStreams && shouldDirectlyCopyVideoStreams) {

            commandArr = new String[]{
                    executablePath,
                    "-i",
                    audioStreamFile.getAbsolutePath(),
                    "-i",
                    videoStreamFile.getAbsolutePath(),
                    /*  Specify to copy both video & audio streams
                        "-c",
                        "copy",
                     */
                    "-c:a",
                    "copy",
                    "-c:v",
                    "copy",
//                        targetFile.getAbsolutePath()
                    targetFile.getAbsolutePath().substring(0, dotIndex) + ext
            };

        } else if (shouldDirectlyCopyVideoStreams) {
            commandArr = new String[]{
                    executablePath,
                    "-i",
                    audioStreamFile.getAbsolutePath(),
                    "-i",
                    videoStreamFile.getAbsolutePath(),
                    "-c:v",
                    "copy",
//                        targetFile.getAbsolutePath()
                    targetFile.getAbsolutePath().substring(0, dotIndex) + ext

            };
        } else if (shouldDirectlyCopyAudioStreams) {
            commandArr = new String[]{
                    executablePath,
                    "-i",
                    audioStreamFile.getAbsolutePath(),
                    "-i",
                    videoStreamFile.getAbsolutePath(),
//                        targetFile.getAbsolutePath()
                    "-c:a",
                    "copy",
                    targetFile.getAbsolutePath().substring(0, dotIndex) + ext

            };
        } else {
            commandArr = new String[]{
                    executablePath,
                    "-i",
                    audioStreamFile.getAbsolutePath(),
                    "-i",
                    videoStreamFile.getAbsolutePath(),
//                        targetFile.getAbsolutePath()
                    targetFile.getAbsolutePath().substring(0, dotIndex) + ext
            };
        }

        System.out.println("Command: ");
        Arrays.stream(commandArr).forEach(System.out::println);
        System.out.println();

        return commandArr;

    }

    private String buildCommand(boolean ignored) {

        int dotIndex = videoStreamFile.getAbsolutePath().lastIndexOf('.');
        String ext = ".mkv";

        StringBuilder cmd;
        cmd = new StringBuilder();
        cmd.append(executablePath)
                .append(" -i")
                .append(" \"")
                .append(audioStreamFile.getAbsolutePath())
                .append("\"")
                .append(" -i")
                .append(" \"")
                .append(videoStreamFile.getAbsolutePath())
                .append("\"")
                .append(" \"")
                .append(videoStreamFile.getAbsolutePath(), 0, dotIndex)
                .append(ext)
                .append("\"");

        return cmd.toString();
    }

}
