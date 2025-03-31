package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public void salvar(Projeto projeto) {
        projetoRepository.save(projeto);
    }

    public List<Projeto> findAllByUsuario(User usuario) {
        return projetoRepository.findAllByUsuario(usuario);
    }

    public Projeto findById(Long id) {
        return projetoRepository.findById(id);
    }

    public void excluir(Projeto projeto) {
        projetoRepository.delete(projeto);
    }
}
