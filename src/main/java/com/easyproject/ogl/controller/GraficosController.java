package com.easyproject.ogl.controller;

import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.services.ProjetoService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GraficosController {

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private UserService userService;

    @GetMapping("/graficos")
    public String graficosPage(Model model) {
        model.addAttribute("nao_atribuidos", projetoService.countProjetosNaoAtribuidosByUsuario(userService.getUsuarioLogado().getId()));
        model.addAttribute("em_andamento", projetoService.countProjetosEmAndamentoByUsuario(userService.getUsuarioLogado().getId()));
        model.addAttribute("em_atraso", projetoService.getProjetosProximosDataFinalizacao(userService.getUsuarioLogado().getId()));
        model.addAttribute("concluidos", projetoService.countProjetosConcluidosByUsuario(userService.getUsuarioLogado().getId()));
        return "/graficos/graficos";
    }

    @GetMapping("/obterdados")
    @ResponseBody
    public Map<String, Object> obterDadosGraficos() {
        Map<String, Object> dados = new HashMap<>();

        List<Projeto> projetosTotal = projetoService.findAllByUsuario(userService.getUsuarioLogado());
        dados.put("projetosTotal", projetosTotal);

        return dados;
    }
}
