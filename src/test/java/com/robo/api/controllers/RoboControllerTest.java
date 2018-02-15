package com.robo.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.robo.api.dtos.MovimentoDto;
import com.robo.api.response.Response;
import com.robo.api.services.RoboService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoboControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RoboService roboService;

	private static final String ENVIAR_COMANDO = "/rest/mars/";
	private static final String COMANDO_VALIDO = "RMMML";
	private static final String POSICAO_ESPERADA = "3,0,N";
	private static final String COMANDO_INVALIDO = "MMMMMMMM";
	private static final String COMANDO_ROBO_PARADO = "MMMMRLMM";
	private static final String POSICAO_ESPERADA_ROBO_PARADO = "0,4,N";
	private static final String COMANDO_NAO_EXECUTADO = "MM";

	@Test
	public void testeEnviarComandoErrado() throws Exception{
		BDDMockito.given(this.roboService.moverRobo(Mockito.any(Response.class))).willReturn(this.obterDadosResponseComErro());

		mvc.perform(MockMvcRequestBuilders.post(ENVIAR_COMANDO + COMANDO_INVALIDO).accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Comando inválido. Digite no máximo 4 movimentos sequenciais (M) e no máximo 2 rotações sequenciais (L ou R)."));
	}

	@Test
	public void testeEnviarComandoCorretoReceberLocalizacaoCorreta() throws Exception{
		BDDMockito.given(this.roboService.moverRobo(Mockito.any(Response.class))).willReturn(this.obterDadosResponseComSucesso());

		mvc.perform(MockMvcRequestBuilders.post(ENVIAR_COMANDO + COMANDO_VALIDO).accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.erros").isEmpty())
			.andExpect(jsonPath("$.data.posicaoAtual").value(POSICAO_ESPERADA));
	}

	@Test
	public void testeEnviarComandoCorretoMasRoboPararDevidoFimDaArea() throws Exception{
		BDDMockito.given(this.roboService.moverRobo(Mockito.any(Response.class))).willReturn(this.obterDadosResponseComErroDeRoboParado());

		mvc.perform(MockMvcRequestBuilders.post(ENVIAR_COMANDO + COMANDO_ROBO_PARADO).accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("O robo parou pois estava saindo da área permitida. O trecho final do comando (" + COMANDO_NAO_EXECUTADO + ") não foi executado."
											   + " A posição atual do robo é " + POSICAO_ESPERADA_ROBO_PARADO))
			.andExpect(jsonPath("$.data.posicaoAtual").value(POSICAO_ESPERADA_ROBO_PARADO));
	}

	private Response obterDadosResponseComSucesso(){
		Response response = new Response(new MovimentoDto());
		((MovimentoDto)response.getData()).setPosicaoAtual(POSICAO_ESPERADA);

		return response;
	}


	private Response obterDadosResponseComErro(){
		Response response = new Response(new MovimentoDto());
		response.getErros().add("Comando inválido. Digite no máximo 4 movimentos sequenciais (M) e no máximo 2 rotações sequenciais (L ou R).");
		((MovimentoDto)response.getData()).getPosicaoAtual();

		return response;
	}

	private Response obterDadosResponseComErroDeRoboParado(){
		Response response = new Response(new MovimentoDto());
		response.getErros().add("O robo parou pois estava saindo da área permitida. O trecho final do comando (" + COMANDO_NAO_EXECUTADO + ") não foi executado."
										+ " A posição atual do robo é " + POSICAO_ESPERADA_ROBO_PARADO);
		((MovimentoDto)response.getData()).setPosicaoAtual(POSICAO_ESPERADA_ROBO_PARADO);

		return response;
	}


}
