import liquidjava.specification.*;
import javax.swing.undo.AbstractUndoableEdit;

@StateSet({"aliveDone", "aliveNotDone", "notAlive"})
@ExternalRefinementsFor("javax.swing.undo.AbstractUndoableEdit")
public interface AbstractUndoableEditRefinementsExpert {

    @StateRefinement(to = "aliveDone(this)")
    void AbstractUndoableEdit();

    @StateRefinement(from = "aliveNotDone(this)", to = "aliveDone(this)")
    void redo();

    @StateRefinement(from = "aliveDone(this)", to = "aliveNotDone(this)")
    void undo();

    @StateRefinement(from = "!notAlive(this)", to = "notAlive(this)")
    void die();
}
