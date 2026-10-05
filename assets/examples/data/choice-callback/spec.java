import liquidjava.specification.*;
import javax.security.auth.callback.ChoiceCallback;

@StateSet({"single", "multiple"})
@ExternalRefinementsFor("javax.security.auth.callback.ChoiceCallback")
public interface ChoiceCallbackRefinementsExpert {

    @StateRefinement(to = "multipleSelectionsAllowed ? multiple(this) : single(this)")
    public void ChoiceCallback(String prompt, String[] choices,
                               int defaultChoice,
                               boolean multipleSelectionsAllowed);

    @StateRefinement(from = "multiple(this)", to = "multiple(this)")
    public void setSelectedIndexes(int[] selections);
}
