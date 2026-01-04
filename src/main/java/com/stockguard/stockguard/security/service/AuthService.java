package com.stockguard.stockguard.security.service;

import com.stockguard.stockguard.security.dto.request.LoginRequest;
import com.stockguard.stockguard.security.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void logout();
}
