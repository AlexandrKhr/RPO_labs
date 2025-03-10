package ru.iu3.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.iu3.player.AudioPlayerFirst;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FxmlControllerFirst implements Initializable {

    private AudioPlayerFirst audioPlayer;
    private Thread playThread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void open(){
        //Выбор файлов формата wav
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Audio Files", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile == null){
            return;
        }

        this.audioPlayer = new AudioPlayerFirst(selectedFile);
        playThread = new Thread(() -> {
            System.out.println("PLAY");
            this.audioPlayer.play();
        });
        playThread.start();
    }

    @FXML
    private void play(){
        System.out.println("PLAY");
        if (this.audioPlayer != null){
            if (this.audioPlayer.isStopped()){
                this.audioPlayer.setStopped(false);
                playThread = new Thread(() -> this.audioPlayer.play());
                playThread.start();
            }
            else {
                this.audioPlayer.setPaused(false);
                this.audioPlayer.resume();
            }
        }
    }

    @FXML
    private void pause(){
        System.out.println("PAUSE");
        if (this.audioPlayer != null){
            this.audioPlayer.setPaused(true);
        }
    }

    @FXML
    private void stop(){
        System.out.println("STOP");
        if (this.audioPlayer != null){
            this.audioPlayer.setStopped(true);
        }
    }

    @FXML
    private void clickClose(){
        if (this.audioPlayer != null){
            if (this.playThread != null){
                this.playThread.interrupt();
            }
            this.audioPlayer.closeResources();
        }

        System.exit(0);
    }
}