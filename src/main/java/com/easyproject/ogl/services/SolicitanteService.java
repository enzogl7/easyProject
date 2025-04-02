package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Solicitante;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.SolicitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitanteService {
    @Autowired
    private SolicitanteRepository solicitanteRepository;

    public void salvar(Solicitante solicitante) {
        solicitanteRepository.save(solicitante);
    }

    public List<Solicitante> findAllByUsuario(User usuario) {
        return solicitanteRepository.findAllByUsuario(usuario);
    }

    public Solicitante findById(Long idSolicitante) {
        return solicitanteRepository.findById(idSolicitante).get();
    }

    public void excluir(Solicitante solicitante) {
        solicitanteRepository.delete(solicitante);
    }
}
