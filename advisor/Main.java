package advisor;

import advisor.objects.PageableObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static String accountsUrl = "https://accounts.spotify.com";
    static String apiUrl = "https://api.spotify.com";
    static String clientId = "12a4f2debe1a4c6e9fdab72c2ecddd74";
    static String serverUrl = "http://localhost:8080";
    static String clientSecret = "61572d4917944a9d984a6c8390fd525c";
    static String accessToken;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String task, currentTask = null;
        if (args.length > 0) {
            if (args[0].equals("-access"))
                accountsUrl = args[1];
            if (args[2].equals("-resource"))
                apiUrl = args[3];
            if (args[4].equals("-page"))
                PageableObject.setQuantityOfObjectsOnOnePage(Integer.parseInt(args[5]));
        }
        Auth auth = new Auth(accountsUrl, clientId, serverUrl, clientSecret);
        Commands commands = new Commands(apiUrl);
        while (true) {
            task = scanner.next();
            switch (task) {
                case "auth": {
                    auth.authUser();
                    accessToken = auth.getAccessToken();
                    commands.setAccessToken(accessToken);
                } break;
                case "new": {
                    commands.newReleases(auth.isAuth());
                    currentTask = task;
                } break;
                case "featured": {
                    commands.featured(auth.isAuth());
                    currentTask = task;
                } break;
                case "categories": {
                    commands.categories(auth.isAuth());
                    currentTask = task;
                } break;
                case "next":
                case "prev": {
                    commands.changePage(auth.isAuth(), currentTask, task);
                } break;
                case "exit": {
                    System.out.println("---GOODBYE!---");
                    System.exit(0);
                }
                default: {
                   try {
                       String nameOfPlaylist = scanner.nextLine().strip();
                       if (task.equals("playlists")) {
                           commands.playlists(auth.isAuth(), nameOfPlaylist);
                           currentTask = task;
                       } else {
                           throw new TaskNotFoundException();
                       }
                   } catch (TaskNotFoundException e) {
                       System.out.println(e.toString());
                   }
                }
            }
        }
    }
}
