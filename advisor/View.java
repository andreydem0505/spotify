package advisor;

import advisor.objects.CategoriesObject;
import advisor.objects.FeaturedObject;
import advisor.objects.NewReleaseObject;
import advisor.objects.PlaylistsObject;

import java.util.Arrays;
import java.util.List;

public class View {
    public static void showNumberOfPage(int min, int max) {
        System.out.println("---PAGE "+min+" OF "+max+"---");
    }

    public static void printNewReleases(List<NewReleaseObject> newReleaseObjects) {
        for (int i = Pages.NewReleases.getMinIndex(); i <= Pages.NewReleases.getMaxIndex(); i++) {
            NewReleaseObject newReleaseObject = newReleaseObjects.get(i);
            System.out.println(newReleaseObject.getName());
            System.out.println(Arrays.toString(newReleaseObject.getArtists().toArray()));
            System.out.println(newReleaseObject.getUrl());
            System.out.println();
        }
        showNumberOfPage(Pages.NewReleases.getCurrentPage(), Pages.NewReleases.getQuantityOfPages());
    }

    public static void printFeatured(List<FeaturedObject> featuredObjects) {
        for (int i = Pages.Featured.getMinIndex(); i <= Pages.Featured.getMaxIndex(); i++) {
            FeaturedObject featuredObject = featuredObjects.get(i);
            System.out.println(featuredObject.getName());
            System.out.println(featuredObject.getUrl());
            System.out.println();
        }
        showNumberOfPage(Pages.Featured.getCurrentPage(), Pages.Featured.getQuantityOfPages());
    }

    public static void printCategories(List<CategoriesObject> categoriesObjects) {
        for (int i = Pages.Categories.getMinIndex(); i <= Pages.Categories.getMaxIndex(); i++) {
            CategoriesObject categoriesObject = categoriesObjects.get(i);
            System.out.println(categoriesObject.getName());
        }
        showNumberOfPage(Pages.Categories.getCurrentPage(), Pages.Categories.getQuantityOfPages());
    }

    public static void printPlaylists(List<PlaylistsObject> playlistsObjects) {
        for (int i = Pages.Playlists.getMinIndex(); i <= Pages.Playlists.getMaxIndex(); i++) {
            PlaylistsObject playlistsObject = playlistsObjects.get(i);
            System.out.println(playlistsObject.getName());
            System.out.println(playlistsObject.getUrl());
            System.out.println();
        }
        showNumberOfPage(Pages.Playlists.getCurrentPage(), Pages.Playlists.getQuantityOfPages());
    }
}
