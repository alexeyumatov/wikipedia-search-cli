package wiki.model;

import java.net.URI;

public record SearchResult(String title, String snippet, long pageId) {

    private static final String ARTICLE_URL = "https://ru.wikipedia.org/w/index.php?curid=";

    public SearchResult {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("У статьи должно быть название");
        }
        if (pageId <= 0) {
            throw new IllegalArgumentException("Некорректный идентификатор статьи: " + pageId);
        }
        if (snippet == null) {
            snippet = "";
        }
    }

    public URI articleUri() {
        return URI.create(ARTICLE_URL + pageId);
    }
}
