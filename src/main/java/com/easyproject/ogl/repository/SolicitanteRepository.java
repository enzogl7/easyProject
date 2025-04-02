package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Solicitante;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
    List<Solicitante> findAllByUsuario(User usuario);
}
