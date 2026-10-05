import liquidjava.specification.*;
import java.util.UUID;

@ExternalRefinementsFor("java.util.UUID")
@StateSet({"dceSecurityNameRandom", "maybeTime", "timeBased"})
@RefinementAlias("Version(int v) { v >= 0 && v <= 4 }")
@RefinementAlias("Variant(int v) { v == 0 || v == 2 || v == 6 || v == 7 }")
public interface UUIDRefinementsExpert {

    // The version bits decide which state we land in.
    @StateRefinement(to = "((mostSigBits/4096) % 16 == 1)"
                       + " ? timeBased(this)"
                       + " : dceSecurityNameRandom(this)")
    void UUID(long mostSigBits, long leastSigBits);

    @Refinement("maybeTime(return)")
    UUID fromString(String name);

    @Refinement("dceSecurityNameRandom(return)")
    UUID randomUUID();

    @StateRefinement(from = "maybeTime(this)")
    @StateRefinement(from = "timeBased(this)")
    int clockSequence();

    @Refinement("Variant(_)")  int variant();
    @Refinement("Version(_)")  int version();
}
