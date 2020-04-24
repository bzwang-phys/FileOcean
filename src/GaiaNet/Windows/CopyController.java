package GaiaNet.Windows;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class CopyController {

    public TextField textFieldSource;
    public TextField textFieldTarget;
    public ProgressBar progressCopy;

    public void button1Click() throws InterruptedException {
        textFieldSource.setText("F:\\迅雷下载\\闻香识女人BD国英音轨中英双字1024高清.mkv");
        textFieldTarget.setText("F:\\迅雷下载\\1.mkv");
        String pathSource = textFieldSource.getText();
        String pathTarget = textFieldTarget.getText();

        NewTask newTask = new NewTask();
        new Thread(newTask).start();

        progressCopy.progressProperty().bind(newTask.progressProperty());

        newTask.titleProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

            }
        });

    }
}


class NewTask extends Task<Number> {

    @Override
    protected Number call() throws Exception {
        this.updateTitle("Copying......");
        int j;
        for (j = 0; j < 1000; j++) {
            if (isCancelled()){
                updateMessage("Cancelled");
                break;
            }
            System.out.println("println: "+j);
            Thread.sleep(10);
            updateProgress(j, 1000);
            updateMessage("at: " + j);
        }
        return j;
    }
}