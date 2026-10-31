import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты команд touch и chown, изменяющих VFS в памяти.
 */
class ModifyingCommandsTest {

    private ShellFixture fixture;

    @BeforeEach
    void loadVfs() {
        fixture = new ShellFixture();
        fixture.shell().loadVfs(Path.of("examples/vfs/deep.xml"));
    }

    @Test
    void touchCreatesFiles() {
        fixture.run("touch /tmp/a.txt /tmp/b.txt");
        assertEquals("a.txt  b.txt\n", fixture.run("ls /tmp"));
    }

    @Test
    void touchRelativePath() {
        fixture.run("cd /home");
        fixture.run("touch new.txt");
        assertEquals("new.txt  yura\n", fixture.run("ls"));
    }

    @Test
    void newFileBelongsToCurrentUser() {
        fixture.run("touch /tmp/a.txt");
        assertTrue(fixture.run("ls -l /tmp").contains(fixture.shell().userName()));
    }

    @Test
    void touchExistingFileKeepsContent() {
        fixture.run("touch /home/yura/hello.txt");
        assertEquals("!dlrow ,olleH\n", fixture.run("rev /home/yura/hello.txt"));
    }

    @Test
    void touchWithoutArgumentsIsError() {
        assertTrue(fixture.run("touch").contains("не указан файл"));
    }

    @Test
    void touchInMissingDirectoryIsError() {
        assertTrue(fixture.run("touch /nope/x").contains("нет такого файла или каталога"));
    }

    @Test
    void touchInsideFileIsError() {
        assertTrue(fixture.run("touch /etc/hostname/x").contains("не каталог"));
    }

    @Test
    void chownSeveralPaths() {
        fixture.run("chown bob /etc /tmp");
        String output = fixture.run("ls -l /");
        assertTrue(output.contains("d bob") && output.contains("etc") && output.contains("d root"));
    }

    @Test
    void chownRecursive() {
        fixture.run("chown -R guest /home");
        assertTrue(fixture.run("ls -l /home/yura/projects/config1").contains("guest"));
    }

    @Test
    void chownWithoutPathIsError() {
        assertTrue(fixture.run("chown bob").contains("использование"));
    }

    @Test
    void chownBadOwnerIsError() {
        assertTrue(fixture.run("chown Bad! /tmp").contains("неверное имя владельца"));
    }

    @Test
    void chownMissingPathIsError() {
        assertTrue(fixture.run("chown bob /nope").contains("нет такого файла или каталога"));
    }

    @Test
    void chownUnknownFlagIsError() {
        assertTrue(fixture.run("chown -x bob /tmp").contains("неверный ключ"));
    }

    @Test
    void sourceFileIsNotModified() throws VfsException {
        fixture.run("touch /tmp/a.txt");
        Vfs fresh = VfsLoader.load(Path.of("examples/vfs/deep.xml"));
        assertTrue(((VfsDirectory) fresh.root().child("tmp")).children().isEmpty());
    }
}
