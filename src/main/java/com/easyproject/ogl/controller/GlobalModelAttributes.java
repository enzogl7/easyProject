package com.easyproject.ogl.controller;

import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void adicionarNomeOrganizacao(Model model) {
        var usuarioLogado = userService.getUsuarioLogado();

        if (usuarioLogado != null && usuarioLogado.getOrganizacao() != null) {
            model.addAttribute("nomeEmpresaNavbar", usuarioLogado.getOrganizacao());
        } else {
            model.addAttribute("nomeEmpresaNavbar", "EasyProjects");
        }
    }
}
