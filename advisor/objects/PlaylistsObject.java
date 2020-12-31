package advisor.objects;


public class PlaylistsObject {
    private String name;
    private String url;

    public PlaylistsObject(String name, String url) {
        this.name = name;
        this.url = url;
    }

    // getters
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
