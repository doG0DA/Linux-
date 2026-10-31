import java.util.List;

/**
 * Команда {@code clear}: очищает экран терминала управляющей
 * последовательностью ANSI.
 */
public class ClearCommand implements Command {

    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (!args.isEmpty()) {
            throw new CommandException("команда не принимает аргументов");
        }
        shell.out().print(CLEAR_SCREEN);
        shell.out().flush();
    }
}
