package GaiaNet.GaiaNet;

public class Net {
    public DHTLikeNet net;
    public Node node;

    public Net(){
        this.node = new Node("local");
        this.net = new DHTLikeNet(this.node);
    }

}
