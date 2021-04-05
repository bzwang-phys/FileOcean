package GaiaNet.NetTransfer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CommandHandler {
    public void handle(String s){
        String[] cmdstrs = s.split(" ");
        if ("send".equals(cmdstrs[0])){
            String ip = cmdstrs[1];
            Integer port = 9190;
            String fname = cmdstrs[2];
            SendFileClient client = new SendFileClient(ip, port);
            client.sendFile(fname);
        } else if ("exit".equals(cmdstrs[0])){
            System.exit(0);
        } else if ("pwd".equals(cmdstrs[0])){
            System.out.println(System.getProperty("user.dir"));
        } else if ("test".equals(cmdstrs[0])){
            test();
        }
    }

    public void test(){
        try {
            while (true) {
                Socket socket = new Socket("114.214.205.186", 9190);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Scanner sc = new Scanner(System.in);
                dos.writeInt(100);   // Child thread to send file.
                dos.flush();

                Integer x = sc.nextInt();
                dos.writeLong(x);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
