import liquidjava.specification.*;
import javax.imageio.ImageWriteParam;
import java.util.Locale;

// Two orthogonal @StateSet declarations describe two independent
// state machines on the same object. The constructor places it in
// the start state of BOTH axes; methods only constrain one axis.

@StateSet({"startTiling", "tilingExplicit", "tilingSet"})
@StateSet({"startCompression", "compressionExplicit", "compressionSet"})
@ExternalRefinementsFor("javax.imageio.ImageWriteParam")
public interface ImageWriteParamsRefinementsExpert {

    @StateRefinement(to = "startTiling(this) && startCompression(this)")
    void ImageWriteParam(Locale locale);

    // ---- Tiling axis ----

    @StateRefinement(to = "mode == 2 ? tilingExplicit(this) : startTiling(this)")
    void setTilingMode(@Refinement("_ >= 0 && _ <= 3") int mode);

    @StateRefinement(from = "tilingExplicit(this)", to = "tilingSet(this)")
    @StateRefinement(from = "tilingSet(this)",       to = "tilingSet(this)")
    void setTiling(@Refinement("_ > 0") int tileWidth,
                   @Refinement("_ > 0") int tileHeight,
                   int xOff, int yOff);

    @StateRefinement(from = "tilingSet(this)") int getTileWidth();
    @StateRefinement(from = "tilingSet(this)") int getTileHeight();

    // ---- Compression axis ----

    @StateRefinement(to = "mode == 2 ? compressionExplicit(this) : startCompression(this)")
    void setCompressionMode(int mode);

    @StateRefinement(from = "compressionExplicit(this)",
                     to   = "compressionSet(this)")
    void setCompressionType(String type);

    @StateRefinement(from = "compressionExplicit(this)")
    @StateRefinement(from = "compressionSet(this)")
    String getCompressionType();
}
