import javax.imageio.ImageWriteParam;
import java.util.Locale;

class Example {
    static void example() throws Exception {
        ImageWriteParam p = new ImageWriteParam(Locale.US);
        // state: startTiling && startCompression

        // --- Tiling axis: drive it independently ---
        p.setTilingMode(2);            // -> tilingExplicit
        p.setTiling(64, 64, 0, 0);     // -> tilingSet
        int w = p.getTileWidth();      // ok in tilingSet

        // --- Compression axis: still in startCompression, advance now ---
        p.setCompressionMode(2);       // -> compressionExplicit
        p.setCompressionType("JPEG");  // -> compressionSet
        String t = p.getCompressionType();

        // Order between axes doesn't matter — the two state machines
        // are orthogonal. Each method only constrains its own axis.
    }
}
