package ru.iu3.effect;

import java.util.concurrent.Callable;

public class Vibrato extends Effect implements Callable<short[]> {
    private static final double RATIO_DRY_TO_WET = 0.5; // Соотношение исходного и обработанного сигнала
    public static final double DEFAULT_FREQUENCY = 5.0; // Частота модуляции в Гц
    public static final double DEFAULT_DEPTH_MS = 5.0; // Глубина модуляции в мс
    private final BufferSample delayBuffer;
    private final double sampleRate;
    private final double depthSamples;
    private int sampleIndex;

    public Vibrato(int sampleRate) {
        super();
        this.sampleRate = sampleRate;
        this.depthSamples = (sampleRate * DEFAULT_DEPTH_MS) / 1000.0;
        int bufferSize = (int) (2 * this.depthSamples); // Буфер должен быть больше максимальной задержки
        this.delayBuffer = new BufferSample(bufferSize);
        this.sampleIndex = 0;
    }

    public void setInputAudioStream(short[] inputStream) {
        this.inputAudioStream = inputStream;
    }

    private int getModulatedDelay() {
        double modulation = Math.sin(2 * Math.PI * DEFAULT_FREQUENCY * sampleIndex / sampleRate);
        return (int) (depthSamples * (modulation + 1) / 2); // Преобразование [-1,1] в [0, depthSamples]
    }

    @Override
    public synchronized short[] createEffect() {
        for (int j = 0; j < inputAudioStream.length; j++) {
            int modulatedDelay = getModulatedDelay();
            int indexCurrentSample = delayBuffer.getIndexCurrentElement();

            double delayedSample = delayBuffer.getAmplitudeSampleDelay(indexCurrentSample, modulatedDelay);

            this.inputAudioStream[j] = (short) ((RATIO_DRY_TO_WET * this.inputAudioStream[j]) +
                    ((1 - RATIO_DRY_TO_WET) * delayedSample));

            delayBuffer.add(new short[]{this.inputAudioStream[j]});
            sampleIndex++;
        }
        return this.inputAudioStream;
    }

    @Override
    public short[] call() {
        return createEffect();
    }
}
