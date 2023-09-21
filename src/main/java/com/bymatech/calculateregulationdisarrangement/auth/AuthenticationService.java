package com.bymatech.calculateregulationdisarrangement.auth;

import com.bymatech.calculateregulationdisarrangement.domain.Role;
import com.bymatech.calculateregulationdisarrangement.domain.User;
import com.bymatech.calculateregulationdisarrangement.repository.UserRepository;
import com.bymatech.calculateregulationdisarrangement.security.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwToken)
                .build();
    }
 }
