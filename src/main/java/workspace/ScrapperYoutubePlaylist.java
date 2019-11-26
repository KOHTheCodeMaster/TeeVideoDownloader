package workspace;

import libs.koh_youtube_dl.utils.YoutubePlaylistPOJO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

class ScrapperYoutubePlaylist {

    private static int extractIntFromStr(String str) {

        String num = "";

        for (char c : str.toCharArray()) {
            if (Character.isDigit(c))
                num += c;
            else break;
        }

        return Integer.parseInt(num);

    }

    static int convertIntToNumOfCharsOccupied(int n) {
        if (n < 10)
            return 1;
        if (n < 100)
            return 2;
        if (n < 1000)
            return 3;
        if (n < 10000)
            return 4;
        if (n < 100000)
            return 5;
        return -1;  //  100K Limit Exceeded... :P
    }

    YoutubePlaylistPOJO acquireYTPLPOJO(String mainUrl) {

        YoutubePlaylistPOJO ytplPOJO = null;

        /*
            Time Stamp: 6th May 2K19, 09:46 AM..!!
              This method creates videoList of type VideoTracker locally,
              updates it accordingly by scraping off the url
              & returns that true only if videoList attribute of Scraper is successfully updated.
         */

        Document document = initializeDocument(mainUrl);

        String playlistTitle;
        String channelName;
        int videosCount;

        /*
            Time Stamp: 6th May 2K19, 05:35 PM..!!
            Acquire CSS Selector Queries for extracting particular
            section off the document for each video found in the playlist.

         */

        try {

            playlistTitle = acquireYTPlaylistTitle(document);
            channelName = acquireYTChannelName(document);
            videosCount = acquireYTPLVideosCount(document);

            ytplPOJO = new YoutubePlaylistPOJO(playlistTitle, channelName, videosCount);

        } catch (NullPointerException e) {
            System.out.println("NullPointerException [01] - Unable to instantiate YoutubePlaylistPOJO");
            e.printStackTrace();
        }

        return ytplPOJO;

    }

    private Document initializeDocument(String url) {

        Document document = null;
        int retryAttempts = 5;

        while (retryAttempts >= 0) {

            try {

                System.out.println("Establishing Connection.");

                //  Establish Connection to the HTML Page of the url.
                document = Jsoup.connect(url).get();
                if (document == null) {
                    retryAttempts--;
                    System.out.println("Connection Failed." +
                            "\nRetry Attempts Left : " + retryAttempts);
                } else break;

            } catch (IOException e) {
                System.out.println("Connection Failed.");
                System.exit(-204);
            }
        }

        if (document == null) {
            System.out.println("Unable to establish connection with Url : " + url);
            System.exit(-205);
        } else System.out.println("Connection Established Successfully.");

        return document;

    }

    private String acquireYTPlaylistTitle(Document document) {
        //  return youtube playlist title from YT Playlist Page off the document.
        final String TITLE_CSQ = "#pl-header > div.pl-header-content > h1";
        return document.selectFirst(TITLE_CSQ).text();
    }

    private String acquireYTChannelName(Document document) {
        //  return youtube playlist title from YT Playlist Page off the document.
        final String CHANNEL_NAME_CSQ = "#pl-header > div.pl-header-content > ul > li:nth-child(1) > a";
        return document.selectFirst(CHANNEL_NAME_CSQ).text();
    }

    private int acquireYTPLVideosCount(Document document) {
        //  return youtube playlist title from YT Playlist Page off the document.
        final String VIDEOS_COUNT_CSQ = "#pl-header > div.pl-header-content > ul > li:nth-child(2)";
        return extractIntFromStr(document.selectFirst(VIDEOS_COUNT_CSQ).text());
    }
}
