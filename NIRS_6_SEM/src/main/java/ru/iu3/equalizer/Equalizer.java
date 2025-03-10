package ru.iu3.equalizer;

import ru.iu3.player.AudioPlayer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Equalizer {
    private short[] outputSignal;
    private Filter[] filters;
    private FilterIir[] filtersIir;
    private final static char COUNT_OF_THREADS = 4;
    ExecutorService pool;

    public Equalizer(){
        pool = Executors.newFixedThreadPool(COUNT_OF_THREADS);
        this.createFilters();
    }

    public void setInputSignal(short[] inputSignal){
        this.outputSignal = new short[inputSignal.length];
        this.filters[0].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_0);
        this.filters[1].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_1);
        this.filters[2].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_2);
        this.filters[3].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_3);
        this.filters[4].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_4);
        this.filters[5].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_5);
        this.filters[6].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_6);
        this.filters[7].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_7);
        this.filters[8].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_8);
        this.filters[9].settings(inputSignal, FilterInfo.COFFS_NUM_OF_BAND_9);
        this.filtersIir[0].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_0, FilterInfoIIR.COFFS_DEN_OF_BAND_0);
        this.filtersIir[1].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_1, FilterInfoIIR.COFFS_DEN_OF_BAND_1);
        this.filtersIir[2].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_2, FilterInfoIIR.COFFS_DEN_OF_BAND_2);
        this.filtersIir[3].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_3, FilterInfoIIR.COFFS_DEN_OF_BAND_3);
        this.filtersIir[4].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_4, FilterInfoIIR.COFFS_DEN_OF_BAND_4);
        this.filtersIir[5].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_5, FilterInfoIIR.COFFS_DEN_OF_BAND_5);
        this.filtersIir[6].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_6, FilterInfoIIR.COFFS_DEN_OF_BAND_6);
        this.filtersIir[7].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_7, FilterInfoIIR.COFFS_DEN_OF_BAND_7);
        this.filtersIir[8].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_8, FilterInfoIIR.COFFS_DEN_OF_BAND_8);
        this.filtersIir[9].settings(inputSignal, FilterInfoIIR.COFFS_NUM_OF_BAND_9, FilterInfoIIR.COFFS_DEN_OF_BAND_9);
    }

    private void createFilters(){
        this.filters = new Filter[FilterInfo.COUNT_OF_BANDS];
        for (int i = 0; i < FilterInfo.COUNT_OF_BANDS; i++)
            this.filters[i] = new Filter();
        for (int i = 0; i < FilterInfoIIR.COUNT_OF_BANDS; i++){
            this.filtersIir[i] = new FilterIir();
        }
    }

    public void equalization() throws InterruptedException, ExecutionException{
        Future<short[]>[] fs = new Future[FilterInfo.COUNT_OF_BANDS];

        for (int i = 0; i < FilterInfo.COUNT_OF_BANDS; i++){
            if (!AudioPlayer.isIirEnabled){
                fs[i] = pool.submit(this.filters[i]);
            }
            else{
                fs[i] = pool.submit(this.filtersIir[i]);
            }
        }

        for (int i = 0; i < this.outputSignal.length; i++){
            this.outputSignal[i] += fs[0].get()[i] + fs[1].get()[i] + fs[2].get()[i] + fs[3].get()[i] +
                    fs[4].get()[i] + fs[5].get()[i] + fs[6].get()[i] + fs[7].get()[i] + fs[8].get()[i] + fs[9].get()[i];
        }
    }

    public short[] getOutputSignal(){
        return this.outputSignal;
    }

    public Filter getFilter(int numberFilter){
        return this.filters[numberFilter];
    }

    public FilterIir getFilterIir(int numberFilter){
        return this.filtersIir[numberFilter];
    }

    public void close(){
        if (this.pool != null){
            this.pool.shutdown();
        }
    }

}
