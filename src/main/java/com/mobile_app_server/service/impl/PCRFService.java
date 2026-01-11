package com.mobile_app_server.service.impl;

import com.mobile_app_server.common.Global;
import com.mobile_app_server.dto.PCRFMessage;
import com.mobile_app_server.dto.PCRFRequest;
import com.mobile_app_server.dto.PartnerPCRFConfig;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.EncryptionException;
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
public class PCRFService {

    private final KafkaProducerService kafkaProducerService;

    public PCRFService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    private static final String ALGO = "AES";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String PCRF_ALL = "-999";

    public void handleMessage(String content) {

        PCRFRequest pcrfRequest = mappingContent(content);
        log.info("PCRF request: {}", pcrfRequest);

        HashMap<Integer, PartnerPCRFConfig> pcrfConfigHashMap = getPartnerPCRFConfig(pcrfRequest);

        if (!ObjectUtils.isEmpty(pcrfConfigHashMap)) {
            for (Integer partnerId : pcrfConfigHashMap.keySet()) {
                PCRFMessage pcrfMessage = setPCRFMessage(pcrfRequest.clone(), pcrfConfigHashMap.get(partnerId));
                if (pcrfMessage.getSendTime() == 0) {
                    kafkaProducerService.sendPcrfMessage(pcrfMessage);
                } else {
                    Global.listPCRFDelayMessages.offer(pcrfMessage);
                }
            }
        } else {
            log.error("request: {}", pcrfRequest);
            throw new IllegalArgumentException("PCRF config is empty, not found config with userMSISDN: " + pcrfRequest.getUsrMSISDN());
        }
    }

    private static PCRFMessage setPCRFMessage(PCRFRequest pcrfRequest, PartnerPCRFConfig pcrfConfig) {
        PCRFMessage pcrfMessage = new PCRFMessage();

        if (pcrfConfig.getIsEncrypt() == 1) {
            try {
                pcrfRequest.setUsrMSISDN(encrypt(pcrfConfig.getPartnerKey(), pcrfRequest.getUsrMSISDN()));
            } catch (EncryptionException e) {
                throw new RuntimeException("Encrypt usrMSISDN failed", e);
            }
        }

        pcrfMessage.setPcrfRequest(pcrfRequest);
        pcrfMessage.setTopic(pcrfConfig.getQueueName());
        pcrfMessage.setTimeDelayConfig(pcrfConfig.getTimeDelay());
        pcrfMessage.setSendTime(pcrfConfig.getTimeDelay() <= 0 ? 0 : System.currentTimeMillis() + pcrfConfig.getTimeDelay() * 1000L);

        return pcrfMessage;
    }

    private static PCRFRequest mappingContent(String content) {

        PCRFRequest dto = new PCRFRequest();

        if (ObjectUtils.isEmpty(content)) {
            throw new IllegalArgumentException("content is empty");
        }

        Map<String, String> kvMap = Arrays.stream(content.split(","))
                .map(p -> p.split("=", 2))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(
                        kv -> kv[0].trim(),
                        kv -> kv[1].trim()
                ));

        dto.setUsrMSISDN(formatIsdn(requiredValue(kvMap, "usrMSISDN")));
        dto.setSrvName(requiredValue(kvMap, "srvName"));
        dto.setQTAVALUE(requiredValue(kvMap, "QTAVALUE"));
        dto.setSrvStartDateTime(parseDateTime(requiredValue(kvMap, "srvStartDateTime")));
        dto.setSrvEndDateTime(parseDateTime(requiredValue(kvMap, "srvEndDateTime")));

        if (dto.getSrvEndDateTime().isBefore(dto.getSrvStartDateTime())) {
            log.error("srvEndDateTime: {} is before srvStartDateTime: {}", dto.getSrvEndDateTime(), dto.getSrvStartDateTime());
            throw new IllegalArgumentException("srvEndDateTime is before srvStartDateTime");
        }

        return dto;
    }

    public static String formatIsdn(String isdn) {

        if (isdn == null)
            return null;

        isdn = isdn.replaceAll("\"", "");

        if (isdn.startsWith("0")) {
            isdn = isdn.substring(1);
        }

        if (!isdn.startsWith("84")) {
            isdn = "84" + isdn;
        }

        if (isdn.startsWith("84120")) {
            return "8470" + isdn.substring(5);
        } else if (isdn.startsWith("84121")) {
            return "8479" + isdn.substring(5);
        } else if (isdn.startsWith("84122")) {
            return "8477" + isdn.substring(5);
        } else if (isdn.startsWith("84126")) {
            return "8476" + isdn.substring(5);
        } else if (isdn.startsWith("84128")) {
            return "8478" + isdn.substring(5);
        }

        return isdn;
    }

    private static String requiredValue(Map<String, String> map, String key) {
        String value = map.get(key);

        if (ObjectUtils.isEmpty(value)) {
            log.error("Field {} is empty", key);
            throw new IllegalArgumentException(key + " is required");
        }
        return value;
    }

    private static LocalDateTime parseDateTime(String dateTime) {

        if (dateTime.length() == 10) {
            dateTime += " 00:00:00";
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    private static HashMap<Integer, PartnerPCRFConfig> getPartnerPCRFConfig(PCRFRequest pcrfRequest) {

        HashMap<Integer, PartnerPCRFConfig> partnerPCRFConfigMap = new HashMap<>();

        for (PartnerPCRFConfig pcrfConfig : listPartnerPCRFConfig) {
            if (checkPrefix(pcrfConfig, pcrfRequest.getUsrMSISDN()) && checkSuffix(pcrfConfig, pcrfRequest.getUsrMSISDN())) {
                partnerPCRFConfigMap.put(pcrfConfig.getPartnerId(), pcrfConfig);
            }
        }

        return partnerPCRFConfigMap;
    }

    private static boolean checkPrefix(PartnerPCRFConfig pcrfConfig, String usrMSISDN) {

        if (!ObjectUtils.isEmpty(pcrfConfig.getPrefix())) {
            return Arrays.asList(pcrfConfig.getPrefix()).contains(PCRF_ALL) || Arrays.stream(pcrfConfig.getPrefix()).anyMatch(usrMSISDN::startsWith);
        }

        return false;
    }

    private static boolean checkSuffix(PartnerPCRFConfig pcrfConfig, String usrMSISDN) {

        if (!ObjectUtils.isEmpty(pcrfConfig.getSuffix())) {
            return Arrays.asList(pcrfConfig.getSuffix()).contains(PCRF_ALL) || Arrays.stream(pcrfConfig.getSuffix()).anyMatch(usrMSISDN::endsWith);
        }

        return false;
    }

    public static String encrypt(String secureKey, String data) throws EncryptionException {
        Key key = null;
        try {
            key = generateKey(secureKey.getBytes());
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
        return encrypt(key, data);
    }

    public static String encrypt(Key key, String data) throws EncryptionException {
        Cipher c = null;
        try {
            c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new EncryptionException(e.getMessage());
        }
        return encrypt(c, data);
    }

    public static String encrypt(Cipher cipher, String data) throws EncryptionException {
        byte[] encVal = null;
        try {
            encVal = cipher.doFinal(data.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptionException(e.getMessage());
        }
        return Base64.getEncoder().encodeToString(encVal);

    }

    public static Key generateKey(byte[] keyValue) throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}
