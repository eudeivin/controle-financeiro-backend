package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.GastoPorCategoriaDTO;
import com.meuprojeto.controle_financeiro.dto.ResumoMensalDTO;
import com.meuprojeto.controle_financeiro.entity.TipoTransacao;
import com.meuprojeto.controle_financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class RelatorioService {

    private final TransacaoRepository transacaoRepository;

    public RelatorioService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public ResumoMensalDTO resumoMensal(Long usuarioId, int ano, int mes) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = YearMonth.of(ano, mes).atEndOfMonth();

        BigDecimal totalReceitas = transacaoRepository.somarPorTipoEPeriodo(
                usuarioId, TipoTransacao.RECEITA, inicio, fim);

        BigDecimal totalDespesas = transacaoRepository.somarPorTipoEPeriodo(
                usuarioId, TipoTransacao.DESPESA, inicio, fim);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new ResumoMensalDTO(totalReceitas, totalDespesas, saldo);
    }

    public List<GastoPorCategoriaDTO> gastosPorCategoria(Long usuarioId, int ano, int mes) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = YearMonth.of(ano, mes).atEndOfMonth();

        return transacaoRepository.gastosPorCategoria(usuarioId, inicio, fim);
    }
}