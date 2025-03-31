package com.easyproject.ogl.services;

import com.easyproject.ogl.model.User;
import com.easyproject.ogl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return (User) userRepository.findByEmail(username);
    }
}
