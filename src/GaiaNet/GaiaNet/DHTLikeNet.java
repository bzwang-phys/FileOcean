package GaiaNet.GaiaNet;

import GaiaNet.NetTransfer.GaiaType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DHTLikeNet {
    public Node node = null;

    public DHTLikeNet(Node node){
        this.node = node;
    }

    public int joinNet(){
        for (String serverIP : node.serverIPList) {
            boolean flag = true;
            try {
                Socket sock = new Socket(serverIP, 9190);
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                DataInputStream dis = new DataInputStream(sock.getInputStream());
                dos.writeInt(GaiaType.GaiaNet.ordinal());
                dos.writeInt(NetRequest.JoinNet.ordinal());
                dos.writeUTF(node.toTxtForm());
                dos.flush();
                String response = dis.readUTF();
                node.buildFromTxt(response);
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            }
            if (flag) break;
        }

        return 0; // normal
    }

    public void Config(){ }

    public void handle(Socket socket){
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int flag = dis.readInt();
            NetRequest type = NetRequest.values()[flag];

            if (type == NetRequest.JoinNet) joinNetResponse(socket, dis, dos);
            else if (type == NetRequest.LeaveNet);
            else if (type == NetRequest.NearestNodes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinNetResponse(Socket socket, DataInputStream dis, DataOutputStream dos) throws IOException {
        String nodeInfo = dis.readUTF();
        Node nodeExt = new Node(nodeInfo);
        boolean isMyneigh = isMyNeighbour(nodeExt);
        if (isMyneigh) joinMyNeighbourList(nodeExt);
        String mynearestNodes =  getMyNearestNodes();
        dos.writeUTF(mynearestNodes);
    }

    private void joinMyNeighbourList(Node node) {
    }

    private boolean isMyNeighbour(Node node) {
        return false;
    }

    private String getMyNearestNodes() {
        return null;
    }


    public int distance(Node node1, Node node2){
        return 0;
    }

    public int leaveNet(){
        return 0;
    }

    public void nearest(Node node) {

    }
}
