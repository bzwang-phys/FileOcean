package GaiaNet.NetTransfer;

import java.util.Scanner;

//import GaiaNet.NetTransfer.*;

public class DaemonServer {
    private String ip;
    private int port;
    private int portServer = 9190;
    private MultiThreadServer server;

    /*
     * Prepare a server listening on poerServer,
     * And produce a commandline tool.
     */
    public DaemonServer(){
        this.server = new MultiThreadServer(portServer);
        new Thread(() -> this.server.serverRun()).start(); ;
        CommandLine();
    }

    public void CommandLine(){
        try {
            Scanner sc = new Scanner(System.in);
            String cmdstr;
            CommandHandler cmd = new CommandHandler();
            while (true) {
                System.out.print("GaiaNet :> ");
                cmdstr = sc.nextLine();
                cmd.sendCmd(cmdstr);
            }
        } catch (Exception e){

        }
    }


}

