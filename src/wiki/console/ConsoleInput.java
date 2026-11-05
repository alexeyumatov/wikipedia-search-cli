package wiki.console;

import java.util.Scanner;

public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new InputClosedException();
        }
        return scanner.nextLine().strip();
    }

    public int readNumber(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int number = Integer.parseInt(line);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // не число, переспрашиваем ниже
            }
            System.out.println("Введите число от " + min + " до " + max);
        }
    }
}
