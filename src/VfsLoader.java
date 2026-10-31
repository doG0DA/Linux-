import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.HexFormat;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Загружает VFS из XML-файла в память. Файл на диске не изменяется.
 *
 * <p>Формат: корневой элемент {@code <vfs name="...">}, внутри элементы
 * {@code <dir name="...">} и {@code <file name="...">}. У обоих есть
 * необязательный атрибут {@code owner}. Содержимое файла — текст элемента;
 * при {@code encoding="base64"} это двоичные данные в base64.
 */
public final class VfsLoader {

    private static final String DEFAULT_OWNER = "root";

    private VfsLoader() {
    }

    /**
     * Загружает VFS.
     *
     * @param path путь к XML-файлу
     * @return загруженная VFS
     * @throws VfsException если файл не найден или имеет неверный формат
     */
    public static Vfs load(Path path) throws VfsException {
        byte[] data = readBytes(path);
        Element rootElement = parse(data).getDocumentElement();
        if (!"vfs".equals(rootElement.getTagName())) {
            throw new VfsException("неверный формат: корневой элемент должен быть <vfs>");
        }
        String name = rootElement.getAttribute("name");
        if (name.isBlank()) {
            name = path.getFileName().toString();
        }
        VfsDirectory root = new VfsDirectory("", ownerOf(rootElement), null);
        readChildren(rootElement, root);
        return new Vfs(name, path, sha256(data), root);
    }

    private static byte[] readBytes(Path path) throws VfsException {
        if (Files.isDirectory(path)) {
            throw new VfsException("ожидался файл, а не каталог: " + path);
        }
        try {
            return Files.readAllBytes(path);
        } catch (NoSuchFileException e) {
            throw new VfsException("файл не найден: " + path);
        } catch (IOException e) {
            throw new VfsException("не удалось прочитать файл: " + path);
        }
    }

    private static Document parse(byte[] data) throws VfsException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            var builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler());
            return builder.parse(new ByteArrayInputStream(data));
        } catch (SAXException | IOException e) {
            throw new VfsException("неверный формат XML: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new VfsException("не удалось создать XML-парсер");
        }
    }

    private static void readChildren(Element element, VfsDirectory dir) throws VfsException {
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                readNode((Element) node, dir);
            } else if (node.getNodeType() == Node.TEXT_NODE && !node.getTextContent().isBlank()) {
                throw new VfsException("неверный формат: текст внутри каталога " + dir.path());
            }
        }
    }

    private static void readNode(Element element, VfsDirectory parent) throws VfsException {
        String name = element.getAttribute("name");
        checkName(name, element.getTagName(), parent);
        String owner = ownerOf(element);
        if ("dir".equals(element.getTagName())) {
            VfsDirectory dir = new VfsDirectory(name, owner, parent);
            parent.add(dir);
            readChildren(element, dir);
        } else if ("file".equals(element.getTagName())) {
            parent.add(new VfsFile(name, owner, parent, fileData(element, name)));
        } else {
            throw new VfsException("неверный формат: неизвестный элемент <" + element.getTagName() + ">");
        }
    }

    private static void checkName(String name, String tag, VfsDirectory parent) throws VfsException {
        if (!isValidName(name)) {
            throw new VfsException("неверный формат: у <" + tag + "> в " + parent.path()
                    + " отсутствует или неверно имя \"" + name + "\"");
        }
        if (parent.child(name) != null) {
            throw new VfsException("неверный формат: повторяющееся имя " + name + " в " + parent.path());
        }
    }

    /**
     * Проверяет, допустимо ли имя файла или каталога.
     *
     * @param name имя
     * @return {@code true}, если имя не пустое, без {@code /} и не {@code .} или {@code ..}
     */
    public static boolean isValidName(String name) {
        return !name.isBlank() && !name.contains("/") && !name.equals(".") && !name.equals("..");
    }

    private static String ownerOf(Element element) {
        String owner = element.getAttribute("owner");
        return owner.isBlank() ? DEFAULT_OWNER : owner;
    }

    private static byte[] fileData(Element element, String name) throws VfsException {
        if (element.getElementsByTagName("*").getLength() > 0) {
            throw new VfsException("неверный формат: файл " + name + " содержит вложенные элементы");
        }
        String text = element.getTextContent();
        String encoding = element.getAttribute("encoding");
        if (encoding.isEmpty() || "text".equals(encoding)) {
            return dedent(text).getBytes(StandardCharsets.UTF_8);
        }
        if (!"base64".equals(encoding)) {
            throw new VfsException("неверный формат: неизвестная кодировка " + encoding + " у файла " + name);
        }
        try {
            return Base64.getDecoder().decode(text.replaceAll("\\s+", ""));
        } catch (IllegalArgumentException e) {
            throw new VfsException("неверный формат: некорректные данные base64 в файле " + name);
        }
    }

    private static String sha256(byte[] data) throws VfsException {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new VfsException("SHA-256 недоступен");
        }
    }

    /**
     * Убирает пустые строки по краям и общий отступ строк, появившийся
     * из-за форматирования XML.
     *
     * @param text текст элемента
     * @return текст без лишних отступов
     */
    static String dedent(String text) {
        List<String> lines = new ArrayList<>(text.lines().toList());
        while (!lines.isEmpty() && lines.get(0).isBlank()) {
            lines.remove(0);
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isBlank()) {
            lines.remove(lines.size() - 1);
        }
        int indent = Integer.MAX_VALUE;
        for (String line : lines) {
            if (!line.isBlank()) {
                indent = Math.min(indent, line.length() - line.stripLeading().length());
            }
        }
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            String cut = line.isBlank() ? "" : line.substring(indent);
            result.append(result.length() > 0 ? "\n" : "").append(cut.stripTrailing());
        }
        return result.toString();
    }
}
