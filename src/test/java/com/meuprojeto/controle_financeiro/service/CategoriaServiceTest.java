package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.CategoriaRequestDTO;
import com.meuprojeto.controle_financeiro.dto.CategoriaResponseDTO;
import com.meuprojeto.controle_financeiro.entity.Categoria;
import com.meuprojeto.controle_financeiro.entity.TipoTransacao;
import com.meuprojeto.controle_financeiro.entity.Usuario;
import com.meuprojeto.controle_financeiro.exception.RegraNegocioException;
import com.meuprojeto.controle_financeiro.repository.CategoriaRepository;
import com.meuprojeto.controle_financeiro.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoriaComSucesso() {
        // Arrange (preparação do cenário)
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CategoriaRequestDTO dto = new CategoriaRequestDTO("Alimentação", TipoTransacao.DESPESA);

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(10L);
        categoriaSalva.setNome("Alimentação");
        categoriaSalva.setTipo(TipoTransacao.DESPESA);
        categoriaSalva.setUsuario(usuario);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);

        // Act (execução)
        CategoriaResponseDTO resultado = categoriaService.criar(dto, usuarioId);

        // Assert (verificação)
        assertThat(resultado.id()).isEqualTo(10L);
        assertThat(resultado.nome()).isEqualTo("Alimentação");
        assertThat(resultado.tipo()).isEqualTo(TipoTransacao.DESPESA);

        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        Long usuarioId = 99L;
        CategoriaRequestDTO dto = new CategoriaRequestDTO("Lazer", TipoTransacao.DESPESA);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.criar(dto, usuarioId))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Usuário não encontrado");

        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deveListarCategoriasDoUsuario() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNome("Transporte");
        categoria1.setTipo(TipoTransacao.DESPESA);
        categoria1.setUsuario(usuario);

        Categoria categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNome("Salário");
        categoria2.setTipo(TipoTransacao.RECEITA);
        categoria2.setUsuario(usuario);

        when(categoriaRepository.findByUsuarioId(usuarioId))
                .thenReturn(List.of(categoria1, categoria2));

        List<CategoriaResponseDTO> resultado = categoriaService.listarPorUsuario(usuarioId);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("Transporte");
        assertThat(resultado.get(1).nome()).isEqualTo("Salário");
    }
}