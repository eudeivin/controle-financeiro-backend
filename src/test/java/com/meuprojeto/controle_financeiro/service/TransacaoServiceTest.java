package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.TransacaoRequestDTO;
import com.meuprojeto.controle_financeiro.dto.TransacaoResponseDTO;
import com.meuprojeto.controle_financeiro.entity.*;
import com.meuprojeto.controle_financeiro.exception.RegraNegocioException;
import com.meuprojeto.controle_financeiro.repository.CategoriaRepository;
import com.meuprojeto.controle_financeiro.repository.TransacaoRepository;
import com.meuprojeto.controle_financeiro.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Test
    void deveCriarTransacaoComSucesso() {
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Categoria categoria = new Categoria();
        categoria.setId(5L);
        categoria.setNome("Alimentação");
        categoria.setTipo(TipoTransacao.DESPESA);
        categoria.setUsuario(usuario);

        TransacaoRequestDTO dto = new TransacaoRequestDTO(
                "Mercado", new BigDecimal("450.00"), LocalDate.of(2026, 7, 8),
                TipoTransacao.DESPESA, 5L
        );

        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setId(100L);
        transacaoSalva.setDescricao("Mercado");
        transacaoSalva.setValor(new BigDecimal("450.00"));
        transacaoSalva.setData(LocalDate.of(2026, 7, 8));
        transacaoSalva.setTipo(TipoTransacao.DESPESA);
        transacaoSalva.setCategoria(categoria);
        transacaoSalva.setUsuario(usuario);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(5L)).thenReturn(Optional.of(categoria));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);

        TransacaoResponseDTO resultado = transacaoService.criar(dto, usuarioId);

        assertThat(resultado.id()).isEqualTo(100L);
        assertThat(resultado.descricao()).isEqualTo("Mercado");
        assertThat(resultado.categoriaNome()).isEqualTo("Alimentação");
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoPertenceAoUsuario() {
        Long usuarioId = 1L;
        Long outroUsuarioId = 2L;

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(usuarioId);

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(outroUsuarioId);

        Categoria categoriaDeOutroUsuario = new Categoria();
        categoriaDeOutroUsuario.setId(5L);
        categoriaDeOutroUsuario.setUsuario(outroUsuario);

        TransacaoRequestDTO dto = new TransacaoRequestDTO(
                "Mercado", new BigDecimal("450.00"), LocalDate.of(2026, 7, 8),
                TipoTransacao.DESPESA, 5L
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioLogado));
        when(categoriaRepository.findById(5L)).thenReturn(Optional.of(categoriaDeOutroUsuario));

        assertThatThrownBy(() -> transacaoService.criar(dto, usuarioId))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Essa categoria não pertence ao usuário informado");

        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExiste() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        TransacaoRequestDTO dto = new TransacaoRequestDTO(
                "Mercado", new BigDecimal("450.00"), LocalDate.of(2026, 7, 8),
                TipoTransacao.DESPESA, 999L
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transacaoService.criar(dto, usuarioId))
                .isInstanceOf(RegraNegocioException.class);

        verify(transacaoRepository, never()).save(any());
    }
}