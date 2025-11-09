package com.mobile_app_server.service;

import com.mobile_app_server.dto.UserLoginDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(UserLoginDto userLoginDto);
}
