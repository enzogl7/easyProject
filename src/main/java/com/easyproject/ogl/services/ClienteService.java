package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Cliente;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public List<Cliente> findAllByUsuario(User usuario) {
        return clienteRepository.findAllByUsuario(usuario);
    }
}
