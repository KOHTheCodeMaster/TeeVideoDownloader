package libs.koh_youtube_dl.utils;

import java.util.Scanner;

public enum VideoQuality {

    Q_4K(3840, 2160),  //  2160P
    Q_2K(2560, 1440),  //  1440P
    Q_1080P(1920, 1080),
    Q_720P(1280, 720),
    Q_480P(854, 480),
    Q_360P(640, 360),
    Q_240P(426, 240),
    Q_144P(256, 144);

    private final int width;
    private final int height;

    VideoQuality(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static VideoQuality parseWidthToVideoQuality(int width) throws VideoQualityOutOfScopeException {

        /*
         *  Time Stamp: 28th Sep. 2K19, 01:18 PM..!!
         *
         *  This method returns true if provided width is within the range of [VQ.width +- wdx]
         *
         *  If 1080P VideoQuality invokes this method with width 640,
         *  then it'll firstly check if the provided width is within the scope of particularly 1080P Width Range
         *  if not then it'll be checked in descending order starting from 720P until 144P width range.
         */

        int wdx = 200;  //  Width Margin

        if (width < Q_4K.width + wdx && width > Q_4K.width - wdx)
            return Q_4K;
        if (width < Q_2K.width + wdx && width > Q_2K.width - wdx)
            return Q_2K;
        if (width < Q_1080P.width + wdx && width > Q_1080P.width - wdx)
            return Q_1080P;
        if (width < Q_720P.width + wdx && width > Q_720P.width - wdx)
            return Q_720P;
        if (width < Q_480P.width + wdx && width > Q_480P.width - wdx)
            return Q_480P;
        if (width < Q_360P.width + wdx && width > Q_360P.width - wdx)
            return Q_360P;
        if (width < Q_240P.width + wdx && width > Q_240P.width - wdx)
            return Q_240P;
        if (width < Q_144P.width + wdx && width > Q_144P.width - wdx)
            return Q_144P;

        String exceptionMsg = "Invalid Width Detected...\n" +
                "Found width  : " + width + "\n";
        throw new VideoQualityOutOfScopeException(exceptionMsg);

    }

    public static VideoQuality chooseVideoQuality() {

        Scanner scanner = new Scanner(System.in);
        int ch;

        do {
            ch = scanner.nextInt();
            switch (ch) {

                case 4000:
                    return Q_4K;
                case 2000:
                    return Q_2K;
                case 1080:
                    return Q_1080P;
                case 720:
                    return Q_720P;
                case 480:
                    return Q_480P;
                case 360:
                    return Q_360P;
                case 240:
                    return Q_240P;
                case 144:
                    return Q_144P;
                default:
                    System.out.println("Invalid Input!\nPlease Enter Valid Width Resolution : ");

            }
        } while (true);

    }

    public VideoQuality acquireTopmostPreferredVQ(VideoQuality currentVQ) throws VideoQualityOutOfScopeException {

        /*
         *  Time Stamp: 29th Sep. 2K19, 09:18 AM..!!
         *
         *  This method returns Topmost Preferred VideoQuality when provided currentVQ
         *
         *  If 1080P VideoQuality invokes this method with currentVQ 640,
         *  then it'll firstly check if the provided currentVQ == 1080P
         *  if not then it'll be checked with remaining VideoQuality in descending order
         *  starting from 720P until 144P currentVQ range.
         */

        switch (this) {

            case Q_4K:
                if (Q_4K == currentVQ) return Q_4K;
            case Q_2K:
                if (Q_2K == currentVQ) return Q_2K;
            case Q_1080P:
                if (Q_1080P == currentVQ) return Q_1080P;
            case Q_720P:
                if (Q_720P == currentVQ) return Q_720P;
            case Q_480P:
                if (Q_480P == currentVQ) return Q_480P;
            case Q_360P:
                if (Q_360P == currentVQ) return Q_360P;
            case Q_240P:
                if (Q_240P == currentVQ) return Q_240P;
            case Q_144P:
                if (Q_144P == currentVQ) return Q_144P;

            default:

                if (currentVQ.width > this.width)
                    return null;

                String exceptionMsg = "Invalid Scope Detected...\n" +
                        "Quality: " + this.toString() + "\n" +
                        "Found currentVQ  : " + currentVQ + " | height : " + height + "\n";
                throw new VideoQualityOutOfScopeException(exceptionMsg, currentVQ);
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
