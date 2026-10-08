package com.jose.emprestimo_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jose.emprestimo_api.dto.AnaliseEmprestimoRequest;
import com.jose.emprestimo_api.dto.ClienteRequest;
import com.jose.emprestimo_api.dto.ClienteResponse;
import com.jose.emprestimo_api.dto.EmprestimoRequest;
import com.jose.emprestimo_api.dto.EmprestimoResponse;
import com.jose.emprestimo_api.entity.Cliente;
import com.jose.emprestimo_api.entity.Emprestimo;
import com.jose.emprestimo_api.entity.StatusEmprestimo;
import com.jose.emprestimo_api.service.ClienteService;
import com.jose.emprestimo_api.service.EmprestimoService;

import jakarta.validation.Valid;

@RestController
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    private final ClienteService clienteService;

    public EmprestimoController(EmprestimoService emprestimoService, ClienteService clienteService) {
        this.emprestimoService = emprestimoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/emprestimos")
    public List<EmprestimoResponse> listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimo();

        return emprestimos.stream()
                .map(emprestimo -> new EmprestimoResponse(
                        emprestimo.getId(),
                        emprestimo.getCliente() != null ? emprestimo.getCliente().getId() : null,
                        "Emprestimo encontrado",
                        emprestimo.getValor(),
                        emprestimo.getPrazo(),
                        emprestimo.getStatus()))
                .toList();
    }

    @GetMapping("/emprestimos/{id}")
    public ResponseEntity<EmprestimoResponse> buscarEmprestimo(@PathVariable Long id) {

        Optional<Emprestimo> emprestimo = emprestimoService.buscarPorId(id);

        if (emprestimo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Emprestimo emprestimoEncontrado = emprestimo.get();

        EmprestimoResponse resposta = new EmprestimoResponse(
                emprestimoEncontrado.getId(),
                emprestimoEncontrado.getCliente() != null ? emprestimoEncontrado.getCliente().getId() : null,
                "Empréstimo encontrado.",
                emprestimoEncontrado.getValor(),
                emprestimoEncontrado.getPrazo(),
                emprestimoEncontrado.getStatus());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/clientes")
    public List<ClienteResponse> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();

        return clientes.stream()
                .map(cliente -> new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getIdade(),
                        cliente.getSalario()))
                .toList();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);

        if (cliente.isPresent()) {
            Cliente clienteEncontrado = cliente.get();

            ClienteResponse resposta = new ClienteResponse(
                    clienteEncontrado.getId(),
                    clienteEncontrado.getNome(),
                    clienteEncontrado.getEmail(),
                    clienteEncontrado.getIdade(),
                    clienteEncontrado.getSalario());

            return ResponseEntity.ok(resposta);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status")
    public String listarStatus() {
        return "API de empréstimos funcionando!";
    }

    @GetMapping("clientes/{id}/emprestimos")
    public ResponseEntity<List<EmprestimoResponse>> listarEmprestimosPorCliente(
            @PathVariable Long id) {

        Optional<Cliente> cliente = clienteService.buscarPorId(id);

        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosPorCliente(id);

        List<EmprestimoResponse> resposta = emprestimos.stream()
                .map(emprestimo -> new EmprestimoResponse(
                        emprestimo.getId(),
                        emprestimo.getCliente() != null ? emprestimo.getCliente().getId() : null,
                        "Emprestimo encontrado.",
                        emprestimo.getValor(),
                        emprestimo.getPrazo(),
                        emprestimo.getStatus()))
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/emprestimos/status/{status}")
    public ResponseEntity<List<EmprestimoResponse>> listarEmprestimosPorStatus(
            @PathVariable StatusEmprestimo status) {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimoPorStatus(status);

        List<EmprestimoResponse> resposta = emprestimos.stream()
                .map(emprestimo -> new EmprestimoResponse(
                        emprestimo.getId(),
                        emprestimo.getCliente() != null ? emprestimo.getCliente().getId() : null,
                        "Empréstimo encontrado.",
                        emprestimo.getValor(),
                        emprestimo.getPrazo(),
                        emprestimo.getStatus()))
                .toList();

        return ResponseEntity.ok(resposta);

    }

    /* MÉTODOS POST */
    @PostMapping("/clientes")
    public ResponseEntity<ClienteResponse> criarCliente(
            @Valid @RequestBody ClienteRequest cliente) {

        Cliente novoCliente = new Cliente(
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getIdade(),
                cliente.getSalario());

        clienteService.salvarCliente(novoCliente);

        ClienteResponse resposta = new ClienteResponse(
                novoCliente.getId(),
                novoCliente.getNome(),
                novoCliente.getEmail(),
                novoCliente.getIdade(),
                novoCliente.getSalario());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resposta);
    }

    @PostMapping("/emprestimos")
    public ResponseEntity<EmprestimoResponse> criarEmprestimo(
            @Valid @RequestBody EmprestimoRequest emprestimo) {

        Emprestimo novoEmprestimo = new Emprestimo(
                emprestimo.getValor(),
                emprestimo.getPrazo());

        Optional<Emprestimo> emprestimoSalvo = emprestimoService.salvarEmprestimo(
                novoEmprestimo,
                emprestimo.getClienteId());

        if (emprestimoSalvo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Emprestimo emprestimoCriado = emprestimoSalvo.get();

        EmprestimoResponse resposta = new EmprestimoResponse(
                emprestimoCriado.getId(),
                emprestimoCriado.getCliente() != null ? emprestimoCriado.getCliente().getId() : null,
                "Empréstimo criado com sucesso.",
                emprestimoCriado.getValor(),
                emprestimoCriado.getPrazo(),
                emprestimoCriado.getStatus());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resposta);
    }

    @PostMapping("/emprestimos/analisar")
    public StatusEmprestimo analisar(@Valid @RequestBody AnaliseEmprestimoRequest emprestimo) {
        return emprestimoService.analisarEmprestimo(
                emprestimo.getValor());
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest cliente) {

        Cliente dados = new Cliente(
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getIdade(),
                cliente.getSalario());

        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(id, dados);

        if (clienteAtualizado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente clienteEncontrado = clienteAtualizado.get();

        ClienteResponse resposta = new ClienteResponse(
                clienteEncontrado.getId(),
                clienteEncontrado.getNome(),
                clienteEncontrado.getEmail(),
                clienteEncontrado.getIdade(),
                clienteEncontrado.getSalario());

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/emprestimos/{id}")
    public ResponseEntity<EmprestimoResponse> atualizarEmprestimo(
            @PathVariable Long id,
            @Valid @RequestBody EmprestimoRequest emprestimo) {

        Emprestimo dados = new Emprestimo(
                emprestimo.getValor(),
                emprestimo.getPrazo());

        Optional<Emprestimo> emprestimoAtualizado = emprestimoService.atualizarEmprestimo(
                id,
                dados,
                emprestimo.getClienteId());

        if (emprestimoAtualizado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Emprestimo emprestimoEncontrado = emprestimoAtualizado.get();

        EmprestimoResponse resposta = new EmprestimoResponse(
                emprestimoEncontrado.getId(),
                emprestimoEncontrado.getCliente() != null ? emprestimoEncontrado.getCliente().getId() : null,
                "Emprestimo atualizado com sucesso",
                emprestimoEncontrado.getValor(),
                emprestimoEncontrado.getPrazo(),
                emprestimoEncontrado.getStatus());

        return ResponseEntity.ok(resposta);

    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {

        boolean deletado = clienteService.deletarCliente(id);

        if (!deletado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/emprestimos/{id}")
    public ResponseEntity<Void> deletarEmprestimo(@PathVariable Long id) {

        boolean deletarEmprestimo = emprestimoService.deletarEmprestimo(id);

        if (!deletarEmprestimo) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
