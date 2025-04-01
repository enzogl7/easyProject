package com.easyproject.ogl.repository;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    List<Projeto> findAllByUsuario(User usuario);

    Projeto findById(Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(p) FROM projetos p WHERE previsao_fim BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days' AND p.usuario = :idUsuario")
    Long getProjetosProximosDataFinalizacao(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT COUNT(p) FROM Projetos p WHERE p.responsavel IS NULL AND p.usuario = :idUsuario", nativeQuery = true)
    Long countProjetosNaoAtribuidosByUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT COUNT(p) FROM projetos p where p.status = 'Em andamento' and p.usuario = :idUsuario", nativeQuery = true)
    Long countProjetosEmAndamentoByUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT COUNT(p) FROM projetos p where p.status = 'Concluido' and p.usuario = :idUsuario", nativeQuery = true)
    Long countProjetosConcluidosByUsuario(@Param("idUsuario") Long idUsuario);

    List<Projeto> findAllByCliente_Id(Long clienteId);

    List<Projeto> findAllByResponsavel(Responsavel responsavel);
}
