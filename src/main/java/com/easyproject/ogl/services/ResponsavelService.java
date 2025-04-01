package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsavelService {
    @Autowired
    private ResponsavelRepository responsavelRepository;

    public void salvar(Responsavel responsavel) {
        responsavelRepository.save(responsavel);
    }

    public List<Responsavel> findAllByUsuario(User usuario) {
        return responsavelRepository.findAllByUsuario(usuario);
    }

    public Responsavel findById(Long id) {
        return responsavelRepository.findById(id).get();
    }

    public void excluir(Responsavel responsavel) {
        responsavelRepository.delete(responsavel);
    }
}
