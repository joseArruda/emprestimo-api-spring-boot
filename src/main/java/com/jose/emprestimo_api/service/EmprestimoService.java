package com.jose.emprestimo_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jose.emprestimo_api.entity.Cliente;
import com.jose.emprestimo_api.entity.Emprestimo;
import com.jose.emprestimo_api.entity.StatusEmprestimo;
import com.jose.emprestimo_api.repository.ClienteRepository;
import com.jose.emprestimo_api.repository.EmprestimoRepository;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ClienteRepository clienteRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            ClienteRepository clienteRepository) {

        this.emprestimoRepository = emprestimoRepository;
        this.clienteRepository = clienteRepository;
    }

    public StatusEmprestimo analisarEmprestimo(double valor) {
        return definirStatus(valor);
    }

    public Optional<Emprestimo> salvarEmprestimo(Emprestimo emprestimo,
            Long id) {

        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()) {
            return Optional.empty();
        }

        emprestimo.setCliente(cliente.get());

        emprestimo.setStatus(definirStatus(emprestimo.getValor()));

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);

        return Optional.of(emprestimoSalvo);
    }

    public List<Emprestimo> listarEmprestimo() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }

    public Optional<Emprestimo> atualizarEmprestimo(
            Long id,
            Emprestimo dados,
            Long clienteId) {

        Optional<Emprestimo> emprestimoExistente = emprestimoRepository.findById(id);

        if (emprestimoExistente.isEmpty()) {
            return Optional.empty();
        }

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);

        if (cliente.isEmpty()) {
            return Optional.empty();
        }

        Emprestimo emprestimo = emprestimoExistente.get();

        emprestimo.setValor(dados.getValor());
        emprestimo.setPrazo(dados.getPrazo());
        emprestimo.setCliente(cliente.get());

        if (dados.getValor() <= 50000) {
            emprestimo.setStatus(StatusEmprestimo.APROVADO);
        } else {
            emprestimo.setStatus(StatusEmprestimo.PENDENTE);
        }

        return Optional.of(emprestimoRepository.save(emprestimo));
    }

    public List<Emprestimo> listarEmprestimosPorCliente(Long clienteId) {
        return emprestimoRepository.findByClienteId(clienteId);
    }

    public List<Emprestimo> listarEmprestimoPorStatus(StatusEmprestimo status) {
        return emprestimoRepository.findByStatus(status);
    }

    private StatusEmprestimo definirStatus(double valor) {
        if (valor <= 50000) {
            return StatusEmprestimo.APROVADO;
        }
        return StatusEmprestimo.PENDENTE;
    }

    public boolean deletarEmprestimo(Long id) {
        Optional<Emprestimo> emprestimo = emprestimoRepository.findById(id);

        if (emprestimo.isEmpty()) {
            return false;
        }

        emprestimoRepository.delete(emprestimo.get());

        return true;

    }
}
