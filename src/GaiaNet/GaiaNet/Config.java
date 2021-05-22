package GaiaNet.GaiaNet;

import java.io.*;

public class Config {
    public String configFile;
    public String nodeName;

    public Config(String s){
        try {
            this.configFile = s;
            readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config(){
        try {
            this.configFile = "NodeConfig.txt";
            readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readConfig() throws IOException {
        File file = new File(this.configFile);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufReader = new BufferedReader(isr);
        String line;
        while ((line=bufReader.readLine()) != null){
            parse(line);
        }
        return "";
    }

    public void parse(String line){
        line.split()
        if ()

    }
}
