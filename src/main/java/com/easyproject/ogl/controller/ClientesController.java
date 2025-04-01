package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.ClienteDTO;
import com.easyproject.ogl.dto.EdicaoClienteDTO;
import com.easyproject.ogl.model.Cliente;
import com.easyproject.ogl.services.ClienteService;
import com.easyproject.ogl.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientesController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UserService userService;

    @RequestMapping("/clientes")
    public String clientesHome(Model model) {
        model.addAttribute("clientes", clienteService.findAllByUsuario(userService.getUsuarioLogado()));
        return "/clientes/clientes";
    }

    @RequestMapping("/clientes/novo")
    public String clientesNovo() {
        return "/clientes/novoCliente";
    }

    @RequestMapping("/clientes/criar")
    public ResponseEntity criarCliente(@RequestBody ClienteDTO clientData) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(clientData.nomeCliente());
            cliente.setDescricao(clientData.descricaoCliente());
            cliente.setUsuario(userService.getUsuarioLogado());
            clienteService.salvar(cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/clientes/excluir")
    public ResponseEntity excluirCliente(@RequestParam("idCliente") String idCliente) {
        try {
            Cliente cliente = clienteService.findById(Long.valueOf(idCliente));
            clienteService.excluir(cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/clientes/editar")
    public ResponseEntity editarCliente(@RequestBody EdicaoClienteDTO edicaoClienteDTO) {
        try {
            Cliente cliente = clienteService.findById(Long.valueOf(edicaoClienteDTO.idClienteEdicao()));
            cliente.setNome(edicaoClienteDTO.nomeClienteEdicao());
            cliente.setDescricao(edicaoClienteDTO.descricaoClienteEdicao());
            cliente.setUsuario(userService.getUsuarioLogado());
            clienteService.salvar(cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
