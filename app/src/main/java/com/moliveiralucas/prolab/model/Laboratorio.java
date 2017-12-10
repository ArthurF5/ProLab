package com.moliveiralucas.prolab.model;

import java.util.ArrayList;

public class Laboratorio {
	private Integer labID;
	private String laboratorio;
    private String mEndereco;

    public Laboratorio(String laboratorio, String endereco) {

    }

    public Laboratorio(){

    }

    public String getmEndereco() {
        return mEndereco;
    }

    public void setmEndereco(String mEndereco) {
        this.mEndereco = mEndereco;
    }

	public Integer getLabID() {
		return labID;
	}
	public void setLabID(Integer labID) {
		this.labID = labID;
	}
	public String getLaboratorio() {
		return laboratorio;
	}
	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	@Override
	public String toString() {
		return laboratorio;
	}
}
