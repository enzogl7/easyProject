package com.easyproject.ogl.controller;

import com.easyproject.ogl.dto.AuthenticationDTO;
import com.easyproject.ogl.dto.LoginResponseDTO;
import com.easyproject.ogl.dto.RegisterDTO;
import com.easyproject.ogl.infra.security.TokenService;
import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String registerPage() {
        return "cadastro";
    }

    @PostMapping("/logar")
    public ResponseEntity logar(@RequestBody AuthenticationDTO dataLogin, HttpServletResponse response) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dataLogin.email(), dataLogin.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);  // Tempo de expiração: 1 hora

            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO dataRegister) {
        if (this.userRepository.findByEmail(dataRegister.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dataRegister.password());
        String nome = dataRegister.nome() + " " + dataRegister.sobrenome();
        User newUser = new User(nome, dataRegister.email(), encryptedPassword, dataRegister.role());

        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
