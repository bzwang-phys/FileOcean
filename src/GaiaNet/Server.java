package GaiaNet;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private int port;
    private String basePath;
    ServerSocket sSocket;
    Hashtable<Integer, ReadWriteLock>  rwLocks = new Hashtable<>();;

    public String getBasePath() {
        return basePath;
    }
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Server(int port) {
        this.port = port;
        try {
            sSocket = new ServerSocket(port);
            basePath = System.getProperty("user.dir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverRun(){
        int flag;
        while (true){
            try {
                Socket socket = sSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                flag = dis.readInt();
                switch (flag){
                    case 0:         // Message of file
                        break;
                    case 1:         // send file with child thread
                        new Thread().start();
                        break;
                    default:
                        System.out.println("Unknown Socket.");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
