package com.meuprojeto.controle_financeiro.dto;

import com.meuprojeto.controle_financeiro.entity.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "A data é obrigatória")
        LocalDate data,

        @NotNull(message = "O tipo é obrigatório")
        TipoTransacao tipo,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {}