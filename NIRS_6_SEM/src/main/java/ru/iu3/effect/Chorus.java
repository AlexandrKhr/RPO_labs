package ru.iu3.effect;

import java.util.concurrent.Callable;

public class Chorus extends Effect implements Callable<short[]> {
    private static final double RATIO_DRY_TO_WET = 0.4; // Соотношение исходного и обработанного сигнала
    public static final int DEFAULT_DELAY_1 = 20; // Задержка первого блока в мс
    public static final int DEFAULT_DELAY_2 = 40; // Задержка второго блока в мс
    public static final double DEFAULT_GAIN_1 = 0.5; // Усиление первого блока
    public static final double DEFAULT_GAIN_2 = 0.5; // Усиление второго блока
    private final BufferSample buffer1;
    private final BufferSample buffer2;
    private final double gain1;
    private final double gain2;

    public Chorus(int sampleRate) {
        super();
        int sizeBuffer1 = (int) (sampleRate * DEFAULT_DELAY_1 / 1000.0);
        int sizeBuffer2 = (int) (sampleRate * DEFAULT_DELAY_2 / 1000.0);
        this.buffer1 = new BufferSample(sizeBuffer1);
        this.buffer2 = new BufferSample(sizeBuffer2);
        this.gain1 = DEFAULT_GAIN_1;
        this.gain2 = DEFAULT_GAIN_2;
    }

    public void setInputAudioStream(short[] inputStream) {
        this.inputAudioStream = inputStream;
    }

    @Override
    public synchronized short[] createEffect() {
        int indexCurrentSample1 = this.buffer1.getIndexCurrentElement();
        int indexCurrentSample2 = this.buffer2.getIndexCurrentElement();

        for (int j = 0; j < inputAudioStream.length; j++) {
            double delayedSample1 = this.buffer1.getAmplitudeSampleDelay(indexCurrentSample1, j) * gain1;
            double delayedSample2 = this.buffer2.getAmplitudeSampleDelay(indexCurrentSample2, j) * gain2;

            this.inputAudioStream[j] = (short) ((RATIO_DRY_TO_WET * this.inputAudioStream[j]) +
                    ((1 - RATIO_DRY_TO_WET) * (delayedSample1 + delayedSample2)));

        }
        this.buffer1.add(this.inputAudioStream);
        this.buffer2.add(this.inputAudioStream);
        return this.inputAudioStream;
    }

    @Override
    public short[] call() {
        return createEffect();
    }
}
