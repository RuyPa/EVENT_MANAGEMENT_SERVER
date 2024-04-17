package com.mobile_app_server.controller;


import com.mobile_app_server.service.CateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CateController {

    private final CateService cateService;

    @GetMapping()
    public ResponseEntity<?> getAllCate(){
        return new ResponseEntity<>(cateService.getAllCate(), HttpStatus.OK);
    }
}
