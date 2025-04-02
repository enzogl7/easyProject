package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.Subtarefa;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubtarefaRepository extends JpaRepository<Subtarefa, Long> {
    List<Subtarefa> findAllByUsuario(User usuario);

    @Query(value = "SELECT DISTINCT p.* FROM projetos p JOIN subtarefas s ON p.id = s.projeto_id WHERE s.usuario_id = :usuarioId ORDER BY p.nome ASC", nativeQuery = true)
    List<Projeto> getProjetosByUsuario(@Param("usuarioId") Long usuarioId);


    List<Subtarefa> findAllByProjeto(Projeto projeto);
}
