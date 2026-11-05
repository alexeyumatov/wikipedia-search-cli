package wiki.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import wiki.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseParser {

    private final Gson gson = new Gson();

    public List<SearchResult> parse(String json) {
        ApiResponse response = readResponse(json);
        if (response.error() != null) {
            throw new SearchException("Википедия вернула ошибку: " + response.error().info());
        }
        if (response.query() == null || response.query().search() == null) {
            throw new SearchException("Не удалось разобрать ответ Википедии");
        }
        List<SearchResult> results = new ArrayList<>();
        for (Hit hit : response.query().search()) {
            results.add(new SearchResult(hit.title(), cleanSnippet(hit.snippet()), hit.pageid()));
        }
        return List.copyOf(results);
    }

    private ApiResponse readResponse(String json) {
        try {
            ApiResponse response = gson.fromJson(json, ApiResponse.class);
            if (response == null) {
                throw new SearchException("Википедия вернула пустой ответ");
            }
            return response;
        } catch (JsonParseException e) {
            throw new SearchException("Не удалось разобрать ответ Википедии", e);
        }
    }

    // в сниппете приходит html: подсветка совпадений через <span> и экранированные символы
    private String cleanSnippet(String snippet) {
        if (snippet == null) {
            return "";
        }
        return snippet.replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .strip();
    }

    private record ApiResponse(Query query, ApiError error) {
    }

    private record Query(List<Hit> search) {
    }

    private record Hit(String title, String snippet, long pageid) {
    }

    private record ApiError(String info) {
    }
}
