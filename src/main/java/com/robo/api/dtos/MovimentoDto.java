package com.robo.api.dtos;

public class MovimentoDto {

	private String posicaoAtual;
	private String comando;

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public String getPosicaoAtual() {
		return posicaoAtual;
	}

	public void setPosicaoAtual(String posicaoAtual) {
		this.posicaoAtual = posicaoAtual;
	}


}
