import liquidjava.specification.*;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

@ExternalRefinementsFor("java.util.zip.ZipFile")
@StateSet({"opened", "closed"})
@RefinementAlias("Mode(int x){ x == 1 || x == 4 || x == 5 }")
public interface ZipFileRefinementsExpert {

    @StateRefinement(to = "opened(this)")
    void ZipFile(File file);

    @StateRefinement(to = "opened(this)")
    void ZipFile(File file, @Refinement("Mode(_)") int mode);

    @StateRefinement(from = "opened(this)", to = "closed(this)")
    void close();

    @StateRefinement(from = "opened(this)")
    Enumeration<? extends ZipEntry> entries();

    @StateRefinement(from = "opened(this)")
    InputStream getInputStream(ZipEntry entry);

    @StateRefinement(from = "opened(this)")
    Stream<? extends ZipEntry> stream();
}
