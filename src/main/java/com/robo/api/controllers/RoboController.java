package com.robo.api.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robo.api.dtos.MovimentoDto;
import com.robo.api.response.Response;
import com.robo.api.services.RoboService;

@RestController
@RequestMapping("/rest/mars")
@CrossOrigin(origins = "*")
public class RoboController {

	@Autowired
	private RoboService roboService;

	@PostMapping(value = "/{comando}")
	public ResponseEntity<Response<MovimentoDto>> mover(@PathVariable("comando") String comando, HttpSession httpSession){

		Response<MovimentoDto> response = new Response<MovimentoDto>();
		MovimentoDto movimentoDto = new MovimentoDto();
		movimentoDto.setComando(comando);

		if (Optional.ofNullable(httpSession.getAttribute("posicaoAtual")).isPresent()) {
			movimentoDto.setPosicaoAtual(httpSession.getAttribute("posicaoAtual").toString());
		} else {
			movimentoDto.setPosicaoAtual("0,0,N");
		}

		response.setData(movimentoDto);
		response = this.roboService.moverRobo(response);
		httpSession.setAttribute("posicaoAtual", response.getData().getPosicaoAtual());

		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

}
