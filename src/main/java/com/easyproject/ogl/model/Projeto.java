package com.easyproject.ogl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    @JsonIgnore
    private LocalDate dataAlteracao;

    private LocalDate dataInicio;
    private LocalDate previsaoFim;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    private String status;
    private String prioridade;
    private boolean iniciado;

    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    @JsonIgnore
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "responsavel")
    @JsonIgnore
    private Responsavel responsavel;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProjetoAnexo> anexos;

    @ManyToOne
    @JoinColumn(name = "solicitante")
    private Solicitante solicitante;
}
