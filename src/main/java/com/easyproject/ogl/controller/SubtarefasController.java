package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.SubtarefaDTO;
import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.Subtarefa;
import com.easyproject.ogl.services.ProjetoService;
import com.easyproject.ogl.services.ResponsavelService;
import com.easyproject.ogl.services.SubtarefaService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SubtarefasController {

    @Autowired
    private SubtarefaService subtarefaService;
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private UserService userService;

    @GetMapping("/subtarefas")
    public String subtarefasHome(Model model) {
        List<Subtarefa> subtarefas = subtarefaService.findAllByUsuario(userService.getUsuarioLogado());

        Map<Projeto, List<Subtarefa>> subtarefasPorProjeto = subtarefas.stream()
                .collect(Collectors.groupingBy(Subtarefa::getProjeto));

        model.addAttribute("subtarefasPorProjeto", subtarefasPorProjeto);
        model.addAttribute("projetos", subtarefaService.findAllProjetosByUsuario(userService.getUsuarioLogado().getId()));
        return "/subtarefas/subtarefas";
    }

    @GetMapping("/subtarefas/obterDados")
    @ResponseBody
    public Map<String, Object> obterDadosGraficosSubtarefas() {
        Map<String, Object> dados = new HashMap<>();

        List<Subtarefa> subtarefas = subtarefaService.findAllByUsuario(userService.getUsuarioLogado());
        dados.put("subtarefasTotal", subtarefas);

        return dados;
    }

    @GetMapping("/subtarefas/novo")
    public String novaSubtarefa(Model model) {
        model.addAttribute("projetos", projetoService.findAllByUsuario(userService.getUsuarioLogado()));
        model.addAttribute("responsaveis", responsavelService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/subtarefas/nova_subtarefa";
    }

    @PostMapping("/subtarefas/criar")
    public ResponseEntity criarSubtarefa(@RequestBody SubtarefaDTO subtarefaDTO) {
        try {
            Subtarefa subtarefa = new Subtarefa();
            subtarefa.setProjeto(projetoService.findById(Long.valueOf(subtarefaDTO.projetoSubtarefa())));
            subtarefa.setNome(subtarefaDTO.nomeSubtarefa());
            subtarefa.setDescricao(subtarefaDTO.descricaoSubtarefa());
            subtarefa.setDataEntrega(LocalDate.parse(subtarefaDTO.dataEntregaSubtarefa()));
            subtarefa.setAtribuido(responsavelService.findById(Long.valueOf(subtarefaDTO.atribuidoASubtarefa())));
            subtarefa.setUsuario(userService.getUsuarioLogado());
            subtarefaService.salvar(subtarefa);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/subtarefas/excluir")
    public ResponseEntity excluirSubtarefa(@RequestParam("idSubtarefa")String idSubtarefa) {
        try {
            Subtarefa subtarefa = subtarefaService.findById(Long.valueOf(idSubtarefa));
            subtarefaService.excluir(subtarefa);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
