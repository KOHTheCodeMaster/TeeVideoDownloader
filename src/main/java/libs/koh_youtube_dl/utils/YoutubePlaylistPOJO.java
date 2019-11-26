package libs.koh_youtube_dl.utils;

public class YoutubePlaylistPOJO {

    private String title;
    private String channelName;
    private int videosCount;

    public YoutubePlaylistPOJO(String title, String channelName, int videosCount) {
        this.title = title;
        this.channelName = channelName;
        this.videosCount = videosCount;
    }

    public String acquireFullyQualifiedPlaylistName() {
        String fullyQualifiedPlaylistName = title + " ~" + channelName + " [" + videosCount + "]";
        fullyQualifiedPlaylistName = fullyQualifiedPlaylistName.replaceAll("[\\-/\\\\:\"?<>*|]", "-");
        return fullyQualifiedPlaylistName;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getVideosCount() {
        return videosCount;
    }

    @Override
    public String toString() {
        return "POJOYoutubePlaylist{" +
                "title='" + title + '\'' +
                ", channelName='" + channelName + '\'' +
                ", numOfVids=" + videosCount +
                '}';
    }
}
