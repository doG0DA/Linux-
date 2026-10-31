import java.util.List;

/**
 * Команда {@code cd [путь]}: меняет текущий каталог VFS.
 * Без аргумента переходит в корень.
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
        VfsNode target = shell.resolve(args.isEmpty() ? "/" : args.get(0));
        if (!target.isDirectory()) {
            throw new CommandException(args.get(0) + ": не каталог");
        }
        shell.setCwd((VfsDirectory) target);
    }
}
