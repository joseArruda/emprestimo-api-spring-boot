package com.jose.emprestimo_api.dto;

public class ClienteResponse {

    private Long id;
    private String nome;
    private String email;
    private int idade;
    private double salario;

    public ClienteResponse(
            Long id,
            String nome,
            String email,
            int idade,
            double salario) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.idade = idade;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public int getIdade() {
        return idade;
    }

    public double getSalario() {
        return salario;
    }
}
