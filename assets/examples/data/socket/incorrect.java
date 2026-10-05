import java.net.Socket;
import java.net.SocketAddress;

class Example {
    static void example(SocketAddress addr, SocketAddress endpoint) throws Exception {
        // Violation: setReuseAddress() is restricted to state `connected`,
        // but here it's called after shutdownInput() — i.e. in state `inputShutdown`.
        Socket s = new Socket();
        s.bind(addr);
        s.connect(endpoint);
        s.shutdownInput();
        s.setReuseAddress(true);   // INVALID: requires connected(s),
                                   //          but s is in state inputShutdown
    }
}
