/**
 * Файл VFS. Содержимое хранится в памяти.
 */
public class VfsFile extends VfsNode {

    private byte[] data;

    /**
     * Создаёт файл.
     *
     * @param name   имя
     * @param owner  владелец
     * @param parent родительский каталог
     * @param data   содержимое
     */
    public VfsFile(String name, String owner, VfsDirectory parent, byte[] data) {
        super(name, owner, parent);
        this.data = data.clone();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public long size() {
        return data.length;
    }

    /**
     * Содержимое файла.
     *
     * @return копия содержимого
     */
    public byte[] data() {
        return data.clone();
    }

    /**
     * Заменяет содержимое файла.
     *
     * @param newData новое содержимое
     */
    public void setData(byte[] newData) {
        data = newData.clone();
    }
}
