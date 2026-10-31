import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты команд ls и cd на VFS examples/vfs/deep.xml.
 */
class NavigationTest {

    private ShellFixture fixture;

    @BeforeEach
    void loadVfs() {
        fixture = new ShellFixture();
        fixture.shell().loadVfs(Path.of("examples/vfs/deep.xml"));
    }

    private String promptDir() {
        String prompt = fixture.shell().promptText();
        return prompt.substring(prompt.lastIndexOf(':') + 1, prompt.lastIndexOf('$'));
    }

    @Test
    void lsCurrentDirectory() {
        assertEquals("etc  home  tmp\n", fixture.run("ls"));
    }

    @Test
    void lsLongFormatShowsTypeAndOwner() {
        String output = fixture.run("ls -l /home/yura");
        assertTrue(output.contains("- yura"));
        assertTrue(output.contains("d yura"));
        assertTrue(output.contains("hello.txt"));
    }

    @Test
    void lsSeveralPathsPrintsHeaders() {
        assertEquals("home:\nyura\n\netc:\nhostname\n", fixture.run("ls home etc"));
    }

    @Test
    void lsFilePrintsItsName() {
        assertEquals("hello.txt\n", fixture.run("ls /home/yura/hello.txt"));
    }

    @Test
    void lsUnknownFlagIsError() {
        assertTrue(fixture.run("ls -x").contains("неверный ключ"));
    }

    @Test
    void lsMissingPathIsError() {
        assertTrue(fixture.run("ls /nope").contains("нет такого файла или каталога"));
    }

    @Test
    void cdRelativeAndAbsolute() {
        fixture.run("cd home/yura");
        assertEquals("/home/yura", promptDir());
        fixture.run("cd /etc");
        assertEquals("/etc", promptDir());
    }

    @Test
    void cdParentDirectory() {
        fixture.run("cd /home/yura/projects");
        fixture.run("cd ../..");
        assertEquals("/home", promptDir());
    }

    @Test
    void cdWithoutArgumentsGoesToRoot() {
        fixture.run("cd /home/yura");
        fixture.run("cd");
        assertEquals("/", promptDir());
    }

    @Test
    void cdIntoFileIsError() {
        assertTrue(fixture.run("cd /etc/hostname").contains("не каталог"));
        assertEquals("/", promptDir());
    }

    @Test
    void cdMissingPathIsError() {
        assertTrue(fixture.run("cd /nope").contains("нет такого файла или каталога"));
    }
}
