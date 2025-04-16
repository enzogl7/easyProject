package com.easyproject.ogl.controller;

import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String homePage(Model model) {
        model.addAttribute("usuario", userService.getUsuarioLogado());
        return "/home/dashboard";
    }

}
