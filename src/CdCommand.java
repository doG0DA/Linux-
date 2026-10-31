import java.util.List;

/**
 * Команда {@code cd}. Пока заглушка: выводит своё имя и аргументы.
 */
public class CdCommand implements Command {

    private static final int MAX_ARGS = 1;

    @Override
    public String name() {
        return "cd";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (args.size() > MAX_ARGS) {
            throw new CommandException("слишком много аргументов");
        }
        shell.out().println("cd " + args);
    }
}
