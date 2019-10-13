package libs.koh_youtube_dl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    private InputStream stream;
    private StringBuffer buffer;
    private boolean shouldPrintStream;

    public StreamGobbler(StringBuffer buffer, InputStream stream) {
        this(buffer, stream, false);
    }

    StreamGobbler(StringBuffer buffer, InputStream stream, boolean shouldPrintStream) {
        this.stream = stream;
        this.buffer = buffer;
        this.shouldPrintStream = shouldPrintStream;
        this.start();
    }

    @Override
    public void run() {
        System.out.println("RUN..!!\n");
        if (shouldPrintStream) printStreamOutput2();
        else bufferStreamOutput();
    }

    private void printStreamOutput() {

        /*
         *  Time Stamp: 29th Sep. 2K19, 08:50 PM..!!
         *
         *  Prints Output of the Stream which has already been ended.
         */

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {

            String currentLine;
            while ((currentLine = br.readLine()) != null)
                System.out.println(currentLine);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printStreamOutput2() {

        while (true) {
            try {
                InputStreamReader isr = new InputStreamReader(stream);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                if ((line = br.readLine()) != null) {
                    System.out.println(line);
                    continue;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return;
        }

    }

    private void bufferStreamOutput2() {

        while (true) {
            try {
                int nextChar;
                if ((nextChar = this.stream.read()) != -1) {
                    this.buffer.append((char) nextChar);
                    System.out.print(nextChar);
                    continue;
                }
            } catch (IOException ignored) {
            }

            return;
        }

    }

    private void bufferStreamOutput() {

        while (true) {
            try {
                int nextChar;
                if ((nextChar = this.stream.read()) != -1) {
                    this.buffer.append((char) nextChar);
//                    System.out.print((char)nextChar);
                    continue;
                }
            } catch (IOException ignored) {
            }
            return;
        }

    }
}
