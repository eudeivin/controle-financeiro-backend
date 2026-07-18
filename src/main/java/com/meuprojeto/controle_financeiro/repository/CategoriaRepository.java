package com.meuprojeto.controle_financeiro.repository;

import com.meuprojeto.controle_financeiro.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByUsuarioId(Long usuarioId);
}