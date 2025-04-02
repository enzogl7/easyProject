package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.EdicaoSolicitanteDTO;
import com.easyproject.ogl.dto.SolicitanteDTO;
import com.easyproject.ogl.model.Solicitante;
import com.easyproject.ogl.services.SolicitanteService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SolicitanteController {

    @Autowired
    private UserService userService;
    @Autowired
    private SolicitanteService solicitanteService;

    @GetMapping("/solicitantes")
    public String solicitanteHome(Model model) {
        model.addAttribute("solicitantes", solicitanteService.findAllByUsuario(userService.getUsuarioLogado()));
        return "solicitantes/solicitantes";
    }

    @GetMapping("/solicitantes/novo")
    public String novoSolicitante() {
        return "solicitantes/novo_solicitante";
    }

    @PostMapping("/solicitantes/criar")
    public ResponseEntity criarSolicitante(@RequestBody SolicitanteDTO solicitanteDTO) {
        try {
            Solicitante solicitante = new Solicitante();
            solicitante.setNome(solicitanteDTO.nomeSolicitante());
            solicitante.setUsuario(userService.getUsuarioLogado());
            solicitanteService.salvar(solicitante);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/solicitantes/excluir")
    public ResponseEntity excluirSolicitante(@RequestParam("idSolicitante")String idSolicitante) {
        try {
            Solicitante solicitante = solicitanteService.findById(Long.valueOf(idSolicitante));
            solicitanteService.excluir(solicitante);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/solicitantes/editar")
    public ResponseEntity editarSolicitante(@RequestBody EdicaoSolicitanteDTO edicaoSolicitanteDTO) {
        try {
            Solicitante solicitante = solicitanteService.findById(Long.valueOf(edicaoSolicitanteDTO.idSolicitanteEdicao()));
            solicitante.setNome(edicaoSolicitanteDTO.nomeSolicitanteEdicao());
            solicitante.setUsuario(userService.getUsuarioLogado());
            solicitanteService.salvar(solicitante);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
