package com.robo.api.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;

public class Movimento {
	private String posicaoAtual;
	private String comando;
	private List<String> erros;

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

	public List<String> getErros() {
		if (this.erros == null) {
			this.erros = new ArrayList<String>();
		}
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}
}
