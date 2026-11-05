package wiki;

import wiki.api.SearchResponseParser;
import wiki.api.WikipediaClient;
import wiki.browser.BrowserOpener;
import wiki.console.ConsoleInput;
import wiki.console.ResultPrinter;
import wiki.console.SearchMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SearchMenu menu = new SearchMenu(
                new ConsoleInput(new Scanner(System.in)),
                new ResultPrinter(),
                new WikipediaClient(),
                new SearchResponseParser(),
                new BrowserOpener()
        );
        menu.run();
    }
}
