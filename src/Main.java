/**
 * Точка входа эмулятора командной строки.
 */
public final class Main {

    private static final int EXIT_BAD_ARGS = 2;

    private Main() {
    }

    /**
     * Запускает эмулятор.
     *
     * @param args параметры: {@code --vfs <путь>} и {@code --script <путь>}
     */
    public static void main(String[] args) {
        Config config;
        try {
            config = Config.fromArgs(args);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка параметров: " + e.getMessage());
            System.out.println(Config.USAGE);
            System.exit(EXIT_BAD_ARGS);
            return;
        }
        config.printDebug(System.out);
        Shell shell = new Shell(config, System.out);
        if (config.vfsPath() != null) {
            shell.loadVfs(config.vfsPath());
        }
        if (config.scriptPath() != null) {
            ScriptRunner.run(shell, config.scriptPath());
        }
        new Repl(shell, System.in).run();
        System.exit(shell.exitCode());
    }
}
