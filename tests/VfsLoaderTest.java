import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Тесты загрузки VFS из XML и команды vfs-info.
 */
class VfsLoaderTest {

    private static final int SHA256_HEX_LENGTH = 64;

    private Vfs load(String name) throws VfsException {
        return VfsLoader.load(Path.of("examples/vfs/" + name));
    }

    @Test
    void minimalVfsIsEmpty() throws VfsException {
        Vfs vfs = load("minimal.xml");
        assertEquals("minimal", vfs.name());
        assertTrue(vfs.root().children().isEmpty());
    }

    @Test
    void deepVfsHasNestedDirectories() throws VfsException {
        Vfs vfs = load("deep.xml");
        VfsDirectory home = (VfsDirectory) vfs.root().child("home");
        VfsDirectory yura = (VfsDirectory) home.child("yura");
        VfsDirectory projects = (VfsDirectory) yura.child("projects");
        assertEquals("/home/yura/projects/config1", projects.child("config1").path());
        assertEquals("yura", yura.owner());
        assertEquals("root", home.owner());
    }

    @Test
    void textFileLosesXmlIndentation() throws VfsException {
        VfsFile notes = (VfsFile) load("multi.xml").root().child("notes.txt");
        String text = new String(notes.data(), StandardCharsets.UTF_8);
        assertEquals("первая строка\nвторая строка\nтретья строка", text);
    }

    @Test
    void base64FileIsDecoded() throws VfsException {
        VfsFile logo = (VfsFile) load("multi.xml").root().child("logo.bin");
        assertEquals((byte) 0x89, logo.data()[0]);
    }

    @Test
    void hashIsSha256() throws VfsException {
        assertEquals(SHA256_HEX_LENGTH, load("deep.xml").sha256().length());
    }

    @Test
    void missingFileIsError() {
        VfsException e = assertThrows(VfsException.class, () -> load("no_such.xml"));
        assertTrue(e.getMessage().contains("не найден"));
    }

    @Test
    void brokenXmlIsError() {
        assertThrows(VfsException.class, () -> load("bad/broken.xml"));
    }

    @Test
    void wrongRootIsError() {
        assertThrows(VfsException.class, () -> load("bad/wrong_root.xml"));
    }

    @Test
    void badBase64IsError() {
        assertThrows(VfsException.class, () -> load("bad/bad_base64.xml"));
    }

    @Test
    void duplicateNameIsError() {
        assertThrows(VfsException.class, () -> load("bad/duplicate.xml"));
    }

    @Test
    void unknownTagIsError() {
        assertThrows(VfsException.class, () -> load("bad/unknown_tag.xml"));
    }

    @Test
    void vfsInfoPrintsNameAndHash() throws VfsException {
        ShellFixture fixture = new ShellFixture();
        assertTrue(fixture.shell().loadVfs(Path.of("examples/vfs/deep.xml")));
        String info = fixture.run("vfs-info");
        assertTrue(info.contains("deep"));
        assertTrue(info.contains(load("deep.xml").sha256()));
    }

    @Test
    void vfsInfoWithoutVfsIsError() {
        assertTrue(new ShellFixture().run("vfs-info").contains("VFS не загружена"));
    }

    @Test
    void failedLoadReportsError() {
        ShellFixture fixture = new ShellFixture();
        assertFalse(fixture.shell().loadVfs(Path.of("examples/vfs/bad/broken.xml")));
        assertTrue(fixture.output().contains("Ошибка загрузки VFS"));
    }
}
