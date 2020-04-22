package FileOcean.NetTransfer;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class MultiThreadServer {
    private int port;
    public String basePath;
    ServerSocket sSocket;

    public MultiThreadServer(int port) {
        this.port = port;
        try {
            sSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverRun(){
        while (true){
            try {
                Socket socket = sSocket.accept();
                System.out.println("New Connection from: "+socket.getInetAddress()+", port: "+socket.getPort());
                new Thread(() -> threadRun(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void threadRun(Socket socket) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String fileName = dis.readUTF();
            Path filePath = Path.of(basePath, fileName);
            System.out.println(filePath);
            File file = filePath.toFile();
            if (!file.exists()){
                throw new IOException("File is not created!!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
