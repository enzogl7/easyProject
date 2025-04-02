package com.easyproject.ogl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "subtarefas")
@Table(name = "subtarefas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subtarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Projeto projeto;
    private String nome;
    private String descricao;
    private LocalDate dataEntrega;
    @ManyToOne
    private Responsavel atribuido;
    @ManyToOne
    private User usuario;

}
