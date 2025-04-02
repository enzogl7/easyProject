package com.easyproject.ogl.dto;

import com.easyproject.ogl.model.UserRole;

public record RegisterDTO(String nome, String sobrenome, String email, String password, String organizacao, UserRole role) {
}
