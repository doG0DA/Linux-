import java.io.PrintStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Параметры эмулятора, заданные в командной строке.
 */
public final class Config {

    /** Подсказка по запуску. */
    public static final String USAGE =
            "Использование: run [--vfs <путь к VFS>] [--script <путь к стартовому скрипту>]";

    private static final String VFS_KEY = "--vfs";
    private static final String SCRIPT_KEY = "--script";

    private Path vfsPath;
    private Path scriptPath;

    private Config() {
    }

    /**
     * Разбирает параметры командной строки.
     *
     * @param args аргументы {@code main}
     * @return параметры эмулятора
     * @throws IllegalArgumentException при неизвестном или повторном параметре,
     *                                  отсутствующем значении или неверном пути
     */
    public static Config fromArgs(String[] args) {
        Config config = new Config();
        int i = 0;
        while (i < args.length) {
            String key = args[i];
            if (!VFS_KEY.equals(key) && !SCRIPT_KEY.equals(key)) {
                throw new IllegalArgumentException("неизвестный параметр " + key);
            }
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("не задано значение параметра " + key);
            }
            config.set(key, toPath(args[i + 1]));
            i += 2;
        }
        return config;
    }

    private void set(String key, Path value) {
        boolean isVfs = VFS_KEY.equals(key);
        if ((isVfs ? vfsPath : scriptPath) != null) {
            throw new IllegalArgumentException("параметр " + key + " задан повторно");
        }
        if (isVfs) {
            vfsPath = value;
        } else {
            scriptPath = value;
        }
    }

    private static Path toPath(String value) {
        try {
            return Path.of(value);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("неверный путь: " + value);
        }
    }

    /**
     * Путь к физическому расположению VFS.
     *
     * @return путь или {@code null}, если не задан
     */
    public Path vfsPath() {
        return vfsPath;
    }

    /**
     * Путь к стартовому скрипту.
     *
     * @return путь или {@code null}, если не задан
     */
    public Path scriptPath() {
        return scriptPath;
    }

    /**
     * Возвращает параметры в виде пар ключ-значение.
     *
     * @return параметры в порядке объявления
     */
    public Map<String, String> asMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("vfs", vfsPath == null ? "" : vfsPath.toString());
        map.put("script", scriptPath == null ? "" : scriptPath.toString());
        return map;
    }

    /**
     * Печатает отладочный вывод всех параметров при запуске.
     *
     * @param out поток вывода
     */
    public void printDebug(PrintStream out) {
        for (Map.Entry<String, String> entry : asMap().entrySet()) {
            String value = entry.getValue().isEmpty() ? "(не задан)" : entry.getValue();
            out.println("[debug] " + entry.getKey() + " = " + value);
        }
    }
}
