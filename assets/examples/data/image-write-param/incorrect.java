import javax.imageio.ImageWriteParam;
import java.util.Locale;

class Example {
    static void example() throws Exception {
        ImageWriteParam p = new ImageWriteParam(Locale.US);

        // Drive the tiling axis all the way to tilingSet...
        p.setTilingMode(2);            // -> tilingExplicit
        p.setTiling(64, 64, 0, 0);     // -> tilingSet
        int w = p.getTileWidth();      // ok

        // ...but the compression axis was never advanced.
        // It's still in startCompression — getCompressionType() needs
        // compressionExplicit or compressionSet.
        String type = p.getCompressionType();   // ✗ rejected

        // Violation: getCompressionType() requires compressionExplicit(this)
        // or compressionSet(this), but the compression axis is in state
        // startCompression.
    }
}
