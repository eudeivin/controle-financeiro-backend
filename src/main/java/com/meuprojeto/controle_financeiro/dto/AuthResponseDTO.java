package com.meuprojeto.controle_financeiro.dto;

public record AuthResponseDTO(
        String token,
        Long usuarioId,
        String nome,
        String email
) {}