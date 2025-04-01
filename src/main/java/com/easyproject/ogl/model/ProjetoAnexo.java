package com.easyproject.ogl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projeto_anexos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjetoAnexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeArquivo;
    private String caminhoArquivo;
    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
}
