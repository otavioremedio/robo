package com.robo.api.services.impl;

import java.util.Iterator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.robo.api.entities.Movimento;
import com.robo.api.services.RoboService;

@Service
public class RoboServiceImpl implements RoboService {

	private static final Logger log = LoggerFactory.getLogger(RoboServiceImpl.class);

	@Override
	public Movimento moverRobo(Movimento movimento) {

		if(!validaComando(movimento.getComando())){
			log.error("Comando {} inválido.", movimento.getComando());
			movimento.getErros().add("Comando inválido. Digite no máximo 4 movimentos sequenciais (M) e no máximo 2 rotações sequenciais (L ou R).");
		} else {
			movimento = realizaMovimento(movimento);
		}

		return movimento;
	}

	private boolean validaComando(String comando){
		return (comando + " ").matches("^([LR]{0,2})([M]{1,4}[RL\\ ]{1,3})+");
	}

	private boolean validaMovimento(Character movimento, String posicaoAtual){

		final int xMinOeste = 0;
		final int xMaxLeste = 4;
		final int yMaxNorte = 4;
		final int yMinSul   = 0;

		Character direcaoAtual = posicaoAtual.split(",")[2].charAt(0);
		int xAtual = Integer.valueOf(posicaoAtual.split(",")[0]);
		int yAtual = Integer.valueOf(posicaoAtual.split(",")[1]);

		if(movimento.equals('M')){

			switch (direcaoAtual) {
			case 'N':
				return(yAtual + 1 <= yMaxNorte);
			case 'S':
				return(yAtual - 1 >= yMinSul);
			case 'W':
				return(xAtual - 1 >= xMinOeste);
			case 'E':
				return(xAtual + 1 <= xMaxLeste);
			}
		}

		//caso seja movimento de direção
		return true;
	}

	private Character mudaDirecao(Character direcaoAtual, Character rotacao){

		switch (rotacao) {
		case 'L':
			switch (direcaoAtual) {
			case 'N':
				return 'W';
			case 'W':
				return 'S';
			case 'S':
				return 'E';
			case 'E':
				return 'N';
			}
		case 'R':
			switch (direcaoAtual) {
			case 'N':
				return 'E';
			case 'E':
				return 'S';
			case 'S':
				return 'W';
			case 'W':
				return 'N';
			}
		default:
			return direcaoAtual;
		}
	}

	private Movimento realizaMovimento(Movimento movimento){

		Iterator<Character> comandos = movimento.getComando().chars().mapToObj(e -> (char)e).collect(Collectors.toList()).iterator();
		Character direcaoAtual;
		String posicaoAtual;
		Character comando;
		int xAtual = 0;
		int yAtual = 0;

		while (comandos.hasNext()) {

			posicaoAtual = movimento.getPosicaoAtual();
			comando = comandos.next();

			if(validaMovimento(comando, posicaoAtual)){
				direcaoAtual = posicaoAtual.split(",")[2].charAt(0);
				xAtual = Integer.valueOf(posicaoAtual.split(",")[0]);
				yAtual = Integer.valueOf(posicaoAtual.split(",")[1]);

				if(!comando.equals('R') && !comando.equals('L')){
					switch (direcaoAtual) {
					case 'N':
						yAtual++;
						break;
					case 'S':
						yAtual--;
						break;
					case 'W':
						xAtual--;
						break;
					case 'E':
						xAtual++;
						break;
					}
				} else {
					direcaoAtual = mudaDirecao(direcaoAtual, comando);
					log.info("Robo mudou a direção para {}.", direcaoAtual);
				}

				comandos.remove();
				movimento.setPosicaoAtual(String.valueOf(xAtual) + "," + String.valueOf(yAtual) + "," + direcaoAtual);
				log.info("Robo se movimentou para {}.", movimento.getPosicaoAtual());
			} else {
				log.error("Robo parou pois estava saindo da área permitida. Posição atual é {}.", movimento.getPosicaoAtual());
				StringBuilder comandoNaoExecutado = new StringBuilder(comando.toString());
				comandos.forEachRemaining(comandoNaoExecutado::append);
				movimento.getErros().add("O robo parou pois estava saindo da área permitida. O trecho final do comando (" + comandoNaoExecutado.toString() + ") não foi executado."
										+ " A posição atual do robo é " + movimento.getPosicaoAtual());
			}
		}

		return movimento;
	}
}
