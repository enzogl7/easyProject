package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.ProjetoDTO;
import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.services.ClienteService;
import com.easyproject.ogl.services.ProjetoService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/projetos")
public class ProjetosController {

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClienteService clienteService;

    @RequestMapping("/lista")
    public String projetosHome(Model model) {
        model.addAttribute("projetos", projetoService.findAllByUsuario(userService.getUsuarioLogado()));
        model.addAttribute("clientes", clienteService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/projetos/projetos";
    }

    @RequestMapping("/novo")
    public String novoProjeto(Model model) {
        model.addAttribute("clientes", clienteService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/projetos/novoProjeto";
    }

    @PostMapping("/criar")
    public ResponseEntity criarProjeto(@RequestBody ProjetoDTO projetoData) {
        Projeto projeto = new Projeto();

        projeto.setNome(projetoData.nomeProjeto());
        projeto.setDescricao(projetoData.descricaoProjeto());
        projeto.setDataCadastro(LocalDate.now());
        if (projetoData.iniciado()) {
            projeto.setIniciado(true);
            projeto.setDataInicio(LocalDate.parse(projetoData.dataInicio()));
        }
        projeto.setPrevisaoFim(LocalDate.parse(projetoData.dataFim()));
        projeto.setCliente(clienteService.findById(Long.valueOf(projetoData.cliente())));
        projeto.setStatus(projetoData.status());
        projeto.setPrioridade(projetoData.prioridade());
        projeto.setUsuario(userService.getUsuarioLogado());
        projetoService.salvar(projeto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/iniciar")
    public ResponseEntity iniciarProjeto(@RequestParam("idProjeto") String idProjeto) {
        Projeto projeto = projetoService.findById(Long.valueOf(idProjeto));
        projeto.setIniciado(true);
        projeto.setDataInicio(LocalDate.now());
        projetoService.salvar(projeto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/excluir")
    public ResponseEntity excluirProjeto(@RequestParam("idProjeto") String idProjeto) {
        Projeto projeto = projetoService.findById(Long.valueOf(idProjeto));
        projetoService.excluir(projeto);
        return ResponseEntity.ok().build();
    }
}
