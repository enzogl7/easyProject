package com.easyproject.ogl.services;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Long getProjetosProximosDataFinalizacao(Long idUsuario) {
        return projetoRepository.getProjetosProximosDataFinalizacao(idUsuario);
    }

    public Long countProjetosNaoAtribuidosByUsuario(Long idUsuario) {
        return projetoRepository.countProjetosNaoAtribuidosByUsuario(idUsuario);
    }

    public Long countProjetosEmAndamentoByUsuario(Long idUsuario) {
        return projetoRepository.countProjetosEmAndamentoByUsuario(idUsuario);
    }

    public Long countProjetosConcluidosByUsuario(Long idUsuario) {
        return projetoRepository.countProjetosConcluidosByUsuario(idUsuario);
    }
    public List<Projeto> findAllByCliente(Long clienteId) {
        return projetoRepository.findAllByCliente_Id(clienteId);
    }

    public List<Projeto> findByResponsavel(Responsavel responsavel) {
        return projetoRepository.findAllByResponsavel(responsavel);
    }
}
