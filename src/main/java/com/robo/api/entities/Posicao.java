package com.robo.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "posicao")
public class Posicao {

	private long id;
	private String coordenadas;
	private Date dataAlteracao;
	
	@Id
	@Column(name="posicao_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "coordenadas", nullable = false)
	public String getCoordenadas() {
		return coordenadas;
	}
	public void setCoordenadas(String posicao) {
		this.coordenadas = posicao;
	}
	
	@Column(name = "data_alteracao", nullable = false)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataUltimoMovimento) {
		this.dataAlteracao = dataUltimoMovimento;
	}
	
}
