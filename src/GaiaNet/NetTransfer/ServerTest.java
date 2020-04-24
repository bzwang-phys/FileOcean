package GaiaNet.NetTransfer;

public class ServerTest {
    public static void main(String[] args) {
        Server server = new Server(9190);
        server.serverRun();
    }
}
