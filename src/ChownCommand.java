import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Команда {@code chown [-R] владелец путь...}: меняет владельца файлов
 * и каталогов VFS. Ключ {@code -R} меняет владельца и всего содержимого
 * каталогов. Изменения выполняются только в памяти.
 */
public class ChownCommand implements Command {

    private static final String RECURSIVE_FLAG = "-R";
    private static final int MIN_OPERANDS = 2;
    private static final Pattern OWNER = Pattern.compile("[a-z_][A-Za-z0-9_.-]*");

    @Override
    public String name() {
        return "chown";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        boolean recursive = false;
        List<String> operands = new ArrayList<>();
        for (String arg : args) {
            if (RECURSIVE_FLAG.equals(arg)) {
                recursive = true;
            } else if (arg.startsWith("-")) {
                throw new CommandException("неверный ключ " + arg);
            } else {
                operands.add(arg);
            }
        }
        if (operands.size() < MIN_OPERANDS) {
            throw new CommandException("использование: chown [-R] владелец путь...");
        }
        String owner = operands.get(0);
        if (!OWNER.matcher(owner).matches()) {
            throw new CommandException("неверное имя владельца " + owner);
        }
        List<VfsNode> nodes = new ArrayList<>();
        for (String path : operands.subList(1, operands.size())) {
            nodes.add(shell.resolve(path));
        }
        for (VfsNode node : nodes) {
            setOwner(node, owner, recursive);
        }
    }

    private void setOwner(VfsNode node, String owner, boolean recursive) {
        node.setOwner(owner);
        if (recursive && node.isDirectory()) {
            for (VfsNode child : ((VfsDirectory) node).children()) {
                setOwner(child, owner, true);
            }
        }
    }
}
