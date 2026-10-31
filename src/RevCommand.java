import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Команда {@code rev файл...}: выводит строки файлов VFS,
 * перевернув каждую задом наперёд.
 */
public class RevCommand implements Command {

    @Override
    public String name() {
        return "rev";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (args.isEmpty()) {
            throw new CommandException("не указан файл");
        }
        for (String path : args) {
            VfsNode node = shell.resolve(path);
            if (node.isDirectory()) {
                throw new CommandException(path + ": это каталог");
            }
            String text = new String(((VfsFile) node).data(), StandardCharsets.UTF_8);
            text.lines().forEach(line -> shell.out().println(new StringBuilder(line).reverse()));
        }
    }
}
