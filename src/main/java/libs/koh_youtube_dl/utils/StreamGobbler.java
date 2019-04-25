package libs.koh_youtube_dl.utils;

import java.io.IOException;
import java.io.InputStream;

public class StreamGobbler extends Thread {
    private InputStream stream;
    private StringBuffer buffer;

    public StreamGobbler(StringBuffer buffer, InputStream stream) {
        this.stream = stream;
        this.buffer = buffer;
        this.start();
    }

    public void run() {
        while (true) {
            try {
                int nextChar;
                if ((nextChar = this.stream.read()) != -1) {
                    this.buffer.append((char) nextChar);
                    continue;
                }
            } catch (IOException var2) {
            }

            return;
        }
    }
}
