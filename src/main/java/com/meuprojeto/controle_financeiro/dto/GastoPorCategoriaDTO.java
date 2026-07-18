package com.meuprojeto.controle_financeiro.dto;

import java.math.BigDecimal;

public record GastoPorCategoriaDTO(
        String categoriaNome,
        BigDecimal total
) {}