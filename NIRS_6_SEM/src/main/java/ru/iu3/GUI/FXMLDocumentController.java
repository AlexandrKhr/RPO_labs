package ru.iu3.GUI;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.iu3.player.AudioPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

    private AudioPlayer audioPlayer;
    private Thread playThread;
    @FXML
    private Slider Slider0, Slider1, Slider2, Slider3, Slider4, Slider5, Slider6, Slider7, soundSlider;
    @FXML
    private Label Label0, Label1, Label2, Label3, Label4, Label5, Label6, Label7;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.listenSliders();
        this.gainFromSlider();
    }

    @FXML
    private void open() {
        //Выбор файлов формата wav
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile == null) return;

        this.audioPlayer = new AudioPlayer(selectedFile);
        playThread = new Thread(() -> {
            System.out.println("PLAY");
            this.resetSliders();
            this.audioPlayer.play();
        });
        playThread.start();
    }

    @FXML
    private void play() {
        System.out.println("PLAY");
        if (this.audioPlayer != null) {
            if (this.audioPlayer.getStopStatus()) {
                playThread = new Thread(() -> {
                    this.audioPlayer.play();
                });
                playThread.start();
            } else
                this.audioPlayer.setPauseStatus(false);
        }
    }

    @FXML
    private void pause() {
        System.out.println("PAUSE");
        if (this.audioPlayer != null)
            this.audioPlayer.setPauseStatus(true);
    }

    @FXML
    private void stop() {
        System.out.println("STOP");
        if (this.audioPlayer != null)
            this.audioPlayer.setStopStatus(true);
        resetSliders();
    }

    @FXML
    private void clickClose() {
        if (this.audioPlayer != null) {
            if (this.playThread != null)
                this.playThread.interrupt();
            this.audioPlayer.close();
        }

        System.exit(0);
    }

    @FXML
    private void VibratoBox() {
        System.out.println("Вибрато on/off");
        if (!this.audioPlayer.VibratoIsActive())
            this.audioPlayer.setVibrato(true);
        else
            this.audioPlayer.setVibrato(false);
    }

    @FXML
    private void ChorusBox() {
        System.out.println("Хор on/off");
        if (!this.audioPlayer.ChorusIsActive())
            this.audioPlayer.setChorus(true);
        else
            this.audioPlayer.setChorus(false);
    }

    @FXML
    private void IirBox() {
        System.out.println("Change Filter");
        audioPlayer.setIirEnabled(!audioPlayer.isIirEnabled());
        resetSlidersToDefault(); // Сброс слайдеров к начальным значениям
    }

    private void resetSlidersToDefault() {
        Platform.runLater(() -> {
            Slider0.setValue(1);
            Slider1.setValue(1);
            Slider2.setValue(1);
            Slider3.setValue(1);
            Slider4.setValue(1);
            Slider5.setValue(1);
            Slider6.setValue(1);
            Slider7.setValue(1);
            updateFilters(); // Обновить значения фильтров
        });
    }

    private void updateFilters() {
        // Обновление усиления для всех фильтров в соответствии со значениями слайдеров
        audioPlayer.getEqualizer().getFilter(0).setGain(Slider0.getValue());
        audioPlayer.getEqualizer().getFilter(1).setGain(Slider1.getValue());
        audioPlayer.getEqualizer().getFilter(2).setGain(Slider2.getValue());
        audioPlayer.getEqualizer().getFilter(3).setGain(Slider3.getValue());
        audioPlayer.getEqualizer().getFilter(4).setGain(Slider4.getValue());
        audioPlayer.getEqualizer().getFilter(5).setGain(Slider5.getValue());
        audioPlayer.getEqualizer().getFilter(6).setGain(Slider6.getValue());
        audioPlayer.getEqualizer().getFilter(7).setGain(Slider7.getValue());
    }

    private void resetSliders() {
        Platform.runLater(() -> {
            Slider0.setValue(1);
            Slider1.setValue(1);
            Slider2.setValue(1);
            Slider3.setValue(1);
            Slider4.setValue(1);
            Slider5.setValue(1);
            Slider6.setValue(1);
            Slider7.setValue(1);
            soundSlider.setValue(1);
        });
    }

    private void gainFromSlider() {
        soundSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            audioPlayer.setGain(newValue.doubleValue());
        });
    }

    private void listenSliders() {
        Slider0.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label0.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(0).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(0).setGain(newValue.doubleValue());
            }
        });

        Slider1.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label1.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(1).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(1).setGain(newValue.doubleValue());
            }
        });

        Slider2.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label2.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(2).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(2).setGain(newValue.doubleValue());
            }
        });

        Slider3.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label3.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(3).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(3).setGain(newValue.doubleValue());
            }
        });

        Slider4.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label4.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(4).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(4).setGain(newValue.doubleValue());
            }
        });

        Slider5.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label5.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(5).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(5).setGain(newValue.doubleValue());
            }
        });

        Slider6.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label6.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(6).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(6).setGain(newValue.doubleValue());
            }
        });

        Slider7.valueProperty().addListener((observable, oldValue, newValue) -> {
            String str = String.format("%.3f", (newValue.doubleValue()));
            Label7.setText(str);
            if (!AudioPlayer.isIirEnabled) {
                audioPlayer.getEqualizer().getFilter(7).setGain(newValue.doubleValue());
            } else {
                audioPlayer.getEqualizer().getFilterIir(7).setGain(newValue.doubleValue());
            }
        });
    }
}
