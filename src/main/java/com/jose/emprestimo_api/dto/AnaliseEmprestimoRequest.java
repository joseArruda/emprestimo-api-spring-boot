package com.jose.emprestimo_api.dto;

import jakarta.validation.constraints.Positive;

public class AnaliseEmprestimoRequest {

    @Positive(message = "O valor do empréstimo deve ser maior que zero.")
    private double valor;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
