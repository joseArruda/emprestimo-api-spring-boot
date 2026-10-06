package com.jose.emprestimo_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jose.emprestimo_api.entity.Cliente;
import com.jose.emprestimo_api.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> atualizarCliente(Long id, Cliente dados) {

        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isEmpty()) {
            return Optional.empty();
        }

        Cliente cliente = clienteExistente.get();

        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setIdade(dados.getIdade());
        cliente.setSalario(dados.getSalario());

        return Optional.of(clienteRepository.save(cliente));

    }

    public boolean deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }

        clienteRepository.deleteById(id);
        return true;
    }
}
