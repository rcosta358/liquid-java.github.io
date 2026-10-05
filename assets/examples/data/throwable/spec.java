import liquidjava.specification.*;

@StateSet({"withThrowable", "noThrowable"})
@ExternalRefinementsFor("java.lang.Throwable")
public interface ThrowableRefinementsExpert {

    @StateRefinement(to = "noThrowable(this)")
    public void Throwable();

    @StateRefinement(to = "noThrowable(this)")
    public void Throwable(String message);

    @StateRefinement(to = "withThrowable(this)")
    public void Throwable(Throwable cause);

    @StateRefinement(to = "withThrowable(this)")
    public void Throwable(String message, Throwable cause);

    @StateRefinement(from = "noThrowable(this)", to = "withThrowable(this)")
    public Throwable initCause(Throwable cause);
}
