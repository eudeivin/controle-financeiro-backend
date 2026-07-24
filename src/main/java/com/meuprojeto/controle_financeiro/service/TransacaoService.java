package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.TransacaoRequestDTO;
import com.meuprojeto.controle_financeiro.dto.TransacaoResponseDTO;
import com.meuprojeto.controle_financeiro.entity.Categoria;
import com.meuprojeto.controle_financeiro.entity.Transacao;
import com.meuprojeto.controle_financeiro.entity.Usuario;
import com.meuprojeto.controle_financeiro.exception.RegraNegocioException;
import com.meuprojeto.controle_financeiro.repository.CategoriaRepository;
import com.meuprojeto.controle_financeiro.repository.TransacaoRepository;
import com.meuprojeto.controle_financeiro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            CategoriaRepository categoriaRepository,
                            UsuarioRepository usuarioRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public TransacaoResponseDTO criar(TransacaoRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));

        // Regra de negócio: a categoria precisa pertencer ao usuário que está criando a transação
        if (!categoria.getUsuario().getId().equals(usuarioId)) {
            throw new RegraNegocioException("Essa categoria não pertence ao usuário informado");
        }

        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.descricao());
        transacao.setValor(dto.valor());
        transacao.setData(dto.data());
        transacao.setTipo(dto.tipo());
        transacao.setCategoria(categoria);
        transacao.setUsuario(usuario);

        transacao = transacaoRepository.save(transacao);

        return toResponseDTO(transacao);
    }

    public List<TransacaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return transacaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void deletar(Long id) {
        transacaoRepository.deleteById(id);
    }

    private TransacaoResponseDTO toResponseDTO(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo(),
                transacao.getCategoria().getNome()
        );
    }

    public List<TransacaoResponseDTO> listarPorUsuarioEPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return transacaoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}