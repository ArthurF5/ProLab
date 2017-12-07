package com.moliveiralucas.prolab.model;

public class Endereco {
	private Integer endID;
	private String endereco;
	private String logradouro;
	private String numero;
	private Integer cidade;
	private Integer estado;
	
	public Integer getEndID() {
		return endID;
	}

	public void setEndID(Integer endID) {
		this.endID = endID;
	}

	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public void setEndereco(String rua, Integer cidade, Integer estado, String numero) {
		this.endereco = rua + " " + numero + " " + cidade + " " + estado;
	}

	public Integer getCidade() {
		return cidade;
	}

	public void setCidade(Integer cidade) {
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return endereco;
	}
}
