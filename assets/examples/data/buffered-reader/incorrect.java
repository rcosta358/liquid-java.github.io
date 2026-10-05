import java.io.BufferedReader;
import java.io.Reader;

class Example {
    static void example(Reader in) throws Exception {
        // Violation: reset() called twice — second call is in state `open`.
        BufferedReader br = new BufferedReader(in);
        br.mark(42);
        br.read();
        br.reset();
        br.reset();   // INVALID: reset() requires marked(br),
                      //          but br is in state open
    }
}
