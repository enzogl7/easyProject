package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.Subtarefa;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.SubtarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubtarefaService {
    @Autowired
    private SubtarefaRepository subtarefaRepository;

    public void salvar(Subtarefa subtarefa) {
        subtarefaRepository.save(subtarefa);
    }

    public List<Subtarefa> findAllByUsuario(User usuario) {
        return subtarefaRepository.findAllByUsuario(usuario);
    }

    public Subtarefa findById(Long idSubtarefa) {
        return subtarefaRepository.findById(idSubtarefa).get();
    }

    public void excluir(Subtarefa subtarefa) {
        subtarefaRepository.delete(subtarefa);
    }

    public List<Projeto> findAllProjetosByUsuario(Long usuarioId) {
        return subtarefaRepository.getProjetosByUsuario(usuarioId);
    }

    public List<Subtarefa> findAllByProjetoId(Projeto projeto) {
        return subtarefaRepository.findAllByProjeto(projeto);
    }
}
