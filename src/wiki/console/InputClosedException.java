package wiki.console;

public class InputClosedException extends RuntimeException {

    public InputClosedException() {
        super("Ввод завершён");
    }
}
