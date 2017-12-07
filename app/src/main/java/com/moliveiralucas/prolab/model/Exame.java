package com.moliveiralucas.prolab.model;


public class Exame {
	private Integer exameID;
	private String exame;
	private Double valor;
	public Integer getExameID() {
		return exameID;
	}
	public void setExameID(Integer exameID) {
		this.exameID = exameID;
	}
	public String getExame() {
		return exame;
	}
	public void setExame(String exame) {
		this.exame = exame;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return exame;
	}
}
