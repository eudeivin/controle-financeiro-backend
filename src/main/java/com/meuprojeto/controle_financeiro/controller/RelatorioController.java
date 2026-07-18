package com.meuprojeto.controle_financeiro.controller;

import com.meuprojeto.controle_financeiro.dto.GastoPorCategoriaDTO;
import com.meuprojeto.controle_financeiro.dto.ResumoMensalDTO;
import com.meuprojeto.controle_financeiro.security.UsuarioAutenticado;
import com.meuprojeto.controle_financeiro.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final UsuarioAutenticado usuarioAutenticado;

    public RelatorioController(RelatorioService relatorioService, UsuarioAutenticado usuarioAutenticado) {
        this.relatorioService = relatorioService;
        this.usuarioAutenticado = usuarioAutenticado;
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoMensalDTO> resumoMensal(
            @RequestParam int ano,
            @RequestParam int mes) {
        return ResponseEntity.ok(
                relatorioService.resumoMensal(usuarioAutenticado.getUsuarioId(), ano, mes)
        );
    }

    @GetMapping("/gastos-por-categoria")
    public ResponseEntity<List<GastoPorCategoriaDTO>> gastosPorCategoria(
            @RequestParam int ano,
            @RequestParam int mes) {
        return ResponseEntity.ok(
                relatorioService.gastosPorCategoria(usuarioAutenticado.getUsuarioId(), ano, mes)
        );
    }
}