import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    // Разбирает строку: раскрывает переменные окружения и делит на слова.
    // "cd $HOME" -> [cd, /home/yura]
    public List<String> parse(String line) {
        String expanded = expandVariables(line).trim();
        if (expanded.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(expanded.split("\\s+")));
    }

    // Заменяет $ИМЯ и ${ИМЯ} на значения переменных окружения ОС.
    // Несуществующая переменная заменяется пустой строкой, как в bash.
    private String expandVariables(String line) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < line.length()) {
            char c = line.charAt(i);

            if (c != '$') {
                result.append(c);
                i++;
                continue;
            }

            // Форма ${ИМЯ}
            if (i + 1 < line.length() && line.charAt(i + 1) == '{') {
                int close = line.indexOf('}', i + 2);
                if (close == -1) {
                    // Нет закрывающей скобки — оставляем текст как есть
                    result.append(line.substring(i));
                    break;
                }
                String name = line.substring(i + 2, close);
                result.append(getVariable(name));
                i = close + 1;
                continue;
            }

            // Форма $ИМЯ: имя состоит из букв, цифр и _
            int start = i + 1;
            int end = start;
            while (end < line.length()
                    && (Character.isLetterOrDigit(line.charAt(end)) || line.charAt(end) == '_')) {
                end++;
            }

            if (end == start) {
                // Одинокий $ без имени — просто символ
                result.append('$');
                i++;
            } else {
                result.append(getVariable(line.substring(start, end)));
                i = end;
            }
        }

        return result.toString();
    }

    private String getVariable(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value;
    }
}