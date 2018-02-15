package com.robo.api.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.robo.api.dtos.MovimentoDto;
import com.robo.api.response.Response;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RoboServiceTest {

	@Autowired
	private RoboService roboService;

	Response testeA = new Response(new MovimentoDto());
	Response testeB = new Response(new MovimentoDto());
	Response testeC = new Response(new MovimentoDto());
	Response testeD = new Response(new MovimentoDto());

	MovimentoDto movimentoA = (MovimentoDto)testeA.getData();
	MovimentoDto movimentoB = (MovimentoDto)testeB.getData();
	MovimentoDto movimentoC = (MovimentoDto)testeC.getData();
	MovimentoDto movimentoD = (MovimentoDto)testeD.getData();


	@Test
	public void testeValidarComandoErrado(){
	    ((MovimentoDto)testeA.getData()).setComando("RRRL");
	    ((MovimentoDto)testeB.getData()).setComando("RLMMMMMM");
	    ((MovimentoDto)testeC.getData()).setComando("MMMMMMRL");
	    ((MovimentoDto)testeD.getData()).setComando("MMMMMMMM");

		testeA = this.roboService.moverRobo(testeA);
		testeB = this.roboService.moverRobo(testeB);
		testeC = this.roboService.moverRobo(testeC);
		testeD = this.roboService.moverRobo(testeD);

		assertTrue(testeA.getErros().stream().anyMatch(str -> str.toString().contains("Comando inválido"))
				   && testeB.getErros().stream().anyMatch(str -> str.toString().contains("Comando inválido"))
				   && testeC.getErros().stream().anyMatch(str -> str.toString().contains("Comando inválido"))
				   && testeD.getErros().stream().anyMatch(str -> str.toString().contains("Comando inválido")));

	}

	@Test
	public void testeValidarComandoCorreto(){

		movimentoA.setComando("RMMML");
		movimentoA.setPosicaoAtual("0,0,N");
		movimentoB.setComando("RLMMM");
		movimentoB.setPosicaoAtual("0,0,N");
		movimentoC.setComando("MMM");
		movimentoC.setPosicaoAtual("0,0,N");
		movimentoD.setComando("MMML");
		movimentoD.setPosicaoAtual("0,0,N");

		testeA = this.roboService.moverRobo(testeA);
		testeB = this.roboService.moverRobo(testeB);
		testeC = this.roboService.moverRobo(testeC);
		testeD = this.roboService.moverRobo(testeD);

		assertTrue(testeA.getErros().isEmpty()
				   && testeB.getErros().isEmpty()
				   && testeC.getErros().isEmpty()
				   && testeD.getErros().isEmpty());
	}

	@Test
	public void testeValidarRespostaLocalizacao(){

		String posicaoEsperadaTesteA = "3,0,N";
		String posicaoEsperadaTesteB = "0,3,N";
		String posicaoEsperadaTesteC = "0,3,N";
		String posicaoEsperadaTesteD = "0,3,W";

		movimentoA.setComando("RMMML");
		movimentoA.setPosicaoAtual("0,0,N");
		movimentoB.setComando("RLMMM");
		movimentoB.setPosicaoAtual("0,0,N");
		movimentoC.setComando("MMM");
		movimentoC.setPosicaoAtual("0,0,N");
		movimentoD.setComando("MMML");
		movimentoD.setPosicaoAtual("0,0,N");

		testeA = this.roboService.moverRobo(testeA);
		testeB = this.roboService.moverRobo(testeB);
		testeC = this.roboService.moverRobo(testeC);
		testeD = this.roboService.moverRobo(testeD);

		assertTrue(movimentoA.getPosicaoAtual().equals(posicaoEsperadaTesteA)
				   && movimentoB.getPosicaoAtual().equals(posicaoEsperadaTesteB)
				   && movimentoC.getPosicaoAtual().equals(posicaoEsperadaTesteC)
				   && movimentoD.getPosicaoAtual().equals(posicaoEsperadaTesteD));

	}

}
