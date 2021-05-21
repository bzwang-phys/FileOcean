package GaiaNet.NetTransfer;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class CommandHandler {

    public int sendCmd(String cmd){
        cmdParse(cmd);
        return 0; // normal
    }


    public void handle(String s){
        Command cmds = cmdParse(s);
        if ("send".equals(cmds.action)){
            String ip = cmds.to;
            Integer port = 9190;
            String fname = cmds.argList.get(0);
            SendFileClient client = new SendFileClient(ip, port);
            client.sendFile(fname);
        } else if ("exit".equals(cmds.action)){
            System.exit(0);
        } else if ("pwd".equals(cmds.action)){
            System.out.println(System.getProperty("user.dir"));
        } else if ("test".equals(cmds.action)){
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



    private Command cmdParse(String cmd){
        Command command = new Command();
        String[] cmdary = cmd.split(" ");
        int index = 0;
        if ("to".equalsIgnoreCase(cmdary[0])){
            command.to = cmdary[1];
            command.action = cmdary[2];
            index = 3;
        }else {
            command.action = cmdary[0];
            index = 1;
        }

        while (index < cmdary.length){
            if (cmdary[index].contains("--")){
                // Long Parameters Parse
                String key = cmdary[index].substring(2);
                String value = "";
                if ((index+1 < cmdary.length) && !cmdary[index+1].contains("-")){
                    value = cmdary[index+1];
                    index += 2;
                } else {
                    index += 1;
                }
                command.longOption.put(key, value);
            } else if (cmdary[index].contains("-")){
                // Short Parameters Parse
                char[] keys = cmdary[index].substring(1).toCharArray();  //maybe like this: -cpt
                String value = "";
                if ((index+1 < cmdary.length) && !cmdary[index+1].contains("-")){
                    value = cmdary[index+1];
                    index += 2;
                } else {
                    index += 1;
                }

                for (char key : keys) {
                    command.shortOption.put(String.valueOf(key), value);
                }
            } else {
                // Position Parameters Parse
                command.argList.add(cmdary[index]);
                index += 1;
            }
        }
        return command;
    }

}
