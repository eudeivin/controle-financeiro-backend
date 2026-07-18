package com.meuprojeto.controle_financeiro.security;

import com.meuprojeto.controle_financeiro.entity.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticado {

    public Usuario getUsuario() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getUsuarioId() {
        return getUsuario().getId();
    }
}