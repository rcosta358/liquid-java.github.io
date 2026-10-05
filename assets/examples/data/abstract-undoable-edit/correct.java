import javax.swing.undo.AbstractUndoableEdit;

class Example {
    static void example() throws Exception {
        // Path: aliveDone -> aliveNotDone -> aliveDone -> aliveNotDone -> aliveDone
        AbstractUndoableEdit edit = new AbstractUndoableEdit();
        edit.undo();
        edit.redo();
        edit.undo();
        edit.redo();
    }
}
