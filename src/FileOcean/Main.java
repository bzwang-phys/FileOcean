package FileOcean;

import FileOcean.Common.*;
import FileOcean.NetTransfer.Client;

import java.io.IOException;
import java.io.RandomAccessFile;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String s = "F:\\迅雷下载\\闻香识女人BD国英音轨中英双字1024高清.mkv";
        String t = "F:\\迅雷下载\\2.mkv";
        Client client = new Client("127.0.0.1", 9000);
        client.sendFile(s);
        //Files.copy(s,t);
/*        RandomAccessFile rw = new RandomAccessFile("test.txt", "rw");
        rw.seek(0);
        rw.writeBytes("abcdefghijklm");
        rw.close();*/
/*        RandomAccessFile r = new RandomAccessFile("test.txt", "r");
        r.seek(2);
        byte[] byt = new byte[4];
        r.read(byt,0,0);

        System.out.println(new String(byt));*/
    }
}
