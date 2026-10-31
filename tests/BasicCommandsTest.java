import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты команд, которые не работают с VFS: ошибки, exit, conf-dump.
 */
class BasicCommandsTest {

    private final ShellFixture fixture = new ShellFixture("--vfs", "v.xml");

    @Test
    void lsWithoutVfsIsError() {
        assertTrue(fixture.run("ls").contains("VFS не загружена"));
    }

    @Test
    void cdWithTooManyArgumentsIsError() {
        assertEquals("cd: слишком много аргументов\n", fixture.run("cd a b"));
    }

    @Test
    void unknownCommandIsError() {
        assertEquals("foo: команда не найдена\n", fixture.run("foo bar"));
    }

    @Test
    void emptyLineDoesNothing() {
        assertEquals("", fixture.run("   "));
    }

    @Test
    void confDumpPrintsKeyValue() {
        assertEquals("vfs=v.xml\nscript=\n", fixture.run("conf-dump"));
    }

    @Test
    void confDumpWithArgumentsIsError() {
        assertTrue(fixture.run("conf-dump x").startsWith("conf-dump: "));
    }

    @Test
    void exitWithoutCode() {
        fixture.run("exit");
        assertFalse(fixture.shell().isRunning());
        assertEquals(0, fixture.shell().exitCode());
    }

    @Test
    void exitWithCode() {
        fixture.run("exit 3");
        assertFalse(fixture.shell().isRunning());
        assertEquals(3, fixture.shell().exitCode());
    }

    @Test
    void exitWithNonNumericCodeIsError() {
        assertTrue(fixture.run("exit abc").contains("требуется числовой аргумент"));
        assertTrue(fixture.shell().isRunning());
    }

    @Test
    void exitWithTooManyArgumentsIsError() {
        assertTrue(fixture.run("exit 1 2").contains("слишком много аргументов"));
        assertTrue(fixture.shell().isRunning());
    }
}
