import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

class Example {
    static void example() throws Exception {
        // Path: opened -> opened -> opened -> closed
        ZipFile zip = new ZipFile(new File("test.zip"));
        zip.entries();
        zip.entries();
        zip.close();
    }
}
