package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Auth {
    private boolean isAuth;
    private final String accountsUrl;
    private final String clientId;
    private final String serverUrl;
    private final String clientSecret;
    private String code;
    private String accessToken;

    public Auth(String accountsUrl, String clientId, String serverUrl, String clientSecret) {
        isAuth = false;
        this.accountsUrl = accountsUrl;
        this.clientId = clientId;
        this.serverUrl = serverUrl;
        this.clientSecret = clientSecret;
    }

    public void authUser() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:");
        System.out.println(accountsUrl+"/authorize?client_id="+clientId+"&redirect_uri="+serverUrl+"&response_type=code");
        System.out.println("waiting for code...");
        if (getResponseOfAuth()) {
            System.out.println("code received");
            System.out.println("making http request for access_token...");
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .uri(URI.create(accountsUrl+"/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code="+code+"&redirect_uri="+serverUrl+"&client_id="+clientId+"&client_secret="+clientSecret))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response:");
            String json = response.body();
            System.out.println(json);
            JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
            accessToken = jo.get("access_token").getAsString();
            System.out.println("---SUCCESS---");
        }
    }

    public boolean getResponseOfAuth() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        if (query != null) {
                            if (query.matches("code=[\\w-]+")) {
                                success(query, exchange);
                            } else {
                                failed(exchange);
                            }
                        } else
                            failed(exchange);
                    }
                }
        );
        server.start();
        while(!isAuth) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.stop(1);
        return isAuth;
    }

    public void success(String query, HttpExchange exchange) throws IOException {
        String response;
        response = "Got the code. Return back to your program.";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        code = query.split("=")[1];
        isAuth = true;
    }

    public static void failed(HttpExchange exchange) throws IOException {
        String response;
        response = "Authorization code not found. Try again.";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }

    public boolean isAuth() {
        return isAuth;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
