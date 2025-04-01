package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    List<Projeto> findAllByUsuario(User usuario);

    Projeto findById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM projetos WHERE previsao_fim BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days'")
    List<Projeto> getProjetosProximosDataFinalizacao();

}
