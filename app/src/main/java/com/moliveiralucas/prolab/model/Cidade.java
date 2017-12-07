package com.moliveiralucas.prolab.model;

public class Cidade {
	private Integer cidadeID;
	private String cidade;
	public Integer getCidadeID() {
		return cidadeID;
	}
	public void setCidadeID(Integer cidadeID) {
		this.cidadeID = cidadeID;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@Override
	public String toString() {
		return cidade;
	}
}
