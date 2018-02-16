package com.robo.api.controllers;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robo.api.dtos.MovimentoDto;
import com.robo.api.entities.Movimento;
import com.robo.api.services.RoboService;

@RestController
@RequestMapping("/rest/mars")
@CrossOrigin(origins = "*")
public class RoboController {

	@Autowired
	private RoboService roboService;

	@PostMapping(value = "/{comando}")
	public ResponseEntity<MovimentoDto> moverRobo(@PathVariable("comando") String comando, HttpSession httpSession){

		MovimentoDto movimentoDto = new MovimentoDto();
		movimentoDto.setComando(comando);

		if (Optional.ofNullable(httpSession.getAttribute("posicaoAtual")).isPresent()) {
			movimentoDto.setPosicaoAtual(httpSession.getAttribute("posicaoAtual").toString());
		} else {
			movimentoDto.setPosicaoAtual("0,0,N");
		}
		
		//converte em objeto para passar pra camada de serviço
		Movimento movimento = converteMovimentoDtoParaMovimento(movimentoDto);
		movimento = this.roboService.moverRobo(movimento);
		
		//converte para dto para responder a requisicao
		movimentoDto = converteMovimentoParaMovimentoDto(movimento);
		httpSession.setAttribute("posicaoAtual", movimentoDto.getPosicaoAtual());
		
		if(movimentoDto.getErros().size() > 0){
			return ResponseEntity.badRequest().body(movimentoDto);
		}
		
		return ResponseEntity.ok(movimentoDto);
	}
	
	@GetMapping(value="/posicao")
	public ResponseEntity<MovimentoDto> obterPosicaoAtual(HttpSession httpSession){
		
		MovimentoDto movimentoDto = new MovimentoDto();
		
		if (Optional.ofNullable(httpSession.getAttribute("posicaoAtual")).isPresent()) {
			movimentoDto.setPosicaoAtual(httpSession.getAttribute("posicaoAtual").toString());
		} else {
			movimentoDto.setPosicaoAtual("0,0,N");
		}
		
		return ResponseEntity.ok(movimentoDto);
	}
	
	private Movimento converteMovimentoDtoParaMovimento(MovimentoDto movimentoDto){
		Movimento movimento = new Movimento();
		movimento.setComando(movimentoDto.getComando());
		movimento.setPosicaoAtual(movimentoDto.getPosicaoAtual());
		return movimento;
	}
	
	private MovimentoDto converteMovimentoParaMovimentoDto(Movimento movimento){
		MovimentoDto movimentoDto = new MovimentoDto();
		movimentoDto.setComando(movimento.getComando());
		movimentoDto.setPosicaoAtual(movimento.getPosicaoAtual());
		movimentoDto.setErros(new ArrayList<String>(movimento.getErros()));
		return movimentoDto;
	}

}
