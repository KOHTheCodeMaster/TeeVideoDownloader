package libs.koh_youtube_dl.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FFMPEGWrapper {

    private static final String executablePath = "ffmpeg";

    private File srcDir;
    private File audioStreamFile;
    private File videoStreamFile;

    public FFMPEGWrapper(File srcDir, File audioStreamFile, File videoStreamFile) {
        this.srcDir = srcDir;
        this.audioStreamFile = audioStreamFile;
        this.videoStreamFile = videoStreamFile;
    }

    public void startMerging() throws IOException {

        long startTime = System.nanoTime();

        int dotIndex = videoStreamFile.getAbsolutePath().lastIndexOf('.');
        String ext = ".mkv";

        System.out.println("dotIndex : " + dotIndex);
        System.out.println("vsf : " + videoStreamFile.getAbsolutePath());

        String[] commandArr = {executablePath,
                "-i",
                "\"" + audioStreamFile.getAbsolutePath() + "\"",
                "-i",
                "\"" + videoStreamFile.getAbsolutePath() + "\"",
                "\"" + videoStreamFile.getAbsolutePath().substring(0, dotIndex) + ext + "\"",
        };
//        command = buildCommand();
        System.out.println("Command: ");
        Arrays.stream(commandArr).forEach(System.out::println);
        System.out.println();

        //  inheritIO() method ensures that the process
        //  Output is redirected to the Java Program Console at realtime
        Process process;
        ProcessBuilder processBuilder = new ProcessBuilder(commandArr);//.inheritIO();

//        if (srcDir != null && srcDir.isDirectory())
//            processBuilder.directory(srcDir);

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
            System.out.println("Out Buffer : " + out);
            System.out.println("Err Buffer : " + err);
            long processTime = ((System.nanoTime() - startTime) / (long) 1E9);
            System.out.println("Process Time : " + processTime + " seconds.");
        }

    }

    private String buildCommand() {

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
