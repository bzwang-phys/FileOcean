package GaiaNet.NetTransfer;

import java.io.*;
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
                        newFile(socket, dis);
                        break;
                    case 1:         // send file with child thread
                        new Thread(() -> threadRun(socket, dis)).start();
                        break;
                    default:
                        System.out.println("Unknown Socket.");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void threadRun(Socket socket, DataInputStream dis) {
        int len;
        try {
            String fileName = dis.readUTF();
            Path filePath = Path.of(basePath, fileName);
            File file = filePath.toFile();
            if (!file.exists()){
                throw new IOException("File is not created!!");
            }

            long start = dis.readLong();
            long end = dis.readLong();
            long index = dis.readLong();
            System.out.println("thread: "+Thread.currentThread().getName()+", fileName: "+fileName);
            System.out.printf("start: %d, end: %d, index: %d\n", start,end,index);
            RandomAccessFile rw = new RandomAccessFile(file, "rw");
            byte[] byt = new byte[500 * 1024];
            while ((len = dis.read(byt,0, 500 * 1024))!=-1){
                rwLocks.get(file.toString().hashCode()).writeLock().lock();
                rw.seek(start+index);
                rw.write(byt,0, len);
                index += len;
                System.out.println(Thread.currentThread().getName()+" "+index+" "+end);
                rwLocks.get(file.toString().hashCode()).writeLock().unlock();
            };
            rw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newFile(Socket socket, DataInputStream dis){
        String fileName;
        try {
            fileName = dis.readUTF();
            long size = dis.readLong();
            File f = Path.of(basePath, fileName).toFile();
            System.out.println(fileName);
            System.out.println(size);
            if ( f.exists() && (f.length()==size) ){
                //  read configuration
            }else {
                RandomAccessFile rw = new RandomAccessFile(f, "rw");
                rw.setLength(size);
            }
            rwLocks.put(f.toString().hashCode(), new ReentrantReadWriteLock());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
