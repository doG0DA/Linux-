import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тесты разбора строки и раскрытия переменных окружения.
 */
class ParserTest {

    private final Parser parser = new Parser();
    private final String home = System.getenv("HOME") == null ? "" : System.getenv("HOME");

    @Test
    void splitsBySpaces() throws CommandException {
        assertEquals(List.of("ls", "-l", "/tmp"), parser.parse("  ls   -l /tmp "));
    }

    @Test
    void emptyLineGivesNoWords() throws CommandException {
        assertEquals(List.of(), parser.parse("   "));
    }

    @Test
    void expandsDollarVariable() throws CommandException {
        assertEquals(List.of("cd", home), parser.parse("cd $HOME"));
    }

    @Test
    void expandsVariableInBraces() throws CommandException {
        assertEquals(List.of("cd", home + "/docs"), parser.parse("cd ${HOME}/docs"));
    }

    @Test
    void unknownVariableBecomesEmpty() throws CommandException {
        assertEquals(List.of("ls"), parser.parse("ls $NO_SUCH_VARIABLE_123"));
    }

    @Test
    void loneDollarStaysAsIs() throws CommandException {
        assertEquals(List.of("echo", "$"), parser.parse("echo $"));
    }

    @Test
    void doubleQuotesKeepSpaces() throws CommandException {
        assertEquals(List.of("echo", "a  b"), parser.parse("echo \"a  b\""));
    }

    @Test
    void doubleQuotesExpandVariables() throws CommandException {
        assertEquals(List.of("echo", "home=" + home), parser.parse("echo \"home=$HOME\""));
    }

    @Test
    void singleQuotesDoNotExpand() throws CommandException {
        assertEquals(List.of("echo", "$HOME"), parser.parse("echo '$HOME'"));
    }

    @Test
    void unclosedQuoteIsError() {
        assertThrows(CommandException.class, () -> parser.parse("echo \"abc"));
    }

    @Test
    void unclosedBraceIsError() {
        assertThrows(CommandException.class, () -> parser.parse("echo ${HOME"));
    }
}
