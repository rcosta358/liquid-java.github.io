
class Example {
    static void example(Throwable cause, Throwable cause2) throws Exception {
        // Path: noThrowable -> withThrowable
        Throwable t = new Throwable();
        t.initCause(cause);
    }
}
