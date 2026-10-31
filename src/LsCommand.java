import java.util.List;

/**
 * Команда {@code ls}. Пока заглушка: выводит своё имя и аргументы.
 */
public class LsCommand implements Command {

    @Override
    public String name() {
        return "ls";
    }

    @Override
    public void run(Shell shell, List<String> args) {
        shell.out().println("ls " + args);
    }
}
