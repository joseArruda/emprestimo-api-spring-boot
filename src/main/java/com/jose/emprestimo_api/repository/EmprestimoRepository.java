package com.jose.emprestimo_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jose.emprestimo_api.entity.Emprestimo;
import com.jose.emprestimo_api.entity.StatusEmprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByClienteId(Long clienteId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);
}
