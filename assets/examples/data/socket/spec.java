import liquidjava.specification.*;
import java.net.Socket;
import java.net.SocketAddress;

@ExternalRefinementsFor("java.net.Socket")
@RefinementAlias("Port(int x) { x >= 0 && x <= 65535 }")
@StateSet({"unconnected", "bound", "connected",
           "inputShutdown", "outputShutdown", "bothShutdown", "closed"})
interface SocketRefinementsExpert {

    @StateRefinement(to = "unconnected(this)")
    void Socket();

    @StateRefinement(to = "connected(this)")
    void Socket(String host, @Refinement("Port(_)") int port);

    @StateRefinement(from = "unconnected(this)", to = "bound(this)")
    void bind(SocketAddress bindpoint);

    @StateRefinement(from = "bound(this)", to = "connected(this)")
    void connect(SocketAddress endpoint);

    @StateRefinement(from = "connected(this)",     to = "inputShutdown(this)")
    @StateRefinement(from = "outputShutdown(this)", to = "bothShutdown(this)")
    public void shutdownInput();

    @StateRefinement(from = "connected(this)",    to = "outputShutdown(this)")
    @StateRefinement(from = "inputShutdown(this)", to = "bothShutdown(this)")
    public void shutdownOutput();

    @StateRefinement(from = "connected(this)")
    public void setReuseAddress(boolean on);

    @StateRefinement(from = "!closed(this)", to = "closed(this)")
    public void close();

    // ... ~30 more methods, mostly safe in any non-closed state
}
