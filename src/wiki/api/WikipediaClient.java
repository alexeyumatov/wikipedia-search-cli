package wiki.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class WikipediaClient {

    private static final String SEARCH_URL =
            "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srlimit=%d&srsearch=%s";
    private static final int RESULT_LIMIT = 10;
    private static final int HTTP_OK = 200;
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    // без user-agent википедия может отклонить запрос
    private static final String USER_AGENT = "wikipedia-search-cli/1.0 (student project)";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    public String search(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Запрос не может быть пустым");
        }
        HttpRequest request = HttpRequest.newBuilder(buildSearchUri(query.strip()))
                .timeout(TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        HttpResponse<String> response = send(request);
        if (response.statusCode() != HTTP_OK) {
            throw new SearchException("Сервер Википедии ответил с кодом " + response.statusCode()
                    + ", попробуйте позже");
        }
        return response.body();
    }

    private URI buildSearchUri(String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return URI.create(String.format(SEARCH_URL, RESULT_LIMIT, encoded));
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new SearchException("Не удалось подключиться к Википедии, проверьте интернет", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SearchException("Запрос к Википедии был прерван", e);
        }
    }
}
