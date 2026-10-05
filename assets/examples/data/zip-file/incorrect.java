import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

class Example {
    static void example() throws Exception {
        // Violation: stream() called after close() — use-after-close.
        ZipFile zip = new ZipFile(new File("test.zip"));
        zip.entries();
        zip.close();
        zip.stream();   // INVALID: stream() requires opened(zip),
                        //          but zip is in state closed
    }
}
