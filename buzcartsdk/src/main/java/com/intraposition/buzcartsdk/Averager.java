package com.intraposition.buzcartsdk;


import java.util.Deque;
import java.util.LinkedList;

class Averager {

    private static int INTERVAL = 1000;

    private int bufferLen = 5;

    private long lastUpdate;

    private Deque<Double> buff;

    public Averager(int buffLen, int interval){
        this.INTERVAL = interval;
        bufferLen = buffLen;
        lastUpdate = System.currentTimeMillis();
        buff = new LinkedList<>();
    }

    public void update(double value){

        long t= System.currentTimeMillis();

        if ((t - lastUpdate) > INTERVAL){
            buff.push(value);
            if (buff.size() > bufferLen){
                buff.removeLast();
            }
            lastUpdate = t;
        }
    }

//    private double getMedian(){
//        Arrays.sort(cyclicBuffer);
//        double median;
//        if (cyclicBuffer.length % 2 == 0)
//            median = ((double)cyclicBuffer[cyclicBuffer.length/2] +
//                    (double)cyclicBuffer[cyclicBuffer.length/2 - 1])/2;
//        else
//            median = (double) cyclicBuffer[cyclicBuffer.length/2];
//        return median;
//    }

//    private double getAverage(){
//        double sum = 0;
//        for (int i =0; i< bufferLen; i++){
//            sum += cyclicBuffer[i];
//        }
//        return sum / bufferLen;
//    }


    public double [] getOrientationBuffer(){
        double [] darray = new double[bufferLen];
        int i = 0;
        for (Object val : buff.toArray()){
            darray[i] = (Double)val;
            i +=1;
        }
        return darray;
    }
}
