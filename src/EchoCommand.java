import java.util.List;

/**
 * Команда {@code echo [-n] [текст...]}: выводит аргументы через пробел.
 * Ключ {@code -n} отключает перевод строки в конце.
 */
public class EchoCommand implements Command {

    private static final String NO_NEWLINE = "-n";

    @Override
    public String name() {
        return "echo";
    }

    @Override
    public void run(Shell shell, List<String> args) {
        boolean newline = args.isEmpty() || !NO_NEWLINE.equals(args.get(0));
        List<String> words = newline ? args : args.subList(1, args.size());
        shell.out().print(String.join(" ", words));
        if (newline) {
            shell.out().println();
        }
        shell.out().flush();
    }
}
