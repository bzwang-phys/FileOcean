package FileOcean.NetTransfer;

import FileOcean.Common.FileSegment;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Client {
    private String ip;
    private int port;
    private String fileName;
    private String filePath;
    private FileSegment fsg;
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
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
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    static class NetTransferInfo{
        long start;
        long end;
        long index;
        public NetTransferInfo(long start, long end, long index) {
            this.start = start;
            this.end = end;
            this.index = index;
        }
    }

    public void sendFile(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        File file = new File(filePath);
        if (!file.exists()){
            throw new FileNotFoundException();
        }
        this.fileName = file.getName();
        this.fsg = new FileSegment(filePath, FileSegment.Flag.NET);
        for (int blockIndex = 0; blockIndex < fsg.getBlockNum(); blockIndex++) {
            NetTransferInfo netTransferInfo = new NetTransferInfo(
                    blockIndex * fsg.getBlockSize(),
                    (blockIndex + 1) * fsg.getBlockSize(), 0);
            threadPool.execute(() -> runSend(netTransferInfo));
        }

    }

    private void runSend(NetTransferInfo netTransferInfo) {
        long start = netTransferInfo.start;
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            RandomAccessFile fis = new RandomAccessFile(filePath, "r");
            dos.writeUTF(fileName);
            dos.flush();
            dos.writeLong(netTransferInfo.start);
            dos.flush();
            dos.writeLong(netTransferInfo.end);
            dos.flush();
            dos.writeLong(netTransferInfo.index);
            dos.flush();

            fis.seek(netTransferInfo.start);
            long blockSize = 500L * 1024;   //500Kb
            byte[] data = new byte[(int) blockSize];
            while (true) {
                if (start + blockSize >= netTransferInfo.end) {
                    blockSize = netTransferInfo.end - start;
                }
                // read data
                rwLock.readLock().lock();
                try {
                    fis.seek(netTransferInfo.start);
                    fis.read(data, 0, (int) blockSize);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rwLock.readLock().unlock();
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
