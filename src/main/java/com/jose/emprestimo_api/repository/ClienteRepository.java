package com.jose.emprestimo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jose.emprestimo_api.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
