package com.easyproject.ogl.services;

import com.easyproject.ogl.model.ProjetoAnexo;
import com.easyproject.ogl.repository.ProjetoAnexoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetoAnexoService {
    @Autowired
    private ProjetoAnexoRepository projetoAnexoRepository;

}
