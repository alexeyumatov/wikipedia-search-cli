package wiki.console;

import wiki.api.SearchException;
import wiki.api.SearchResponseParser;
import wiki.api.WikipediaClient;
import wiki.browser.BrowserOpener;
import wiki.model.SearchResult;

import java.util.List;

public class SearchMenu {

    private static final String EXIT_COMMAND = "выход";
    private static final int NEW_SEARCH = 0;

    private final ConsoleInput input;
    private final ResultPrinter printer;
    private final WikipediaClient client;
    private final SearchResponseParser parser;
    private final BrowserOpener browser;

    public SearchMenu(ConsoleInput input, ResultPrinter printer, WikipediaClient client,
                      SearchResponseParser parser, BrowserOpener browser) {
        this.input = input;
        this.printer = printer;
        this.client = client;
        this.parser = parser;
        this.browser = browser;
    }

    public void run() {
        System.out.println("Поиск по русской Википедии. Чтобы завершить работу, введите \"" + EXIT_COMMAND + "\".");
        try {
            while (true) {
                String query = input.readLine("Поисковый запрос: ");
                if (query.equalsIgnoreCase(EXIT_COMMAND)) {
                    break;
                }
                try {
                    searchAndOpen(query);
                } catch (SearchException | IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        } catch (InputClosedException e) {
            // stdin закрыли, просто выходим
            System.out.println();
        }
        System.out.println("До свидания!");
    }

    private void searchAndOpen(String query) {
        List<SearchResult> results = parser.parse(client.search(query));
        if (results.isEmpty()) {
            System.out.println("По запросу \"" + query + "\" ничего не найдено");
            return;
        }
        printer.print(results);
        int choice = input.readNumber("Номер статьи (" + NEW_SEARCH + " - новый поиск): ", NEW_SEARCH, results.size());
        if (choice == NEW_SEARCH) {
            return;
        }
        SearchResult article = results.get(choice - 1);
        System.out.println("Открываю статью \"" + article.title() + "\"");
        browser.open(article.articleUri());
    }
}
