package webservices;

/**
 * Class model for a video object.
 *
 * This class is used to store relevant video information only and discard the
 * rest of the information from the response of a search results
 */
public class Video {
    private String id;
    private String title;
    private String thumbnailURL;

    public Video(String id, String title, String thumbnailURL){
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
