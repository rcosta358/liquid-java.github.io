import javax.security.auth.callback.ChoiceCallback;

class Example {
    static void example() throws Exception {
        // Violation: last constructor argument is `false` -> state `single`,
        // where setSelectedIndexes() is forbidden.
        ChoiceCallback cb = new ChoiceCallback(
            "Pick one", new String[]{"a", "b"}, 0, false);
        cb.setSelectedIndexes(new int[]{0});   // INVALID: requires multiple(cb),
                                               //          but cb is in state single
    }
}
