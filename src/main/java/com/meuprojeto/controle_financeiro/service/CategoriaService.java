package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.CategoriaRequestDTO;
import com.meuprojeto.controle_financeiro.dto.CategoriaResponseDTO;
import com.meuprojeto.controle_financeiro.entity.Categoria;
import com.meuprojeto.controle_financeiro.entity.Usuario;
import com.meuprojeto.controle_financeiro.exception.RegraNegocioException;
import com.meuprojeto.controle_financeiro.repository.CategoriaRepository;
import com.meuprojeto.controle_financeiro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setTipo(dto.tipo());
        categoria.setUsuario(usuario);

        categoria = categoriaRepository.save(categoria);

        return toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> listarPorUsuario(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void deletar(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo()
        );
    }
}