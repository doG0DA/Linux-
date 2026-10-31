import java.util.List;

/**
 * Команда эмулятора.
 */
public interface Command {

    /**
     * Возвращает имя, по которому команда вызывается.
     *
     * @return имя команды
     */
    String name();

    /**
     * Выполняет команду.
     *
     * @param shell оболочка, в которой выполняется команда
     * @param args  аргументы команды (без имени)
     * @throws CommandException при ошибке выполнения
     */
    void run(Shell shell, List<String> args) throws CommandException;
}
