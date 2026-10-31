import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты команд rev, echo и clear.
 */
class TextCommandsTest {

    private ShellFixture fixture;

    @BeforeEach
    void loadVfs() {
        fixture = new ShellFixture();
        fixture.shell().loadVfs(Path.of("examples/vfs/deep.xml"));
    }

    @Test
    void revReversesLines() {
        assertEquals("!dlrow ,olleH\n", fixture.run("rev /home/yura/hello.txt"));
    }

    @Test
    void revSeveralFiles() {
        assertEquals("!dlrow ,olleH\ntsoh-sfv\n", fixture.run("rev /home/yura/hello.txt /etc/hostname"));
    }

    @Test
    void revWithoutFileIsError() {
        assertTrue(fixture.run("rev").contains("не указан файл"));
    }

    @Test
    void revDirectoryIsError() {
        assertTrue(fixture.run("rev /home").contains("это каталог"));
    }

    @Test
    void echoJoinsArguments() {
        assertEquals("a  b c\n", fixture.run("echo \"a  b\" c"));
    }

    @Test
    void echoWithoutNewline() {
        assertEquals("x", fixture.run("echo -n x"));
    }

    @Test
    void echoWithoutArgumentsPrintsEmptyLine() {
        assertEquals("\n", fixture.run("echo"));
    }

    @Test
    void clearPrintsAnsiSequence() {
        assertEquals("\033[H\033[2J", fixture.run("clear"));
    }

    @Test
    void clearWithArgumentsIsError() {
        assertTrue(fixture.run("clear now").contains("не принимает аргументов"));
    }
}
