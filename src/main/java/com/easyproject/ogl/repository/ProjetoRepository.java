package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    List<Projeto> findAllByUsuario(User usuario);

    Projeto findById(Long id);
}
