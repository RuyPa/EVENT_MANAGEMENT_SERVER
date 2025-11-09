package com.mobile_app_server.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface SignatureService {

    public String signFile(MultipartFile file) throws Exception;

    public void signPdfFile(java.nio.file.Path inputPdf, java.nio.file.Path signedPdf) throws Exception;
}
