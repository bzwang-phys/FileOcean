package GaiaNet.NetTransfer;

// 断点传输
// javafx 多线程
// 检查文件完整性

public class ClientTest {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9190);
        client.sendFile("F:\\量子多体理论（文小刚）.pdf");
/*        FileSegment fsg = new FileSegment("F:\\books(cs)\\C#高级编程（第9版）.pdf", FileSegment.Flag.NET);

        System.out.println(fsg.getSize());
        System.out.println(fsg.getBlockSize());
        System.out.println(fsg.getBlockNum());*/
    }

}
