package com.mobile_app_server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile_app_server.common.Global;
import com.mobile_app_server.dto.PCRFMessage;
import com.mobile_app_server.dto.PCRFRequest;
import com.mobile_app_server.dto.PartnerPCRFConfig;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.EncryptionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mobile_app_server.common.Global.listPartnerPCRFConfig;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;



    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message);
            log.info("✅ Sent message to topic {}: {}", topic, message);
        } catch (Exception e) {
            log.error("❌ Failed to send message to topic {}", topic, e);
        }
    }


    public void sendPcrfMessage(PCRFMessage pcrfMessage) {
        try {
            sendMessage(pcrfMessage.getTopic(), objectMapper.writeValueAsString(pcrfMessage.getPcrfRequest()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
