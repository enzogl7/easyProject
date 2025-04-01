package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {
    List<Responsavel> findAllByUsuario(User usuario);
}
