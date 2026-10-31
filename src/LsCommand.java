import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда {@code ls [-l] [путь...]}: выводит содержимое каталогов VFS.
 * Без пути выводится текущий каталог. Ключ {@code -l} включает подробный
 * формат: тип, владелец, размер, время изменения и имя.
 */
public class LsCommand implements Command {

    private static final String LONG_FLAG = "-l";
    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    @Override
    public String name() {
        return "ls";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        boolean longFormat = false;
        List<String> paths = new ArrayList<>();
        for (String arg : args) {
            if (LONG_FLAG.equals(arg)) {
                longFormat = true;
            } else if (arg.startsWith("-")) {
                throw new CommandException("неверный ключ " + arg);
            } else {
                paths.add(arg);
            }
        }
        if (paths.isEmpty()) {
            paths.add(".");
        }
        List<VfsNode> nodes = new ArrayList<>();
        for (String path : paths) {
            nodes.add(shell.resolve(path));
        }
        for (int i = 0; i < nodes.size(); i++) {
            if (paths.size() > 1) {
                shell.out().println((i > 0 ? "\n" : "") + paths.get(i) + ":");
            }
            print(shell, nodes.get(i), longFormat);
        }
    }

    private void print(Shell shell, VfsNode node, boolean longFormat) {
        List<VfsNode> entries = new ArrayList<>();
        if (node.isDirectory()) {
            entries.addAll(((VfsDirectory) node).children());
        } else {
            entries.add(node);
        }
        if (longFormat) {
            for (VfsNode entry : entries) {
                shell.out().println(longLine(entry));
            }
        } else if (!entries.isEmpty()) {
            List<String> names = new ArrayList<>();
            for (VfsNode entry : entries) {
                names.add(entry.name());
            }
            shell.out().println(String.join("  ", names));
        }
    }

    /**
     * Строка подробного формата для одного узла.
     *
     * @param node узел
     * @return строка вида {@code d yura 0 2026-10-23 12:00 projects}
     */
    protected String longLine(VfsNode node) {
        return String.format("%s %-8s %8d %s %s", node.isDirectory() ? "d" : "-", node.owner(), node.size(),
                DATE.format(node.modified()), node.name());
    }
}
