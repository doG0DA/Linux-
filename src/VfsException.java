/**
 * Ошибка загрузки VFS: файл не найден, неверный формат и т. п.
 */
public class VfsException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с сообщением для пользователя.
     *
     * @param message текст ошибки
     */
    public VfsException(String message) {
        super(message);
    }
}
