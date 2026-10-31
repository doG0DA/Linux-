/**
 * Ошибка выполнения команды эмулятора: неверные аргументы,
 * отсутствующий файл и т. п. Сообщение выводится пользователю.
 */
public class CommandException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с сообщением для пользователя.
     *
     * @param message текст ошибки
     */
    public CommandException(String message) {
        super(message);
    }
}
