package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.EdicaoResponsavelDTO;
import com.easyproject.ogl.dto.ResponsavelDTO;
import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.services.ResponsavelService;
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
public class ResponsaveisController {

    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private UserService userService;

    @GetMapping("/responsaveis")
    public String responsaveisHome(Model model) {
        model.addAttribute("responsaveis", responsavelService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/responsaveis/responsaveis";
    }

    @GetMapping("/responsaveis/novo")
    public String novoResposavel() {
        return "/responsaveis/novo_responsavel";
    }

    @PostMapping("/responsaveis/criar")
    public ResponseEntity criarResposavel(@RequestBody ResponsavelDTO responsavelDTO) {
        try {
            Responsavel responsavel = new Responsavel();
            responsavel.setNome(responsavelDTO.nomeResponsavel());
            responsavel.setUsuario(userService.getUsuarioLogado());
            responsavelService.salvar(responsavel);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/responsaveis/editar")
    public ResponseEntity editarResposavel(@RequestBody EdicaoResponsavelDTO edicaoResponsavelDTO) {
        try {
            Responsavel responsavel = responsavelService.findById(Long.valueOf(edicaoResponsavelDTO.idResponsavelEdicao()));
            responsavel.setNome(edicaoResponsavelDTO.nomeResponsavelEdicao());
            responsavel.setUsuario(userService.getUsuarioLogado());
            responsavelService.salvar(responsavel);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/responsaveis/excluir")
    public ResponseEntity excluirResposavel(@RequestParam("idResponsavel") String idResponsavel) {
        try {
            Responsavel responsavel = responsavelService.findById(Long.valueOf(idResponsavel));
            responsavelService.excluir(responsavel);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
