package GaiaNet.Net;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;

public class TcpClient {
    private String ip;
    private int port;
    private Socket socket;

    public TcpClient(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            this.socket = new Socket(ip, port);
            this.socket.setSoTimeout(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() throws IOException {
        BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
        PrintStream outStream = new PrintStream(this.socket.getOutputStream());
        BufferedReader inStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

//      This part is used to send message
        while (true){
            System.out.println("message: >> ");
            String str = keyInput.readLine();
            if ("exit".equals(str)){
                break;
            } else {
                outStream.println(str);
                String inString = inStream.readLine();
                System.out.println(inString);
            }
        }

    }
}
