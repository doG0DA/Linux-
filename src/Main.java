import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Prompt prompt = new Prompt();
    private static final Parser parser = new Parser();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print(prompt.build());

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> tokens = parser.parse(line);

            if (!tokens.isEmpty()) {
                execute(tokens);
            }

            System.out.print(prompt.build());
        }
    }

    // Первое слово — имя команды, остальные — аргументы
    private static void execute(List<String> tokens) {
        String name = tokens.get(0);
        List<String> commandArgs = tokens.subList(1, tokens.size());

        switch (name) {
            case "ls" -> ls(commandArgs);
            case "cd" -> cd(commandArgs);
            case "exit" -> exit(commandArgs);
            default -> System.out.println(name + ": команда не найдена");
        }
    }

    // Заглушка: печатает имя и аргументы
    private static void ls(List<String> commandArgs) {
        System.out.println("ls " + commandArgs);
    }

    // Заглушка: печатает имя и аргументы. Принимает не больше одного аргумента
    private static void cd(List<String> commandArgs) {
        if (commandArgs.size() > 1) {
            System.out.println("cd: слишком много аргументов");
            return;
        }
        System.out.println("cd " + commandArgs);
    }

    // exit или exit <код>; код должен быть целым числом
    private static void exit(List<String> commandArgs) {
        if (commandArgs.size() > 1) {
            System.out.println("exit: слишком много аргументов");
            return;
        }

        int code = 0;
        if (commandArgs.size() == 1) {
            try {
                code = Integer.parseInt(commandArgs.get(0));
            } catch (NumberFormatException e) {
                System.out.println("exit: " + commandArgs.get(0) + ": требуется числовой аргумент");
                return;
            }
        }

        System.exit(code);
    }
}