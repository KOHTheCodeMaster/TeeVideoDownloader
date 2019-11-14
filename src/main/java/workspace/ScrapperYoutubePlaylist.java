package workspace;

import libs.koh_youtube_dl.utils.YoutubePlaylistPOJO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

class ScrapperYoutubePlaylist {

//    private String url;

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

    private static int extractIntFromStr(String str) {

        String num = "";

        for (char c : str.toCharArray()) {
            if (Character.isDigit(c))
                num += c;
            else break;
        }

        return Integer.parseInt(num);

    }

    YoutubePlaylistPOJO acquireYTPOJO(String mainUrl) {

        YoutubePlaylistPOJO pojoYTPL = null;

        /*
            Time Stamp: 6th May 2K19, 09:46 AM..!!
              This method creates videoList of type VideoTracker locally,
              updates it accordingly by scraping off the url
              & returns that true only if videoList attribute of Scraper is successfully updated.
         */

        Document document = initializeDocument(mainUrl);
//        int count;  //  Total Number of Videos Count.

        String youtubePlaylistTitle;
        String channelName;
        int videosCount;

        /*
            Time Stamp: 6th May 2K19, 05:35 PM..!!
            Acquire CSS Selector Queries for extracting particular
            section off the document for each video found in the playlist.

            ytPLTitleElement => List of All the Titles of videos in Playlist
            timeLengthElements => List of Time Length (e.g.: 01:23 i.e. 1 min. 23 sec.)

            Css Selector Query for url of video is same as its title. It just
            requires element.attr("href") property to extract url rather than element.text()

         */

        try {

            /*
                System.out.println("==!Begin!==");
                System.out.println(document.html());
                System.out.println("==!End!==");
            */

            Element ytPLTitleElement = document.selectFirst(acquireYTPlaylistTitleCssSelectorQuery());
            youtubePlaylistTitle = ytPLTitleElement.text();

            Element ytChannelNameElement = document.selectFirst(acquireYTPLChannelNameCssSelectorQuery());
            channelName = ytChannelNameElement.text();

            Element ytNumOfVidsElement = document.selectFirst(acquireYTPLVideosCountCssSelectorQuery());
            String strVideosCount = ytNumOfVidsElement.text();
            videosCount = extractIntFromStr(strVideosCount);

            /*
                System.out.println("str : [" + strVideosCount + "] | len : " + strVideosCount.length());
                strVideosCount = strVideosCount.substring(0, 1);
                videosCount = Integer.parseInt(strVideosCount);
            */

            pojoYTPL = new YoutubePlaylistPOJO(youtubePlaylistTitle, channelName, videosCount);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return pojoYTPL;

    }

    private String acquireYTPlaylistTitleCssSelectorQuery() {
        //  Return valid CSS Selector Query for extracting youtube playlist title from YT Playlist Page off the document.
        return "#pl-header > div.pl-header-content > h1";
    }

    private String acquireYTPLChannelNameCssSelectorQuery() {
        //  Return valid CSS Selector Query for extracting youtube channel name from YT Playlist Page off the document.
        return "#pl-header > div.pl-header-content > ul > li:nth-child(1) > a";
    }

    private String acquireYTPLVideosCountCssSelectorQuery() {
        //  Return valid CSS Selector Query for extracting video title off the document.
        return "#pl-header > div.pl-header-content > ul > li:nth-child(2)";
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

}
