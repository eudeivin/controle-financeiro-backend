package com.meuprojeto.controle_financeiro.controller;

import com.meuprojeto.controle_financeiro.dto.CategoriaRequestDTO;
import com.meuprojeto.controle_financeiro.dto.CategoriaResponseDTO;
import com.meuprojeto.controle_financeiro.security.UsuarioAutenticado;
import com.meuprojeto.controle_financeiro.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final UsuarioAutenticado usuarioAutenticado;

    public CategoriaController(CategoriaService categoriaService, UsuarioAutenticado usuarioAutenticado) {
        this.categoriaService = categoriaService;
        this.usuarioAutenticado = usuarioAutenticado;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.criar(dto, usuarioAutenticado.getUsuarioId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listarPorUsuario(usuarioAutenticado.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}