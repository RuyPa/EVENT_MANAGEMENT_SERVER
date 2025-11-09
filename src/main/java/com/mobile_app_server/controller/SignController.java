package com.mobile_app_server.controller;

import com.mobile_app_server.dto.SignResponse;
import com.mobile_app_server.service.SignatureService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/sign")
public class SignController {

    private final SignatureService signService;

    public SignController(SignatureService signService) {
        this.signService = signService;
    }

    @PostMapping
    public SignResponse sign(@RequestParam("file") MultipartFile file) throws Exception {
        String signature = signService.signFile(file);
        return new SignResponse(file.getOriginalFilename(), signature);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/view")
    public ResponseEntity<ResourceRegion> getPdf(
            @RequestParam("path") String path,
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) throws IOException {

        // Convert backslashes (if user sends Windows-style)
        path = path.replace("\\", "/");

        File file = new File(path);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        UrlResource resource = new UrlResource(file.toURI());
        long contentLength = resource.contentLength();
        final long CHUNK_SIZE = 1_000_000;

        ResourceRegion region;
        if (rangeHeader != null) {
            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty()
                    ? Long.parseLong(ranges[1])
                    : Math.min(start + CHUNK_SIZE, contentLength - 1);
            long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
            region = new ResourceRegion(resource, start, rangeLength);
        } else {
            region = new ResourceRegion(resource, 0, Math.min(CHUNK_SIZE, contentLength));
        }

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(region);
    }

    @PostMapping("/pdf")
    public ResponseEntity<Resource> signPdf(@RequestParam("file") MultipartFile file) throws Exception {
        Path inputPdf = Files.createTempFile("input_", ".pdf");
        file.transferTo(inputPdf);

        Path signedPdf = Files.createTempFile("signed_", ".pdf");

        signService.signPdfFile(inputPdf, signedPdf); // Hàm này nhúng chữ ký vào file PDF

        Resource resource = (Resource) new FileSystemResource(signedPdf);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + signedPdf.getFileName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
