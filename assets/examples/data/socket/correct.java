import java.net.Socket;
import java.net.SocketAddress;

class Example {
    static void example(SocketAddress addr, SocketAddress endpoint) throws Exception {
        // Path: unconnected -> bound -> connected -> inputShutdown -> bothShutdown
        Socket s = new Socket();
        s.bind(addr);
        s.connect(endpoint);
        s.shutdownInput();
        s.shutdownOutput();
        s.close();
    }
}
