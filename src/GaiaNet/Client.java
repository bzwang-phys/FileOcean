package GaiaNet;

import GaiaNet.Common.FileSegment;

import java.io.*;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Client {
    private String ip;
    private int port;
    private Socket clientSock;
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private Hashtable<Integer, ReadWriteLock>  rwLocks = new Hashtable<>();

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            clientSock = new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class NetTransferInfo{
        String filePath;
        String fileName;
        long size;
        long start;
        long end;
        long index;
        public NetTransferInfo(String filePath,String fileName,long size,long start,long end,long index) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.size = size;
            this.start = start;
            this.end = end;
            this.index = index;
        }
    }

    public void sendFile(String filePath){
        try {
            File file = new File(filePath);
            if (!file.exists()){
                throw new FileNotFoundException();
            }
            rwLocks.put(filePath.hashCode(), new ReentrantReadWriteLock());
            String fileName = file.getName();
            FileSegment fsg = new FileSegment(filePath, FileSegment.Flag.NET);
            //fsg.setThreadNum(1);
            DataOutputStream dosSocket = new DataOutputStream(clientSock.getOutputStream());
            dosSocket.writeInt(0);   // Message of file
            dosSocket.flush();
            dosSocket.writeUTF(fileName);
            dosSocket.flush();
            dosSocket.writeLong(fsg.getSize());
            dosSocket.flush();
            System.out.printf("blockNum: %d, blockSize: %d",fsg.getBlockNum(),fsg.getBlockSize());
            for (int blockIndex = 0; blockIndex < fsg.getBlockNum(); blockIndex++) {
                NetTransferInfo netTransferInfo = new NetTransferInfo(filePath,fileName,
                        fsg.getSize(),
                        blockIndex * fsg.getBlockSize(),
                        (blockIndex + 1) * fsg.getBlockSize(),
                        0);
                threadPool.execute(() -> runSend(netTransferInfo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runSend(NetTransferInfo netTransferInfo) {
        long start = netTransferInfo.start;
        if (start > netTransferInfo.size-1){    // 0 <= start <= size-1
            // Solve the problem that blockNum/threadNum may be large than ...
            return;
        }
        if (netTransferInfo.end > netTransferInfo.size){ // 1 <= end <= size
            netTransferInfo.end = netTransferInfo.size;
        }
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            RandomAccessFile fis = new RandomAccessFile(netTransferInfo.filePath, "r");
            dos.writeInt(1);   // Child thread to send file.
            dos.flush();
            dos.writeUTF(netTransferInfo.fileName);
            dos.flush();
            dos.writeLong(netTransferInfo.start);
            dos.flush();
            dos.writeLong(netTransferInfo.end);
            dos.flush();
            dos.writeLong(netTransferInfo.index);
            dos.flush();
            fis.seek(start);
            long blockSize = 500L * 1024;   //500Kb
            byte[] data = new byte[(int) blockSize];
            while (true) {
                if (start + blockSize >= netTransferInfo.end) {
                    blockSize = netTransferInfo.end - start;
                }
                // read data
                rwLocks.get(netTransferInfo.filePath.hashCode()).readLock().lock();
                try {
                    fis.seek(start);
                    fis.read(data, 0, (int) blockSize);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rwLocks.get(netTransferInfo.filePath.hashCode()).readLock().unlock();
                }

                // write data
                dos.write(data, 0, (int) blockSize);

                start += blockSize;
                if (start >= netTransferInfo.end) {
                    dos.close();
                    fis.close();
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }



}
