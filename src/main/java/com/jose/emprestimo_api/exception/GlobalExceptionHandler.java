package com.jose.emprestimo_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErroDeValidacao(
            MethodArgumentNotValidException erro) {

        Map<String, Object> resposta = new HashMap<>();

        Map<String, String> campos = new HashMap<>();

        erro.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    campos.put(
                            fieldError.getField(),
                            fieldError.getDefaultMessage());
                });

        resposta.put("erro", "Dados inválidos");
        resposta.put("campos", campos);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> tratarErroDeConversao(
            MethodArgumentTypeMismatchException erro) {

        Map<String, String> resposta = new HashMap<>();

        resposta.put("erro", "Valor inválido");
        resposta.put("mensagem", "O valor informado para o parâmetro é inválido.");

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarErroDeIntegridade(
            DataIntegrityViolationException erro) {

        Map<String, String> resposta = new HashMap<>();

        resposta.put("erro", "Operação não permitida.");
        resposta.put(
                "mensagem",
                "Não é possível excluir este cliente porque ele possui empréstimos associados.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }
}