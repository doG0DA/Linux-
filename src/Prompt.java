import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Формирует приглашение к вводу вида {@code username@hostname:~$}
 * на основе данных реальной ОС.
 */
public class Prompt {

    /**
     * Собирает приглашение с указанной папкой.
     *
     * @param dir папка, которая показывается в приглашении
     * @return строка приглашения
     */
    public String build(String dir) {
        return getUserName() + "@" + getHostName() + ":" + dir + "$ ";
    }

    /**
     * Возвращает имя текущего пользователя ОС.
     *
     * @return имя пользователя
     */
    public String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Возвращает имя компьютера.
     *
     * @return имя компьютера или {@code localhost}, если его не удалось узнать
     */
    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * Возвращает текущую папку ОС; домашняя папка заменяется на {@code ~}.
     *
     * @return текущая папка
     */
    public String getRealDir() {
        String homeDir = System.getProperty("user.home");
        String currentDir = System.getProperty("user.dir");
        if (homeDir.equals(currentDir)) {
            return "~";
        }
        if (currentDir.startsWith(homeDir + File.separator)) {
            return "~" + currentDir.substring(homeDir.length());
        }
        return currentDir;
    }
}
