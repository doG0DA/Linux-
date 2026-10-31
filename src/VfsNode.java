import java.time.Instant;

/**
 * Узел виртуальной файловой системы: файл или каталог.
 */
public abstract class VfsNode {

    private final String name;
    private final VfsDirectory parent;
    private String owner;
    private Instant modified = Instant.now();

    /**
     * Создаёт узел.
     *
     * @param name   имя узла (у корня пустое)
     * @param owner  владелец
     * @param parent родительский каталог или {@code null} для корня
     */
    protected VfsNode(String name, String owner, VfsDirectory parent) {
        this.name = name;
        this.owner = owner;
        this.parent = parent;
    }

    /**
     * Является ли узел каталогом.
     *
     * @return {@code true} для каталога
     */
    public abstract boolean isDirectory();

    /**
     * Размер узла в байтах.
     *
     * @return размер
     */
    public abstract long size();

    /**
     * Имя узла.
     *
     * @return имя
     */
    public String name() {
        return name;
    }

    /**
     * Владелец узла.
     *
     * @return имя владельца
     */
    public String owner() {
        return owner;
    }

    /**
     * Меняет владельца узла.
     *
     * @param owner новый владелец
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Время последнего изменения.
     *
     * @return время изменения
     */
    public Instant modified() {
        return modified;
    }

    /**
     * Обновляет время последнего изменения на текущее.
     */
    public void touch() {
        modified = Instant.now();
    }

    /**
     * Родительский каталог.
     *
     * @return родитель или {@code null} для корня
     */
    public VfsDirectory parent() {
        return parent;
    }

    /**
     * Абсолютный путь узла внутри VFS.
     *
     * @return путь вида {@code /home/user}
     */
    public String path() {
        if (parent == null) {
            return "/";
        }
        String parentPath = parent.path();
        return (parentPath.equals("/") ? "" : parentPath) + "/" + name;
    }
}
