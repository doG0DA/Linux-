import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Вспомогательный класс для тестов: оболочка, вывод которой
 * пишется в буфер, чтобы его можно было проверить.
 */
final class ShellFixture {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final Shell shell;

    /**
     * Создаёт оболочку с параметрами командной строки.
     *
     * @param args параметры, например {@code "--vfs", "a.xml"}
     */
    ShellFixture(String... args) {
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        shell = new Shell(Config.fromArgs(args), out);
    }

    /**
     * Выполняет строку и возвращает её вывод.
     *
     * @param line строка для выполнения
     * @return вывод с переводами строк {@code \n}
     */
    String run(String line) {
        buffer.reset();
        shell.execute(line);
        return output();
    }

    /**
     * Весь вывод с момента последнего {@link #run} или {@link #clear}.
     *
     * @return вывод с переводами строк {@code \n}
     */
    String output() {
        return buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    /**
     * Очищает накопленный вывод.
     */
    void clear() {
        buffer.reset();
    }

    /**
     * Сама оболочка.
     *
     * @return оболочка
     */
    Shell shell() {
        return shell;
    }
}
