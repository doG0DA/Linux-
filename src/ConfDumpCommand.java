import java.util.List;
import java.util.Map;

/**
 * Служебная команда {@code conf-dump}: выводит параметры эмулятора
 * в формате ключ-значение.
 */
public class ConfDumpCommand implements Command {

    @Override
    public String name() {
        return "conf-dump";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (!args.isEmpty()) {
            throw new CommandException("команда не принимает аргументов");
        }
        for (Map.Entry<String, String> entry : shell.config().asMap().entrySet()) {
            shell.out().println(entry.getKey() + "=" + entry.getValue());
        }
    }
}
