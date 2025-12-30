package com.stockguard.stockguard.security.service.impl;

import com.stockguard.stockguard.security.dto.request.LoginRequest;
import com.stockguard.stockguard.security.dto.response.AuthResponse;
import com.stockguard.stockguard.security.repository.UserRepository;
import com.stockguard.stockguard.security.service.AuthService;
import com.stockguard.stockguard.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken(authentication.getName());

            return new AuthResponse(token);
        } catch(BadCredentialsException ex) {
            throw new BadCredentialsException ("Invalid username or password");
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
