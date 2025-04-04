package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.EdicaoProjetoDTO;
import com.easyproject.ogl.dto.ProjetoDTO;
import com.easyproject.ogl.model.*;
import com.easyproject.ogl.repository.ProjetoAnexoRepository;
import com.easyproject.ogl.services.*;
import org.apache.tomcat.util.net.SSLUtilBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/projetos")
public class ProjetosController {

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private ProjetoAnexoRepository projetoAnexoRepository;
    @Value("${app.upload.dir}")
    private String diretorioUpload;
    @Autowired
    private SolicitanteService solicitanteService;
    @Autowired
    private SubtarefaService subtarefaService;

    @ModelAttribute
    public void adicionarClientesEResponsaveisAoModel(Model model) {
        List<Cliente> clientes = clienteService.findAllByUsuario(userService.getUsuarioLogado());
        List<Responsavel> responsaveis = responsavelService.findAllByUsuario(userService.getUsuarioLogado());
        List<Solicitante> solicitantes = solicitanteService.findAllByUsuario(userService.getUsuarioLogado());
        clientes.sort(Comparator.comparing(Cliente::getNome, String::compareToIgnoreCase));
        responsaveis.sort(Comparator.comparing(Responsavel::getNome, String::compareToIgnoreCase));
        solicitantes.sort(Comparator.comparing(Solicitante::getNome, String::compareToIgnoreCase));

        model.addAttribute("clientes", clientes);
        model.addAttribute("responsaveis", responsaveis);
        model.addAttribute("solicitantes", solicitantes);
    }

    @RequestMapping("/lista")
    public String projetosHome(Model model) {
        List<Projeto> projetos = projetoService.findAllByUsuario(userService.getUsuarioLogado());
        Map<Long, List<Subtarefa>> subtarefasPorProjeto = new HashMap<>();
        Map<Long, Boolean> projetoTemAtraso = new HashMap<>();

        for (Projeto projeto : projetos) {
            List<Subtarefa> subtarefas = subtarefaService.findAllByProjetoId(projeto);
            subtarefasPorProjeto.put(projeto.getId(), subtarefas);
            boolean temAtraso = subtarefas.stream()
                    .anyMatch(s -> s.getDataEntrega() != null && s.getDataEntrega().isBefore(LocalDate.now()));
            projetoTemAtraso.put(projeto.getId(), temAtraso);
        }

        model.addAttribute("projetos", projetos);
        model.addAttribute("subtarefasPorProjeto", subtarefasPorProjeto);
        model.addAttribute("projetoTemAtraso", projetoTemAtraso);
        return "/projetos/projetos";
    }


    @RequestMapping("/novo")
    public String novoProjeto() {
        return "/projetos/novoProjeto";
    }

    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarProjeto(@ModelAttribute ProjetoDTO projetoData) throws IOException {
        Projeto projeto = new Projeto();
        Solicitante solicitante = solicitanteService.findById(Long.valueOf(projetoData.solicitanteProjeto()));

        projeto.setNome(projetoData.nomeProjeto());
        projeto.setDescricao(projetoData.descricaoProjeto());
        projeto.setDataCadastro(LocalDate.now());
        if (projetoData.iniciado()) {
            projeto.setIniciado(true);
            projeto.setDataInicio(LocalDate.parse(projetoData.dataInicio()));
        }
        projeto.setPrevisaoFim(LocalDate.parse(projetoData.dataFim()));
        projeto.setSolicitante(solicitante);
        projeto.setCliente(clienteService.findById(Long.valueOf(projetoData.cliente())));
        projeto.setResponsavel((projetoData.responsavel() != null && !projetoData.responsavel().isEmpty()) ?
                responsavelService.findById(Long.valueOf(projetoData.responsavel())) : null);
        projeto.setStatus(projetoData.status());
        projeto.setPrioridade(projetoData.prioridade());
        projeto.setStatusProposta(projetoData.statusProposta());
        projeto.setUsuario(userService.getUsuarioLogado());
        projetoService.salvar(projeto);
        salvarAnexosProjeto(projeto, projetoData.anexos());
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
        try {
            Projeto projeto = projetoService.findById(Long.valueOf(idProjeto));
            List<Subtarefa> subtarefasPorProjeto = subtarefaService.findAllByProjetoId(projeto);
            if (subtarefasPorProjeto.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            projetoService.excluir(projeto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    private void salvarAnexosProjeto(Projeto projeto, List<MultipartFile> anexos) throws IOException {
        Files.createDirectories(Paths.get(diretorioUpload));
        List<ProjetoAnexo> listaAnexos = new ArrayList<>();
        for (MultipartFile anexo : anexos) {
            if (!anexo.isEmpty()) {
                String nomeArquivo = System.currentTimeMillis() + "_" + anexo.getOriginalFilename();
                String caminhoArquivo = diretorioUpload + File.separator + nomeArquivo;

                try {
                    anexo.transferTo(new File(caminhoArquivo));

                    ProjetoAnexo projetoAnexo = new ProjetoAnexo();
                    projetoAnexo.setProjeto(projeto);
                    projetoAnexo.setNomeArquivo(nomeArquivo);
                    projetoAnexo.setCaminhoArquivo(caminhoArquivo);

                    listaAnexos.add(projetoAnexo);
                    projetoAnexoRepository.saveAll(listaAnexos);
                } catch (IOException ignored) {
                }
            }
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> baixarArquivo(@PathVariable Long id) throws IOException {
        ProjetoAnexo anexo = projetoAnexoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
        Path caminho = Paths.get(anexo.getCaminhoArquivo());
        if (!Files.exists(caminho) || !Files.isReadable(caminho)) {
            throw new RuntimeException("O arquivo não pode ser lido ou não existe.");
        }
        Resource recurso = new UrlResource(caminho.toUri());
        if (!recurso.exists()) {
            throw new RuntimeException("Arquivo não encontrado.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNomeArquivo() + "\"")
                .body(recurso);
    }

    @PostMapping("/editar")
    public ResponseEntity editarProjeto(@RequestBody EdicaoProjetoDTO edicaoProjetoDTO) {
        try {
            Projeto projeto = projetoService.findById(Long.valueOf(edicaoProjetoDTO.idProjeto()));
            if (edicaoProjetoDTO.iniciadoProjeto()) {
                projeto.setIniciado(true);
                projeto.setDataInicio(LocalDate.parse(edicaoProjetoDTO.dataInicioProjeto()));
            } else {
                projeto.setIniciado(false);
                projeto.setDataInicio(null);
            }
            if (edicaoProjetoDTO.responsavelProjeto() != "") {
                projeto.setResponsavel(responsavelService.findById(Long.valueOf(edicaoProjetoDTO.responsavelProjeto())));
            } else {
                projeto.setResponsavel(null);
            }
            projeto.setNome(edicaoProjetoDTO.nomeProjeto());
            projeto.setDescricao(edicaoProjetoDTO.descricaoProjeto());
            projeto.setStatus(edicaoProjetoDTO.statusProjeto());
            projeto.setPrioridade(edicaoProjetoDTO.prioridadeProjeto());
            projeto.setCliente(clienteService.findById(Long.valueOf(edicaoProjetoDTO.clienteProjeto())));
            projeto.setPrevisaoFim(LocalDate.parse(edicaoProjetoDTO.previsaoFimProjeto()));
            projeto.setDataAlteracao(LocalDate.now());
            projeto.setStatusProposta(edicaoProjetoDTO.statusPropostaProjeto());
            projetoService.salvar(projeto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
