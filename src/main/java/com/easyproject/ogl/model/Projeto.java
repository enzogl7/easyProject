package com.easyproject.ogl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "projetos")
@Table(name = "projetos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate dataAlteracao;
    private LocalDate dataInicio;
    private LocalDate previsaoFim;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    private String status;
    private String prioridade;
    private boolean iniciado;
    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    private User usuario;
    @ManyToOne
    @JoinColumn(name = "responsavel")
    private Responsavel responsavel;
}
