package com.robo.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robo.api.dtos.PosicaoDto;
import com.robo.api.response.Response;

@RestController
@RequestMapping("/rest/mars")
@CrossOrigin(origins = "*")
public class RoboController {
	 
	@PostMapping(value = "/{comando}")
	public ResponseEntity<Response<PosicaoDto>> mover(@PathVariable("comando") String comando){
		
		Response<PosicaoDto> response = new Response<PosicaoDto>();
		
		PosicaoDto posicaoDto = new PosicaoDto();
		posicaoDto.setCoordenadas("0, 2, W");
		
		response.setData(posicaoDto);
		return ResponseEntity.ok(response);
	}

}
