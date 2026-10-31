import java.util.List;

/**
 * Команда {@code exit [код]}: завершает работу эмулятора.
 */
public class ExitCommand implements Command {

    private static final int MAX_ARGS = 1;

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (args.size() > MAX_ARGS) {
            throw new CommandException("слишком много аргументов");
        }
        int code = 0;
        if (!args.isEmpty()) {
            try {
                code = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                throw new CommandException(args.get(0) + ": требуется числовой аргумент");
            }
        }
        shell.requestExit(code);
    }
}
