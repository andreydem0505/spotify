package advisor.objects;

import java.util.List;

public class NewReleaseObject {
    private String name;
    private List<String> artists;
    private String url;

    public NewReleaseObject(String name, List<String> artists, String url) {
        this.name = name;
        this.artists = artists;
        this.url = url;
    }

    // getters
    public String getName() {
        return name;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getUrl() {
        return url;
    }
}
