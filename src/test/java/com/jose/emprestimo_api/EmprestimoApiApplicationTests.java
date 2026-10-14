
package com.jose.emprestimo_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmprestimoApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// 1. Verifica se a aplicação inicia corretamente
	@Test
	void contextLoads() {
	}

	// 2. Verifica a aprovação de um empréstimo válido
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

	// 3. Verifica a rejeição de um valor negativo
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

	// 4. Verifica se valores acima de R$ 50.000 ficam pendentes
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

	// 5. Verifica a aprovação no limite exato de R$ 50.000
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

	// 6. Verifica a rejeição de um cliente sem nome
	@Test
	void deveRejeitarCadastroDeClienteSemNome() throws Exception {

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "",
						    "email": "teste@email.com",
						    "idade": 30,
						    "salario": 3000
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.nome")
						.value("O nome é obrigatório."));
	}

	// 7. Verifica a rejeição quando o e-mail não é informado
	@Test
	void deveRejeitarCadastroDeClienteSemEmail() throws Exception {

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "José",
						    "idade": 30,
						    "salario": 3000
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.email")
						.value("O e-mail é obrigatório."));
	}

	// 8. Verifica a rejeição de um e-mail com formato inválido
	@Test
	void deveRejeitarCadastroDeClienteComEmailInvalido() throws Exception {

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "José",
						    "email": "email-invalido",
						    "idade": 30,
						    "salario": 3000
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.email")
						.value("Informe um e-mail válido."));
	}

	// 9. Verifica a rejeição de clientes menores de 18 anos
	@Test
	void deveRejeitarCadastroDeClienteMenorDeIdade() throws Exception {

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "José",
						    "email": "teste@email.com",
						    "idade": 17,
						    "salario": 3000
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.idade")
						.value("O cliente deve ter pelo menos 18 anos."));
	}

	// 10. Verifica a rejeição de salário igual a zero
	@Test
	void deveRejeitarCadastroDeClienteComSalarioZero() throws Exception {

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "José",
						    "email": "teste@email.com",
						    "idade": 30,
						    "salario": 0
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.salario")
						.value("O salário deve ser maior que zero."));
	}
}
