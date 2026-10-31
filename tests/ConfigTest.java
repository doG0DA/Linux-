import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тесты разбора параметров командной строки.
 */
class ConfigTest {

    @Test
    void noParameters() {
        Config config = Config.fromArgs(new String[0]);
        assertEquals("", config.asMap().get("vfs"));
        assertEquals("", config.asMap().get("script"));
    }

    @Test
    void bothParametersInAnyOrder() {
        Config config = Config.fromArgs(new String[] {"--script", "s.txt", "--vfs", "a.xml"});
        assertEquals("a.xml", config.asMap().get("vfs"));
        assertEquals("s.txt", config.asMap().get("script"));
    }

    @Test
    void unknownParameterIsError() {
        assertThrows(IllegalArgumentException.class, () -> Config.fromArgs(new String[] {"--foo", "x"}));
    }

    @Test
    void missingValueIsError() {
        assertThrows(IllegalArgumentException.class, () -> Config.fromArgs(new String[] {"--vfs"}));
    }

    @Test
    void repeatedParameterIsError() {
        assertThrows(IllegalArgumentException.class,
                () -> Config.fromArgs(new String[] {"--vfs", "a", "--vfs", "b"}));
    }
}
