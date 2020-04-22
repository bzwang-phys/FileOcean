package FileOcean.Windows;

import FileOcean.Common.Files;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CopyController {

    public TextField textFieldSource;
    public TextField textFieldTarget;
    public ProgressBar progressCopy;

    public void button1Click() throws InterruptedException {
        textFieldSource.setText("F:\\迅雷下载\\闻香识女人BD国英音轨中英双字1024高清.mkv");
        textFieldTarget.setText("F:\\迅雷下载\\1.mkv");
        String pathSource = textFieldSource.getText();
        String pathTarget = textFieldTarget.getText();

        new Thread(()-> {
            try {
                Files.copy(pathSource, pathTarget);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        while (true){
            Thread.sleep(1000);
            progressCopy.setProgress(1.0*Files.progress/100);
            if (Files.progress == 100){
                break;
            }
        }



    }
}
