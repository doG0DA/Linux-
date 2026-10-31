import java.util.List;

/**
 * Служебная команда {@code vfs-info}: выводит имя загруженной VFS
 * и хеш SHA-256 её данных.
 */
public class VfsInfoCommand implements Command {

    @Override
    public String name() {
        return "vfs-info";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (!args.isEmpty()) {
            throw new CommandException("команда не принимает аргументов");
        }
        Vfs vfs = shell.requireVfs();
        shell.out().println("Имя VFS: " + vfs.name());
        shell.out().println("Файл: " + vfs.source());
        shell.out().println("SHA-256: " + vfs.sha256());
    }
}
