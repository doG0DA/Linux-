import java.io.InputStream;
import java.util.Scanner;

/**
 * Интерактивный цикл: приглашение, ввод, выполнение.
 */
public class Repl {

    private final Shell shell;
    private final Scanner scanner;

    /**
     * Создаёт цикл.
     *
     * @param shell оболочка
     * @param in    поток ввода пользователя
     */
    public Repl(Shell shell, InputStream in) {
        this.shell = shell;
        this.scanner = new Scanner(in);
    }

    /**
     * Работает, пока не вызвана {@code exit} или не закончился ввод.
     */
    public void run() {
        while (shell.isRunning()) {
            shell.out().print(shell.promptText());
            shell.out().flush();
            if (!scanner.hasNextLine()) {
                shell.out().println();
                return;
            }
            shell.execute(scanner.nextLine());
        }
    }
}
