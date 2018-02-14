package com.robo.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.robo.api.entities.Posicao;

@Repository
public interface PosicaoRepository extends CrudRepository<Posicao, Long>{ 

}
