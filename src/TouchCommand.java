import java.util.List;

/**
 * Команда {@code touch файл...}: создаёт пустой файл в VFS или обновляет
 * время изменения существующего. Изменения выполняются только в памяти.
 * Владельцем нового файла становится текущий пользователь ОС.
 */
public class TouchCommand implements Command {

    @Override
    public String name() {
        return "touch";
    }

    @Override
    public void run(Shell shell, List<String> args) throws CommandException {
        if (args.isEmpty()) {
            throw new CommandException("не указан файл");
        }
        for (String path : args) {
            touch(shell, path);
        }
    }

    private void touch(Shell shell, String path) throws CommandException {
        VfsNode existing = find(shell, path);
        if (existing != null) {
            existing.touch();
            return;
        }
        int slash = path.lastIndexOf('/');
        String parentPath = slash < 0 ? "." : path.substring(0, Math.max(slash, 1));
        String fileName = path.substring(slash + 1);
        VfsNode parent = shell.resolve(parentPath);
        if (!parent.isDirectory()) {
            throw new CommandException(parentPath + ": не каталог");
        }
        if (!VfsLoader.isValidName(fileName)) {
            throw new CommandException(path + ": неверное имя файла");
        }
        VfsDirectory dir = (VfsDirectory) parent;
        dir.add(new VfsFile(fileName, shell.userName(), dir, new byte[0]));
        dir.touch();
    }

    private VfsNode find(Shell shell, String path) {
        try {
            return shell.resolve(path);
        } catch (CommandException e) {
            return null;
        }
    }
}
