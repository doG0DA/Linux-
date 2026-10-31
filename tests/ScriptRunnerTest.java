import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Тесты выполнения стартового скрипта.
 */
class ScriptRunnerTest {

    private Path writeScript(String text) throws IOException {
        Path script = Files.createTempFile("script", ".txt");
        script.toFile().deleteOnExit();
        Files.writeString(script, text, StandardCharsets.UTF_8);
        return script;
    }

    @Test
    void showsInputAndOutput() throws IOException {
        ShellFixture fixture = new ShellFixture("--vfs", "v.xml");
        assertTrue(ScriptRunner.run(fixture.shell(), writeScript("conf-dump\n")));
        String output = fixture.output();
        assertTrue(output.contains("$ conf-dump\n"));
        assertTrue(output.contains("vfs=v.xml\n"));
    }

    @Test
    void skipsCommentsAndEmptyLines() throws IOException {
        ShellFixture fixture = new ShellFixture();
        assertTrue(ScriptRunner.run(fixture.shell(), writeScript("# комментарий\n\n   \n")));
        assertEquals("", fixture.output());
    }

    @Test
    void stopsAtFirstError() throws IOException {
        ShellFixture fixture = new ShellFixture();
        Path script = writeScript("conf-dump\nexit abc\nconf-dump\n");
        assertFalse(ScriptRunner.run(fixture.shell(), script));
        String output = fixture.output();
        assertTrue(output.contains("строка 2"));
        assertEquals(1, output.split("script=", -1).length - 1);
    }

    @Test
    void missingScriptIsError() {
        ShellFixture fixture = new ShellFixture();
        assertFalse(ScriptRunner.run(fixture.shell(), Path.of("no_such_script.txt")));
        assertTrue(fixture.output().contains("не найден"));
    }
}
