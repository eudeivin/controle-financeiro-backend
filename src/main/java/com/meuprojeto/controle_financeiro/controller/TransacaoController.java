package com.meuprojeto.controle_financeiro.controller;

import com.meuprojeto.controle_financeiro.dto.TransacaoRequestDTO;
import com.meuprojeto.controle_financeiro.dto.TransacaoResponseDTO;
import com.meuprojeto.controle_financeiro.security.UsuarioAutenticado;
import com.meuprojeto.controle_financeiro.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final UsuarioAutenticado usuarioAutenticado;

    public TransacaoController(TransacaoService transacaoService, UsuarioAutenticado usuarioAutenticado) {
        this.transacaoService = transacaoService;
        this.usuarioAutenticado = usuarioAutenticado;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = transacaoService.criar(dto, usuarioAutenticado.getUsuarioId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listar() {
        return ResponseEntity.ok(transacaoService.listarPorUsuario(usuarioAutenticado.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}