package com.meuprojeto.controle_financeiro.dto;

import java.math.BigDecimal;

public record ResumoMensalDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {}