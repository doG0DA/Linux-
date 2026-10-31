import java.io.PrintStream;
import java.nio.file.Path;
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
    private Vfs vfs;
    private VfsDirectory cwd;

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
        register(new VfsInfoCommand());
        register(new RevCommand());
        register(new ClearCommand());
        register(new EchoCommand());
        register(new TouchCommand());
        register(new ChownCommand());
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
     * Загружает VFS и делает её корень текущим каталогом.
     * Об ошибке загрузки сообщается в поток вывода.
     *
     * @param path путь к XML-файлу VFS
     * @return {@code true}, если VFS загружена
     */
    public boolean loadVfs(Path path) {
        try {
            vfs = VfsLoader.load(path);
            cwd = vfs.root();
            out.println("VFS загружена: " + vfs.name());
            return true;
        } catch (VfsException e) {
            out.println("Ошибка загрузки VFS: " + e.getMessage());
            return false;
        }
    }

    /**
     * Возвращает загруженную VFS.
     *
     * @return VFS
     * @throws CommandException если VFS не загружена
     */
    public Vfs requireVfs() throws CommandException {
        if (vfs == null) {
            throw new CommandException("VFS не загружена (используйте параметр --vfs)");
        }
        return vfs;
    }

    /**
     * Текущий каталог VFS.
     *
     * @return каталог
     * @throws CommandException если VFS не загружена
     */
    public VfsDirectory cwd() throws CommandException {
        requireVfs();
        return cwd;
    }

    /**
     * Находит узел VFS по пути относительно текущего каталога.
     *
     * @param path абсолютный или относительный путь
     * @return узел
     * @throws CommandException если VFS не загружена или путь не найден
     */
    public VfsNode resolve(String path) throws CommandException {
        return requireVfs().resolve(cwd, path);
    }

    /**
     * Меняет текущий каталог VFS.
     *
     * @param dir новый текущий каталог
     */
    public void setCwd(VfsDirectory dir) {
        cwd = dir;
    }

    /**
     * Возвращает текущее приглашение к вводу. Если VFS загружена,
     * в нём показывается текущий каталог VFS, иначе папка ОС.
     *
     * @return строка приглашения
     */
    public String promptText() {
        return prompt.build(vfs == null ? prompt.getRealDir() : cwd.path());
    }

    /**
     * Имя текущего пользователя ОС; становится владельцем новых файлов.
     *
     * @return имя пользователя
     */
    public String userName() {
        return prompt.getUserName();
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
