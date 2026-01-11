package com.mobile_app_server.controller;

import com.mobile_app_server.dto.UserLoginDto;
import com.mobile_app_server.security.jwt.JwtUtil;
import com.mobile_app_server.service.AuthService;
import com.mobile_app_server.service.impl.KafkaProducerService;
import com.mobile_app_server.service.impl.PCRFService;
import com.mobile_app_server.service.impl.ProcessingService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
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
    private final PCRFService pcrfService;

    public AuthController(AuthService authService,
                          ProcessingService processingService,
                          KafkaProducerService kafkaProducerService,
                          PCRFService pcrfService) {

        this.authService = authService;
        this.processingService = processingService;
        this.kafkaProducerService = kafkaProducerService;
        this.pcrfService = pcrfService;
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
//        kafkaProducerService.sendMessage("duy dz", "test-topic");
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping(value = "/pcrf")
    public String receiveMessage(@RequestBody String bodyXml, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String content = extractContent(bodyXml, "content");

        pcrfService.handleMessage(content);

        return "";
    }

    public static String extractContent(String xml, String elemName) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader =
                factory.createXMLStreamReader(new StringReader(xml));

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT
                    && elemName.equals(reader.getLocalName())) {

                // next() moves to CHARACTERS
                reader.next();
                return reader.getText();
            }
        }
        return null;
    }

}
