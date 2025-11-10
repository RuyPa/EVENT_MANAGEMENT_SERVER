package com.mobile_app_server.controller;

import com.mobile_app_server.dto.UserLoginDto;
import com.mobile_app_server.security.jwt.JwtUtil;
import com.mobile_app_server.service.AuthService;
import com.mobile_app_server.service.impl.KafkaProducerService;
import com.mobile_app_server.service.impl.ProcessingService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ProcessingService processingService;
    private final KafkaProducerService kafkaProducerService;

    public AuthController(AuthService authService,
                          ProcessingService processingService,
                          KafkaProducerService kafkaProducerService) {

        this.authService = authService;
        this.processingService = processingService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {

        return authService.login(userLoginDto);
    }

    @GetMapping("/process")
    public ResponseEntity<?> process() {
        List<String> records = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> "record-payload-" + i + "-some-data")
                .collect(Collectors.toList());

        // submit tasks
        processingService.processRecords(records);
        return ResponseEntity.ok().body("duy");
    }

    @GetMapping("/send-kafka")
    public ResponseEntity<?> sendKafka() {
        kafkaProducerService.sendMessage("duy dz");
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
