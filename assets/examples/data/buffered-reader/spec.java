import liquidjava.specification.*;
import java.io.BufferedReader;
import java.io.Reader;

@RefinementAlias("NonNegative(int v) { v >= 0 }")
@RefinementAlias("Positive(int v) { v > 0 }")
@StateSet({"open", "marked", "closed"})
@ExternalRefinementsFor("java.io.BufferedReader")
public interface BufferedReaderRefinementsExpert {

    @StateRefinement(to = "open(this)")
    public void BufferedReader(Reader in);

    @StateRefinement(to = "open(this)")
    public void BufferedReader(Reader in, @Refinement("Positive(_)") int sz);

    @StateRefinement(from = "open(this)")
    @StateRefinement(from = "marked(this)")
    public int read();

    @StateRefinement(from = "open(this)", to = "marked(this)")
    @StateRefinement(from = "marked(this)")
    public void mark(@Refinement("NonNegative(_)") int readAheadLimit);

    @StateRefinement(from = "marked(this)", to = "open(this)")
    public void reset();

    @StateRefinement(from = "!closed(this)", to = "closed(this)")
    public void close();
}
