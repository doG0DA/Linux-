import java.util.ArrayList;
import java.util.List;

/**
 * Разбирает введённую строку на слова и раскрывает переменные окружения ОС.
 *
 * <p>Поддерживается: {@code $ИМЯ} и {@code ${ИМЯ}}, двойные кавычки
 * (переменные внутри раскрываются) и одинарные кавычки (текст берётся
 * как есть). Несуществующая переменная заменяется пустой строкой.
 */
public class Parser {

    /**
     * Разбирает строку на слова.
     *
     * @param line введённая строка
     * @return список слов; первое — имя команды
     * @throws CommandException если не закрыта кавычка или скобка
     */
    public List<String> parse(String line) throws CommandException {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inToken = false;
        int i = 0;
        while (i < line.length()) {
            char c = line.charAt(i);
            if (Character.isWhitespace(c)) {
                if (inToken) {
                    tokens.add(current.toString());
                    current.setLength(0);
                    inToken = false;
                }
                i++;
            } else {
                i = readPart(line, i, current);
                inToken = inToken || isQuote(c) || current.length() > 0;
            }
        }
        if (inToken) {
            tokens.add(current.toString());
        }
        return tokens;
    }

    private boolean isQuote(char c) {
        return c == '\'' || c == '"';
    }

    private int readPart(String line, int i, StringBuilder out) throws CommandException {
        char c = line.charAt(i);
        if (c == '\'') {
            return readSingleQuoted(line, i + 1, out);
        }
        if (c == '"') {
            return readDoubleQuoted(line, i + 1, out);
        }
        if (c == '$') {
            return expandVariable(line, i, out);
        }
        out.append(c);
        return i + 1;
    }

    private int readSingleQuoted(String line, int start, StringBuilder out) throws CommandException {
        int close = line.indexOf('\'', start);
        if (close < 0) {
            throw new CommandException("незакрытая кавычка '");
        }
        out.append(line, start, close);
        return close + 1;
    }

    private int readDoubleQuoted(String line, int start, StringBuilder out) throws CommandException {
        int i = start;
        while (i < line.length()) {
            char c = line.charAt(i);
            if (c == '"') {
                return i + 1;
            }
            if (c == '$') {
                i = expandVariable(line, i, out);
            } else {
                out.append(c);
                i++;
            }
        }
        throw new CommandException("незакрытая кавычка \"");
    }

    private int expandVariable(String line, int dollar, StringBuilder out) throws CommandException {
        int next = dollar + 1;
        if (next < line.length() && line.charAt(next) == '{') {
            int close = line.indexOf('}', next);
            if (close < 0) {
                throw new CommandException("незакрытая скобка ${");
            }
            out.append(getVariable(line.substring(next + 1, close)));
            return close + 1;
        }
        int end = next;
        while (end < line.length() && isNameChar(line.charAt(end))) {
            end++;
        }
        if (end == next) {
            out.append('$');
            return next;
        }
        out.append(getVariable(line.substring(next, end)));
        return end;
    }

    private boolean isNameChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private String getVariable(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value;
    }
}
