package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.ProjetoDTO;
import com.easyproject.ogl.model.Cliente;
import com.easyproject.ogl.model.Projeto;
import com.easyproject.ogl.model.ProjetoAnexo;
import com.easyproject.ogl.model.Responsavel;
import com.easyproject.ogl.repository.ProjetoAnexoRepository;
import com.easyproject.ogl.services.ClienteService;
import com.easyproject.ogl.services.ProjetoService;
import com.easyproject.ogl.services.ResponsavelService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private static final String DIRETORIO_UPLOAD = "/uploads/projetos/";

    @ModelAttribute
    public void adicionarClientesEResponsaveisAoModel(Model model) {
        List<Cliente> clientes = clienteService.findAllByUsuario(userService.getUsuarioLogado());
        List<Responsavel> responsaveis = responsavelService.findAllByUsuario(userService.getUsuarioLogado());
        clientes.sort(Comparator.comparing(Cliente::getNome, String::compareToIgnoreCase));
        responsaveis.sort(Comparator.comparing(Responsavel::getNome, String::compareToIgnoreCase));

        model.addAttribute("clientes", clientes);
        model.addAttribute("responsaveis", responsaveis);
    }

    @RequestMapping("/lista")
    public String projetosHome(Model model) {
        model.addAttribute("projetos", projetoService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/projetos/projetos";
    }

    @RequestMapping("/novo")
    public String novoProjeto() {
        return "/projetos/novoProjeto";
    }

    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarProjeto(@ModelAttribute ProjetoDTO projetoData) throws IOException {
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
        projeto.setResponsavel((projetoData.responsavel() != null && !projetoData.responsavel().isEmpty()) ?
                responsavelService.findById(Long.valueOf(projetoData.responsavel())) : null);
        projeto.setStatus(projetoData.status());
        projeto.setPrioridade(projetoData.prioridade());
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
        Projeto projeto = projetoService.findById(Long.valueOf(idProjeto));
        projetoService.excluir(projeto);
        return ResponseEntity.ok().build();
    }

    private void salvarAnexosProjeto(Projeto projeto, List<MultipartFile> anexos) throws IOException {
        String diretorioUpload = System.getProperty("user.dir") + "/uploads/projetos/";
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
}
