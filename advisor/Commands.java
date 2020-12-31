package advisor;

import advisor.objects.CategoriesObject;
import advisor.objects.FeaturedObject;
import advisor.objects.NewReleaseObject;
import advisor.objects.PlaylistsObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class Commands {
    private final String goAway = "Please, provide access for application.";
    private String accessToken;
    private final String apiUrl;
    private List<NewReleaseObject> newReleaseObjects;
    private List<FeaturedObject> featuredObjects;
    private List<CategoriesObject> categoriesObjects;
    private List<PlaylistsObject> playlistsObjects;

    public Commands(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void newReleases(boolean isAuth) throws IOException, InterruptedException {
        if (!isAuth) {
            System.out.println(goAway);
            return;
        }
        newReleaseObjects = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiUrl + "/v1/browse/new-releases"))
                .GET()
                .build();
        JsonObject jo = RequestMaker.getJsonFromRequest(request);
        JsonObject albumsJo = jo.getAsJsonObject("albums");
        for (JsonElement item : albumsJo.getAsJsonArray("items")) {
            JsonObject itemJo = item.getAsJsonObject();
            String name = itemJo.get("name").getAsString();
            List<String> artists = new ArrayList<>();
            for (JsonElement artist : itemJo.getAsJsonArray("artists")) {
                JsonObject artistJo = artist.getAsJsonObject();
                artists.add(artistJo.get("name").getAsString());
            }
            JsonObject external_urlsJo = itemJo.getAsJsonObject("external_urls");
            String url = external_urlsJo.get("spotify").getAsString();

            newReleaseObjects.add(new NewReleaseObject(
                    name, artists, url
            ));
        }
        Pages.NewReleases.setPages(newReleaseObjects.size());

        View.printNewReleases(newReleaseObjects);
    }

    public void featured(boolean isAuth) throws IOException, InterruptedException {
        if (!isAuth) {
            System.out.println(goAway);
            return;
        }
        featuredObjects = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiUrl + "/v1/browse/featured-playlists"))
                .GET()
                .build();
        JsonObject jo = RequestMaker.getJsonFromRequest(request);
        JsonObject playlistsJo = jo.getAsJsonObject("playlists");
        for (JsonElement item : playlistsJo.getAsJsonArray("items")) {
            JsonObject itemJo = item.getAsJsonObject();
            String name = itemJo.get("name").getAsString();
            JsonObject external_urlsJo = itemJo.getAsJsonObject("external_urls");
            String url = external_urlsJo.get("spotify").getAsString();

            featuredObjects.add(new FeaturedObject(
                    name, url
            ));
        }
        Pages.Featured.setPages(featuredObjects.size());

        View.printFeatured(featuredObjects);
    }

    public void categories(boolean isAuth) throws IOException, InterruptedException {
        if (!isAuth) {
            System.out.println(goAway);
            return;
        }
        categoriesObjects = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiUrl + "/v1/browse/categories"))
                .GET()
                .build();
        JsonObject jo = RequestMaker.getJsonFromRequest(request);
        JsonObject categoriesJo = jo.getAsJsonObject("categories");
        for (JsonElement item : categoriesJo.getAsJsonArray("items")) {
            JsonObject itemJo = item.getAsJsonObject();
            String name = itemJo.get("name").getAsString();

            categoriesObjects.add(new CategoriesObject(
                    name
            ));
        }
        Pages.Categories.setPages(categoriesObjects.size());

        View.printCategories(categoriesObjects);
    }

    public void playlists(boolean isAuth, String nameOfPlaylist) throws IOException, InterruptedException {
        if (!isAuth) {
            System.out.println(goAway);
            return;
        }
        String categoryId = "none";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiUrl + "/v1/browse/categories"))
                .GET()
                .build();
        JsonObject jo = RequestMaker.getJsonFromRequest(request);
        JsonObject categoriesJo = jo.getAsJsonObject("categories");
        for (JsonElement item : categoriesJo.getAsJsonArray("items")) {
            JsonObject itemJo = item.getAsJsonObject();
            if (itemJo.get("name").getAsString().equals(nameOfPlaylist)) {
                categoryId = itemJo.get("id").getAsString();
                break;
            }
        }
        if (categoryId.equals("none")) {
            System.out.println("Unknown category name.");
            return;
        }

        request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiUrl + "/v1/browse/categories/"+categoryId+"/playlists"))
                .GET()
                .build();
        jo = RequestMaker.getJsonFromRequest(request);
        if (jo.has("error")) {
            JsonObject errorJo = jo.getAsJsonObject("error");
            String errorMessage = errorJo.get("message").getAsString();

            System.out.println(errorMessage);
        } else {
            playlistsObjects = new ArrayList<>();
            JsonObject playlistsJo = jo.getAsJsonObject("playlists");
            for (JsonElement item : playlistsJo.getAsJsonArray("items")) {
                JsonObject itemJo = item.getAsJsonObject();
                String name = itemJo.get("name").getAsString();
                JsonObject external_urlsJo = itemJo.getAsJsonObject("external_urls");
                String url = external_urlsJo.get("spotify").getAsString();

                playlistsObjects.add(new PlaylistsObject(
                        name, url
                ));
            }
            Pages.Playlists.setPages(playlistsObjects.size());

            View.printPlaylists(playlistsObjects);
        }
    }

    public void changePage(boolean isAuth, String currentTask, String task) {
        if (!isAuth) {
            System.out.println(goAway);
            return;
        }
        if (currentTask == null)
            return;
        switch (currentTask) {
            case "new": {
                boolean success;
                if (task.equals("next"))
                    success = Pages.NewReleases.nextPage();
                else
                    success = Pages.NewReleases.prevPage();
                if (success) {
                    View.printNewReleases(newReleaseObjects);
                } else {
                    System.out.println("No more pages.");
                }
            } break;
            case "featured": {
                boolean success;
                if (task.equals("next"))
                    success = Pages.Featured.nextPage();
                else
                    success = Pages.Featured.prevPage();
                if (success) {
                    View.printFeatured(featuredObjects);
                } else {
                    System.out.println("No more pages.");
                }
            } break;
            case "categories": {
                boolean success;
                if (task.equals("next"))
                    success = Pages.Categories.nextPage();
                else
                    success = Pages.Categories.prevPage();
                if (success) {
                    View.printCategories(categoriesObjects);
                } else {
                    System.out.println("No more pages.");
                }
            } break;
            case "playlists": {
                boolean success;
                if (task.equals("next"))
                    success = Pages.Playlists.nextPage();
                else
                    success = Pages.Playlists.prevPage();
                if (success) {
                    View.printPlaylists(playlistsObjects);
                } else {
                    System.out.println("No more pages.");
                }
            } break;
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
