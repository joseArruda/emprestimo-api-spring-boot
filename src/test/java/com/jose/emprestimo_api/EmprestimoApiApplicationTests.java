
package com.jose.emprestimo_api;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jose.emprestimo_api.entity.Cliente;
import com.jose.emprestimo_api.entity.Emprestimo;
import com.jose.emprestimo_api.entity.StatusEmprestimo;
import com.jose.emprestimo_api.repository.ClienteRepository;
import com.jose.emprestimo_api.repository.EmprestimoRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmprestimoApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EmprestimoRepository emprestimoRepository;

	// =====================================================
	// TESTES DE INICIALIZAÇÃO E REGRAS DE ANÁLISE
	// =====================================================

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

	// =====================================================
	// TESTES DE VALIDAÇÃO DE CLIENTES
	// =====================================================

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

	// =====================================================
	// TESTES DO CRUD DE CLIENTES
	// =====================================================

	@Test
	void deveCadastrarClienteComDadosValidos() throws Exception {

		String email = gerarEmail();

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "Cliente Teste",
						    "email": "%s",
						    "idade": 30,
						    "salario": 3000
						}
						""".formatted(email)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nome").value("Cliente Teste"))
				.andExpect(jsonPath("$.email").value(email))
				.andExpect(jsonPath("$.idade").value(30))
				.andExpect(jsonPath("$.salario").value(3000.0));
	}

	@Test
	void deveListarClientes() throws Exception {

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void deveBuscarClientePorId() throws Exception {

		Cliente cliente = criarClientePersistido();

		mockMvc.perform(get("/clientes/{id}", cliente.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(cliente.getId()))
				.andExpect(jsonPath("$.nome").value(cliente.getNome()))
				.andExpect(jsonPath("$.email").value(cliente.getEmail()))
				.andExpect(jsonPath("$.idade").value(30))
				.andExpect(jsonPath("$.salario").value(3000.0));
	}

	@Test
	void deveRetornar404QuandoClienteNaoExiste() throws Exception {

		mockMvc.perform(get("/clientes/999999999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveAtualizarClienteExistente() throws Exception {

		Cliente cliente = criarClientePersistido();
		String novoEmail = gerarEmail();

		mockMvc.perform(put("/clientes/{id}", cliente.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "Cliente Atualizado",
						    "email": "%s",
						    "idade": 35,
						    "salario": 4500
						}
						""".formatted(novoEmail)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(cliente.getId()))
				.andExpect(jsonPath("$.nome").value("Cliente Atualizado"))
				.andExpect(jsonPath("$.email").value(novoEmail))
				.andExpect(jsonPath("$.idade").value(35))
				.andExpect(jsonPath("$.salario").value(4500.0));
	}

	@Test
	void deveRetornar404AoAtualizarClienteInexistente() throws Exception {

		mockMvc.perform(put("/clientes/999999999")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "nome": "Cliente Inexistente",
						    "email": "inexistente@email.com",
						    "idade": 30,
						    "salario": 3000
						}
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveExcluirClienteSemEmprestimos() throws Exception {

		Cliente cliente = criarClientePersistido();

		mockMvc.perform(delete("/clientes/{id}", cliente.getId()))
				.andExpect(status().isNoContent());
	}

	@Test
	void deveRetornar404AoExcluirClienteInexistente() throws Exception {

		mockMvc.perform(delete("/clientes/999999999"))
				.andExpect(status().isNotFound());
	}

	// =====================================================
	// TESTES DO CRUD DE EMPRÉSTIMOS
	// =====================================================

	@Test
	void deveListarEmprestimos() throws Exception {

		mockMvc.perform(get("/emprestimos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void deveCriarEmprestimoParaClienteExistente() throws Exception {

		Cliente cliente = criarClientePersistido();

		mockMvc.perform(post("/emprestimos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 20000,
						    "prazo": 12,
						    "clienteId": %d
						}
						""".formatted(cliente.getId())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.clienteId").value(cliente.getId()))
				.andExpect(jsonPath("$.valor").value(20000.0))
				.andExpect(jsonPath("$.prazo").value(12))
				.andExpect(jsonPath("$.status").value("APROVADO"));
	}

	@Test
	void deveRetornar404AoCriarEmprestimoParaClienteInexistente() throws Exception {

		mockMvc.perform(post("/emprestimos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 20000,
						    "prazo": 12,
						    "clienteId": 999999999
						}
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveRejeitarEmprestimoComDadosInvalidos() throws Exception {

		mockMvc.perform(post("/emprestimos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 0,
						    "prazo": 0,
						    "clienteId": 1
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erro").value("Dados inválidos"))
				.andExpect(jsonPath("$.campos.valor").exists())
				.andExpect(jsonPath("$.campos.prazo").exists());
	}

	@Test
	void deveBuscarEmprestimoPorId() throws Exception {

		Cliente cliente = criarClientePersistido();
		Emprestimo emprestimo = criarEmprestimoPersistido(cliente, 20000, 12);

		mockMvc.perform(get("/emprestimos/{id}", emprestimo.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(emprestimo.getId()))
				.andExpect(jsonPath("$.clienteId").value(cliente.getId()))
				.andExpect(jsonPath("$.valor").value(20000.0))
				.andExpect(jsonPath("$.prazo").value(12))
				.andExpect(jsonPath("$.status").value("APROVADO"));
	}

	@Test
	void deveRetornar404QuandoEmprestimoNaoExiste() throws Exception {

		mockMvc.perform(get("/emprestimos/999999999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveAtualizarEmprestimoExistente() throws Exception {

		Cliente cliente = criarClientePersistido();
		Emprestimo emprestimo = criarEmprestimoPersistido(cliente, 20000, 12);

		mockMvc.perform(put("/emprestimos/{id}", emprestimo.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 60000,
						    "prazo": 24,
						    "clienteId": %d
						}
						""".formatted(cliente.getId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(emprestimo.getId()))
				.andExpect(jsonPath("$.clienteId").value(cliente.getId()))
				.andExpect(jsonPath("$.valor").value(60000.0))
				.andExpect(jsonPath("$.prazo").value(24))
				.andExpect(jsonPath("$.status").value("PENDENTE"));
	}

	@Test
	void deveRetornar404AoAtualizarEmprestimoInexistente() throws Exception {

		mockMvc.perform(put("/emprestimos/999999999")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "valor": 20000,
						    "prazo": 12,
						    "clienteId": 1
						}
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveExcluirEmprestimoExistente() throws Exception {

		Cliente cliente = criarClientePersistido();
		Emprestimo emprestimo = criarEmprestimoPersistido(cliente, 20000, 12);

		mockMvc.perform(delete("/emprestimos/{id}", emprestimo.getId()))
				.andExpect(status().isNoContent());
	}

	@Test
	void deveRetornar404AoExcluirEmprestimoInexistente() throws Exception {

		mockMvc.perform(delete("/emprestimos/999999999"))
				.andExpect(status().isNotFound());
	}

	// =====================================================
	// TESTES DE RELACIONAMENTOS E FILTROS
	// =====================================================

	@Test
	void deveListarEmprestimosPorCliente() throws Exception {

		Cliente cliente = criarClientePersistido();
		criarEmprestimoPersistido(cliente, 20000, 12);

		mockMvc.perform(get("/clientes/{id}/emprestimos", cliente.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].clienteId").value(cliente.getId()));
	}

	@Test
	void deveRetornar404AoListarEmprestimosDeClienteInexistente() throws Exception {

		mockMvc.perform(get("/clientes/999999999/emprestimos"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deveFiltrarEmprestimosPorStatus() throws Exception {

		Cliente cliente = criarClientePersistido();
		criarEmprestimoPersistido(cliente, 20000, 12);

		mockMvc.perform(get("/emprestimos/status/APROVADO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath(
						"$[?(@.clienteId == " + cliente.getId() + ")]").isNotEmpty());
	}

	@Test
	void deveRejeitarStatusInvalido() throws Exception {

		mockMvc.perform(get("/emprestimos/status/STATUS_INVALIDO"))
				.andExpect(status().isBadRequest());
	}

	// =====================================================
	// MÉTODOS AUXILIARES PARA PREPARAR DADOS DOS TESTES
	// =====================================================

	private Cliente criarClientePersistido() {

		Cliente cliente = new Cliente(
				"Cliente de Teste",
				gerarEmail(),
				30,
				3000);

		return clienteRepository.saveAndFlush(cliente);
	}

	private Emprestimo criarEmprestimoPersistido(
			Cliente cliente,
			double valor,
			int prazo) {

		Emprestimo emprestimo = new Emprestimo(valor, prazo);
		emprestimo.setCliente(cliente);

		if (valor <= 50000) {
			emprestimo.setStatus(StatusEmprestimo.APROVADO);
		} else {
			emprestimo.setStatus(StatusEmprestimo.PENDENTE);
		}

		return emprestimoRepository.saveAndFlush(emprestimo);
	}

	private String gerarEmail() {
		return "teste-" + UUID.randomUUID() + "@email.com";
	}
}
