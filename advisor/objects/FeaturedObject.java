package advisor.objects;

import java.util.List;

public class FeaturedObject {
    private String name;
    private String url;

    public FeaturedObject(String name, String url) {
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
