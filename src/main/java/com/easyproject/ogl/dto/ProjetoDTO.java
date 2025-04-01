package com.easyproject.ogl.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProjetoDTO(String nomeProjeto, String descricaoProjeto, String dataInicio, String dataFim,
                         String responsavel, String cliente, String status, String prioridade,
                         boolean iniciado, List<MultipartFile> anexos) {
}
