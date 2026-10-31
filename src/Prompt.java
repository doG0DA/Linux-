import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Prompt {

    private static final int userHomeLength = System.getProperty("user.home").length();

    // Собирает приглашение вида yura@laptop:~$
    public String build() {
        return getUserName() + "@"
                + getHostName() + ":"
                + getCurrentDir() + "$ ";
    }

    public String getUserName() {
        return System.getProperty("user.name");
    }

    public String getHostName() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    public String getCurrentDir() {
        String homeDir = System.getProperty("user.home");
        String currentDir = System.getProperty("user.dir");

        if (homeDir.equals(currentDir)) {
            return "~";
        }

        if (currentDir.startsWith(homeDir)) {
            String letter = String.valueOf(currentDir.charAt(userHomeLength));
            if (letter.equals(File.separator)) {
                return "~" + currentDir.substring(userHomeLength);
            }
        }

        return currentDir;
    }
}