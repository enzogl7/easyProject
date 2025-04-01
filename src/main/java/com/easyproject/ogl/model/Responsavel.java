package com.easyproject.ogl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "responsaveis")
@Table(name = "responsaveis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToMany
    @JoinColumn(name = "projetos")
    private List<Projeto> projetos;
    @ManyToOne
    private User usuario;
}
