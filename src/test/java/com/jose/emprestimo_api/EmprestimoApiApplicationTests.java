package com.jose.emprestimo_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EmprestimoApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void deveAprovarEmprestimoComValorValido() throws Exception {

		mockMvc.perform(post("/emprestimos/analisar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 20000
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(content().string("\"APROVADO\""));
	}

	@Test
	void deveRejeitarEmprestimoComValorNegativo() throws Exception {

		mockMvc.perform(post("/emprestimos/analisar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": -5000
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.valor")
						.value("O valor do empréstimo deve ser maior que zero."));
	}

	@Test
	void deveManterEmprestimoPendenteQuandoValorSuperiorAoLimite() throws Exception {

		mockMvc.perform(post("/emprestimos/analisar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 60000
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(content().string("\"PENDENTE\""));
	}

	@Test
	void deveAprovarEmprestimoNoLimiteDeCinquentaMil() throws Exception {

		mockMvc.perform(post("/emprestimos/analisar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 50000
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(content().string("\"APROVADO\""));
	}

}