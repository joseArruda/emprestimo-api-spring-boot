package com.jose.emprestimo_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EmprestimoRequest {
    @Positive(message = "O valor do empréstimo deve ser maior que zero.")
    private double valor;

    @Positive(message = "O prazo deve ser maior que zero.")
    private int prazo;

    @Positive(message = "O prazo deve ser maior que zero.")
    @NotNull(message = "O clienteId é obrigatório.")
    private Long clienteId;

    public Long getClienteId() {
        return clienteId;
    }

    public double getValor() {
        return valor;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

}
