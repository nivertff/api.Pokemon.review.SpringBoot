package com.pokemonReview.api.controllers;

import com.pokemonReview.api.Dto.LoginDto;
import com.pokemonReview.api.Dto.RegisterDto;
import com.pokemonReview.api.models.Roles;
import com.pokemonReview.api.models.UserEntity;
import com.pokemonReview.api.repository.RoleRepository;
import com.pokemonReview.api.repository.UserRepository;
import com.pokemonReview.api.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("USer is taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Roles roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User register succes", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("user sigder saccess", HttpStatus.OK);
    }


}
