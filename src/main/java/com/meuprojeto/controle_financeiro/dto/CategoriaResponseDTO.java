package com.meuprojeto.controle_financeiro.dto;

import com.meuprojeto.controle_financeiro.entity.TipoTransacao;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        TipoTransacao tipo
) {}