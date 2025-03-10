package ru.iu3.player;

import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import ru.iu3.effect.Chorus;
import ru.iu3.effect.Vibrato;
import ru.iu3.equalizer.Equalizer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;

public class AudioPlayer {

    private final File currentMusicFile;
    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    public static final int BUFF_SIZE = 100000;
    private final byte[] bufferBytes = new byte[BUFF_SIZE];
    private short[] bufferShort = new short[BUFF_SIZE / 2];
    private boolean pauseStatus;
    private boolean stopStatus;
    private double gain;
    private final Equalizer equalizer;
    private final Chorus chorus;
    private final Vibrato vibrato;
    private boolean isChorus;
    private boolean isVibrato;
    private boolean vibratoEnabled = false;
    private boolean chorusEnabled = false;
    public static boolean isIirEnabled = false;
    private static final int sampleRate = 44000;

    public void setIirEnabled(boolean iirEnabled){
        isIirEnabled = iirEnabled;
    }

    public boolean isIirEnabled(){
        return isIirEnabled;
    }

    public AudioPlayer(File musicFile) {
        this.currentMusicFile = musicFile;
        this.equalizer = new Equalizer();
        this.gain = 1.0;
        this.chorus = new Chorus(sampleRate);
        this.vibrato = new Vibrato(sampleRate);
        this.isChorus = false;
        this.isVibrato = false;
    }

    public Chorus getChorus(){
        return chorus;
    }

    public Vibrato getVibrato(){
        return vibrato;
    }

    private void Vibrato(short[] inputSamples) throws ExecutionException, InterruptedException{
        this.vibrato.setInputAudioStream(inputSamples);
        this.vibrato.createEffect();
    }

    private void Chorus(short[] inputSamples) throws ExecutionException, InterruptedException{
        this.chorus.setInputAudioStream(inputSamples);
        this.chorus.createEffect();
    }

    public boolean VibratoIsActive(){
        return this.isVibrato;
    }

    public void setVibrato(boolean b){
        this.isVibrato = b;
    }

    public boolean ChorusIsActive(){
        return this.isChorus;
    }

    public void setChorus(boolean chorusEnabled){
        this.isChorus = chorusEnabled;
    }

    public void play() {
        try {
            this.audioStream = AudioSystem.getAudioInputStream(currentMusicFile);
            AudioFormat audioFormat = audioStream.getFormat();
            this.sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            this.sourceDataLine.open(audioFormat);
            this.sourceDataLine.start();
            this.pauseStatus = false;
            this.stopStatus = false;

            while ((this.audioStream.read(this.bufferBytes) != -1)){
                this.ByteArrayToShortArray();

                if (this.pauseStatus){
                    this.pause();
                }
                if (this.stopStatus){
                    break;
                }

                equalizer.setInputSignal(this.bufferShort);
                this.equalizer.equalization();
                this.bufferShort = equalizer.getOutputSignal();

                if (chorusEnabled){
                    chorus.setInputAudioStream(this.bufferShort);
                    this.bufferShort = chorus.createEffect();
                }

                if (vibratoEnabled){
                    vibrato.setInputAudioStream(this.bufferShort);
                    this.bufferShort = vibrato.createEffect();
                }

                this.ShortArrayToByteArray();
                this.sourceDataLine.write(this.bufferBytes, 0, this.bufferBytes.length);
            }
            this.sourceDataLine.drain();
            this.sourceDataLine.close();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException | ExecutionException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private void pause(){
        if (this.pauseStatus){
            while (true){
                try {
                    if (!this.pauseStatus || this.stopStatus){
                        break;
                    }
                    Thread.sleep(50);
                }
                catch (InterruptedException ignored){

                }
            }
        }
    }

    public void setPauseStatus(boolean pauseStatus) {
        this.pauseStatus = pauseStatus;
    }

    public void setStopStatus(boolean stopStatus) {
        this.stopStatus = stopStatus;
    }

    public boolean getStopStatus() {
        return this.stopStatus;
    }


    public void close() {
        if (this.audioStream != null){
            try {
                this.audioStream.close();
            }
            catch (IOException e){

            }
        }
        if (this.sourceDataLine != null){
            this.sourceDataLine.close();
        }
    }

    private void ByteArrayToShortArray(){
        for (int i = 0, j = 0; i < this.bufferBytes.length; i += 2, j++){
            this.bufferShort[j] = (short) ((ByteBuffer.wrap(this.bufferBytes, i, 2).order(ByteOrder.LITTLE_ENDIAN)
                    .getShort() / 2) * this.gain);
        }
    }

    private void ShortArrayToByteArray(){
        for (int i = 0, j = 0; i < this.bufferShort.length && j < this.bufferBytes.length; i++, j += 2){
            ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(bufferShort[i]);
            this.bufferBytes[j] = buffer.array()[0];
            this.bufferBytes[j + 1] = buffer.array()[1];
        }
    }

    public void setGain(double gain){
        this.gain = gain;
    }

    public Equalizer getEqualizer(){
        return this.equalizer;
    }

    public void setDelayEnabled(boolean b) {
    }

    public boolean isDelayEnabled() {
        Boolean bool = true;
        return bool;
    }

    public boolean EnvelopeIsActive() {
        Boolean bool = true;
        return bool;
    }


}
