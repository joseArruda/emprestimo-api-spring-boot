package com.jose.emprestimo_api.dto;

import com.jose.emprestimo_api.entity.StatusEmprestimo;

public class EmprestimoResponse {
    private Long id;
    private Long clienteId;
    private String mensagem;
    private double valor;
    private int prazo;
    private StatusEmprestimo status;

    public EmprestimoResponse(
            Long id,
            Long clienteId,
            String mensagem,
            double valor,
            int prazo,
            StatusEmprestimo status) {

        this.id = id;
        this.clienteId = clienteId;
        this.mensagem = mensagem;
        this.valor = valor;
        this.prazo = prazo;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public double getValor() {
        return valor;
    }

    public int getPrazo() {
        return prazo;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

}
