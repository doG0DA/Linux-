import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Каталог VFS. Дочерние узлы хранятся отсортированными по имени.
 */
public class VfsDirectory extends VfsNode {

    private final Map<String, VfsNode> children = new TreeMap<>();

    /**
     * Создаёт каталог.
     *
     * @param name   имя
     * @param owner  владелец
     * @param parent родитель или {@code null} для корня
     */
    public VfsDirectory(String name, String owner, VfsDirectory parent) {
        super(name, owner, parent);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public long size() {
        return 0;
    }

    /**
     * Ищет дочерний узел по имени.
     *
     * @param name имя
     * @return узел или {@code null}
     */
    public VfsNode child(String name) {
        return children.get(name);
    }

    /**
     * Добавляет дочерний узел.
     *
     * @param node узел
     */
    public void add(VfsNode node) {
        children.put(node.name(), node);
    }

    /**
     * Дочерние узлы в порядке имён.
     *
     * @return дочерние узлы
     */
    public Collection<VfsNode> children() {
        return children.values();
    }
}
