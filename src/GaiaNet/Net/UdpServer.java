package GaiaNet.Net;

//TODO:
// 1. Potential safety loophole: should check the data that would be written in.

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UdpServer {
    private int port;

    public UdpServer(int port){
        this.port = port;
    }

    private void writeFile(String s) throws IOException {
        File file = new File("UdpReceived.log");
        if (!file.exists()){
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(s);
        bw.close();
    }


//    receive the data and write in log.
    public void run() throws IOException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        byte[] buf = new byte[1024];
        DatagramSocket ds = new DatagramSocket(9392);
        DatagramPacket dpReceive = new DatagramPacket(buf, buf.length);
        System.out.println("server is on.");
        while (true){
            ds.receive(dpReceive);
            String clientInfo = dpReceive.getAddress().toString() +":" + dpReceive.getPort();
            Date dNow = new Date();
            String timeNow = "[ " + ft.format(dNow) + " ] ";
            writeFile(timeNow + " From: " + clientInfo);
            String dataReceive = new String(dpReceive.getData(), 0, dpReceive.getLength(), StandardCharsets.UTF_8);
            writeFile("  Data: " + dataReceive + System.lineSeparator());
        }
//        ds.close();

    }
}
