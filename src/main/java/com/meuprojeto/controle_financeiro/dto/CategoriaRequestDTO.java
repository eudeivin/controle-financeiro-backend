package com.meuprojeto.controle_financeiro.dto;

import com.meuprojeto.controle_financeiro.entity.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O tipo é obrigatório")
        TipoTransacao tipo
) {}