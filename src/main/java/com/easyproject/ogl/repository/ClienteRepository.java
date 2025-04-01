package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Cliente;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findAllByUsuario(User usuario);

    Cliente findById(Long idCliente);
}
