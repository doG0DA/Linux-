import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

/**
 * Выполняет стартовый скрипт: каждая строка показывается вместе
 * с приглашением, как будто её ввёл пользователь. Выполнение
 * останавливается на первой ошибке. Пустые строки и строки,
 * начинающиеся с {@code #}, пропускаются.
 */
public final class ScriptRunner {

    private static final String COMMENT = "#";

    private ScriptRunner() {
    }

    /**
     * Выполняет скрипт.
     *
     * @param shell  оболочка
     * @param script путь к скрипту
     * @return {@code true}, если скрипт выполнен без ошибок
     */
    public static boolean run(Shell shell, Path script) {
        List<String> lines;
        try {
            lines = Files.readAllLines(script, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            shell.out().println("Ошибка: стартовый скрипт не найден: " + script);
            return false;
        } catch (IOException e) {
            shell.out().println("Ошибка: не удалось прочитать стартовый скрипт: " + script);
            return false;
        }
        for (int i = 0; i < lines.size() && shell.isRunning(); i++) {
            String line = lines.get(i);
            if (line.isBlank() || line.strip().startsWith(COMMENT)) {
                continue;
            }
            shell.out().println(shell.promptText() + line);
            if (!shell.execute(line)) {
                shell.out().println("Ошибка в стартовом скрипте " + script + ", строка " + (i + 1)
                        + ". Выполнение скрипта остановлено.");
                return false;
            }
        }
        return true;
    }
}
