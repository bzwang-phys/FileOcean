package GaiaNet.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {
        MulticastSocket multicastSocket = new MulticastSocket(9091);
        multicastSocket.setTimeToLive(50);
    }
}
