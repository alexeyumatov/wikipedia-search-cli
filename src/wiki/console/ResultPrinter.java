package wiki.console;

import wiki.model.SearchResult;

import java.util.List;

public class ResultPrinter {

    private static final int MAX_SNIPPET_LENGTH = 150;

    public void print(List<SearchResult> results) {
        System.out.println();
        System.out.println("Результаты поиска:");
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            System.out.println((i + 1) + ". " + result.title());
            if (!result.snippet().isEmpty()) {
                System.out.println("   " + shorten(result.snippet()));
            }
        }
        System.out.println();
    }

    private String shorten(String text) {
        if (text.length() <= MAX_SNIPPET_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_SNIPPET_LENGTH).strip() + "...";
    }
}
