package wiki.browser;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class BrowserOpener {

    public void open(URI uri) {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            throw new IllegalStateException("Открыть браузер на этом компьютере не получится, ссылка на статью: " + uri);
        }
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось открыть браузер, ссылка на статью: " + uri, e);
        }
    }
}
