import java.io.BufferedReader;
import java.io.Reader;

class Example {
    static void example(Reader in) throws Exception {
        // Path: open -> open -> marked -> marked -> open -> open
        BufferedReader br = new BufferedReader(in);
        br.read();
        br.mark(42);
        br.read();
        br.reset();
        br.read();
    }
}
