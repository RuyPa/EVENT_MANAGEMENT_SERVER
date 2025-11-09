package com.mobile_app_server.dto;

public class SignResponse {

    private String fileName;
    private String signature;

    public SignResponse(String fileName, String signature) {
        this.fileName = fileName;
        this.signature = signature;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSignature() {
        return signature;
    }
}
