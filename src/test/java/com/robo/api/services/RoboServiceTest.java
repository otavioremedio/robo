package com.robo.api.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.robo.api.dtos.MovimentoDto;
import com.robo.api.entities.Movimento;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RoboServiceTest {

	@Autowired
	private RoboService roboService;

	Movimento testeA = new Movimento();
	Movimento testeB = new Movimento();
	Movimento testeC = new Movimento();
	Movimento testeD = new Movimento();

	@Test
	public void testeValidarComandoErrado(){
		testeA.setComando("RRRL");
	    testeB.setComando("RLMMMMMM");
	    testeC.setComando("MMMMMMRL");
	    testeD.setComando("MMMMMMMM");

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

		testeA.setComando("RMMML");
		testeA.setPosicaoAtual("0,0,N");
		testeB.setComando("RLMMM");
		testeB.setPosicaoAtual("0,0,N");
		testeC.setComando("MMM");
		testeC.setPosicaoAtual("0,0,N");
		testeD.setComando("MMML");
		testeD.setPosicaoAtual("0,0,N");

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

		testeA.setComando("RMMML");
		testeA.setPosicaoAtual("0,0,N");
		testeB.setComando("RLMMM");
		testeB.setPosicaoAtual("0,0,N");
		testeC.setComando("MMM");
		testeC.setPosicaoAtual("0,0,N");
		testeD.setComando("MMML");
		testeD.setPosicaoAtual("0,0,N");

		testeA = this.roboService.moverRobo(testeA);
		testeB = this.roboService.moverRobo(testeB);
		testeC = this.roboService.moverRobo(testeC);
		testeD = this.roboService.moverRobo(testeD);

		assertTrue(testeA.getPosicaoAtual().equals(posicaoEsperadaTesteA)
				   && testeB.getPosicaoAtual().equals(posicaoEsperadaTesteB)
				   && testeC.getPosicaoAtual().equals(posicaoEsperadaTesteC)
				   && testeD.getPosicaoAtual().equals(posicaoEsperadaTesteD));

	}

}
