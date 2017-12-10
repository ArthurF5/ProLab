package com.moliveiralucas.prolab.model;

/**
 * Created by moliveiralucas on 10/12/17.
 */

public class Filial {
    private String mLab;
    private String mEnd;
    private Double mDistancia;

    public Filial(String laboratorio, String endereco, Double distancia) {

    }

    public String getmLab() {
        return mLab;
    }

    public void setmLab(String mLab) {
        this.mLab = mLab;
    }

    public String getmEnd() {
        return mEnd;
    }

    public void setmEnd(String mEnd) {
        this.mEnd = mEnd;
    }

    public Double getmDistancia(){
        return mDistancia;
    }

    public void setmDistancia(Double mDistancia){
        this.mDistancia = mDistancia;
    }
}
