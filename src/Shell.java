import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Ядро эмулятора: хранит состояние сеанса и выполняет команды.
 */
public class Shell {

    private final Config config;
    private final PrintStream out;
    private final Parser parser = new Parser();
    private final Prompt prompt = new Prompt();
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private boolean running = true;
    private int exitCode;

    /**
     * Создаёт оболочку.
     *
     * @param config параметры запуска
     * @param out    поток, куда пишется весь вывод команд
     */
    public Shell(Config config, PrintStream out) {
        this.config = config;
        this.out = out;
        register(new LsCommand());
        register(new CdCommand());
        register(new ExitCommand());
        register(new ConfDumpCommand());
    }

    private void register(Command command) {
        commands.put(command.name(), command);
    }

    /**
     * Разбирает и выполняет одну строку.
     *
     * @param line введённая строка
     * @return {@code true}, если строка выполнена без ошибок
     */
    public boolean execute(String line) {
        List<String> tokens;
        try {
            tokens = parser.parse(line);
        } catch (CommandException e) {
            out.println("Ошибка разбора: " + e.getMessage());
            return false;
        }
        if (tokens.isEmpty()) {
            return true;
        }
        String name = tokens.get(0);
        Command command = commands.get(name);
        if (command == null) {
            out.println(name + ": команда не найдена");
            return false;
        }
        try {
            command.run(this, tokens.subList(1, tokens.size()));
            return true;
        } catch (CommandException e) {
            out.println(name + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Возвращает текущее приглашение к вводу.
     *
     * @return строка приглашения
     */
    public String promptText() {
        return prompt.build(prompt.getRealDir());
    }

    /**
     * Завершает работу оболочки.
     *
     * @param code код завершения
     */
    public void requestExit(int code) {
        running = false;
        exitCode = code;
    }

    /**
     * Работает ли оболочка (не была ли вызвана {@code exit}).
     *
     * @return {@code true}, пока не вызвана {@code exit}
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Код завершения, заданный командой {@code exit}.
     *
     * @return код завершения
     */
    public int exitCode() {
        return exitCode;
    }

    /**
     * Параметры запуска.
     *
     * @return параметры
     */
    public Config config() {
        return config;
    }

    /**
     * Поток вывода оболочки.
     *
     * @return поток вывода
     */
    public PrintStream out() {
        return out;
    }
}
