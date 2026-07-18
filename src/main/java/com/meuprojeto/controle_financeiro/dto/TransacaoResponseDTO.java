package com.meuprojeto.controle_financeiro.dto;

import com.meuprojeto.controle_financeiro.entity.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        TipoTransacao tipo,
        String categoriaNome
) {}