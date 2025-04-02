package com.easyproject.ogl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "solicitantes")
@Table(name = "solicitantes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToOne
    private User usuario;
}
