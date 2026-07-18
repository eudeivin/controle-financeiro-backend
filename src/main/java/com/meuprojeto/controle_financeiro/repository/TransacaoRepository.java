package com.meuprojeto.controle_financeiro.repository;

import com.meuprojeto.controle_financeiro.dto.GastoPorCategoriaDTO;
import com.meuprojeto.controle_financeiro.entity.Transacao;
import com.meuprojeto.controle_financeiro.entity.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT COALESCE(SUM(t.valor), 0)
        FROM Transacao t
        WHERE t.usuario.id = :usuarioId
        AND t.tipo = :tipo
        AND t.data BETWEEN :inicio AND :fim
    """)
    BigDecimal somarPorTipoEPeriodo(@Param("usuarioId") Long usuarioId,
                                    @Param("tipo") TipoTransacao tipo,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim);

    @Query("""
        SELECT new com.meuprojeto.controle_financeiro.dto.GastoPorCategoriaDTO(t.categoria.nome, SUM(t.valor))
        FROM Transacao t
        WHERE t.usuario.id = :usuarioId
        AND t.tipo = 'DESPESA'
        AND t.data BETWEEN :inicio AND :fim
        GROUP BY t.categoria.nome
        ORDER BY SUM(t.valor) DESC
    """)
    List<GastoPorCategoriaDTO> gastosPorCategoria(@Param("usuarioId") Long usuarioId,
                                                  @Param("inicio") LocalDate inicio,
                                                  @Param("fim") LocalDate fim);
}