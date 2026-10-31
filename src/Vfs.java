import java.nio.file.Path;

/**
 * Загруженная виртуальная файловая система.
 */
public class Vfs {

    private final String name;
    private final Path source;
    private final String sha256;
    private final VfsDirectory root;

    /**
     * Создаёт VFS.
     *
     * @param name   имя VFS
     * @param source путь к исходному файлу
     * @param sha256 хеш SHA-256 исходных данных
     * @param root   корневой каталог
     */
    public Vfs(String name, Path source, String sha256, VfsDirectory root) {
        this.name = name;
        this.source = source;
        this.sha256 = sha256;
        this.root = root;
    }

    /**
     * Имя VFS.
     *
     * @return имя
     */
    public String name() {
        return name;
    }

    /**
     * Путь к исходному файлу.
     *
     * @return путь
     */
    public Path source() {
        return source;
    }

    /**
     * Хеш SHA-256 исходных данных в шестнадцатеричном виде.
     *
     * @return хеш
     */
    public String sha256() {
        return sha256;
    }

    /**
     * Корневой каталог.
     *
     * @return корень
     */
    public VfsDirectory root() {
        return root;
    }

    /**
     * Находит узел по пути. Поддерживаются абсолютные и относительные пути,
     * {@code .} и {@code ..}.
     *
     * @param cwd  текущий каталог, от которого считается относительный путь
     * @param path путь
     * @return найденный узел
     * @throws CommandException если узел не найден или часть пути не каталог
     */
    public VfsNode resolve(VfsDirectory cwd, String path) throws CommandException {
        VfsNode node = path.startsWith("/") ? root : cwd;
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                node = node.parent() == null ? node : node.parent();
                continue;
            }
            if (!node.isDirectory()) {
                throw new CommandException(path + ": не каталог");
            }
            VfsNode child = ((VfsDirectory) node).child(part);
            if (child == null) {
                throw new CommandException(path + ": нет такого файла или каталога");
            }
            node = child;
        }
        return node;
    }
}
