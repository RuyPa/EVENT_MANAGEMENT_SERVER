package com.mobile_app_server.service.impl;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import com.mobile_app_server.service.SignatureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;

@Service
public class SignatureServiceImpl implements SignatureService {

    private final PrivateKey privateKey;

    @Value("${app.keystore.path}")
    private String keystorePath;

    @Value("${app.keystore.store-password}")
    private String storePassword;

    @Value("${app.keystore.key-alias}")
    private String keyAlias;

    @Value("${app.keystore.key-password}")
    private String keyPassword;

    public SignatureServiceImpl(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String signFile(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream()) {
            byte[] data = inputStream.readAllBytes();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);

            byte[] signedData = signature.sign();

            return Base64.getEncoder().encodeToString(signedData);
        }
    }

//    public void signPdfFile(java.nio.file.Path inputPdf, java.nio.file.Path signedPdf) throws Exception {
//        // 1️⃣ Load key & certificate
//        KeyStore ks = KeyStore.getInstance("PKCS12");
//        ks.load(new FileInputStream("C:\\Users\\ruy_pa_\\Downloads\\Docker\\Project\\EVENT_MANAGEMENT_SERVER\\src\\main\\resources\\signer.p12"), storePassword.toCharArray());
//
//        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
//        Certificate[] chain = ks.getCertificateChain(keyAlias);
//
//        // 2️⃣ Mở file PDF
//        PdfReader reader = new PdfReader(inputPdf.toString());
//        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(signedPdf.toString()),
//                new StampingProperties().useAppendMode());
//
//        // 3️⃣ Tạo vùng chữ ký hiển thị (tuỳ chọn)
//        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
//        appearance.setReason("Digital signing demo");
//        appearance.setLocation("Hà Nội, Việt Nam");
//        appearance.setReuseAppearance(false);
//
//        // Hiển thị chữ ký ở góc dưới bên phải trang 1
//        appearance.setPageRect(new com.itextpdf.kernel.geom.Rectangle(400, 50, 150, 100))
//                .setPageNumber(1);
//
//        signer.setFieldName("sig_1");
//
//        // 4️⃣ Dùng iText để ký
//        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "SunRsaSign");
//        IExternalDigest digest = new BouncyCastleDigest();
//
//        // 5️⃣ Ký PDF
//        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);
//
//        System.out.println("✅ File PDF đã được ký: " + signedPdf);
//    }

    public void signPdfFile(Path inputPdf, Path signedPdf) throws Exception {
        // 1️⃣ Load key & certificate
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("C:\\Users\\ruy_pa_\\Downloads\\Docker\\Project\\EVENT_MANAGEMENT_SERVER\\src\\main\\resources\\signer.p12"), storePassword.toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
        Certificate[] chain = ks.getCertificateChain(keyAlias);

        // 2️⃣ Mở file PDF
        PdfReader reader = new PdfReader(inputPdf.toString());
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(signedPdf.toString()),
                new StampingProperties().useAppendMode());

        // 3️⃣ Thiết lập vùng chữ ký
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance.setReason("Ký xác nhận tài liệu");
        appearance.setLocation("Hà Nội, Việt Nam");
        appearance.setReuseAppearance(false);

        // 👉 Thêm dòng chữ hiển thị
        appearance.setLayer2Text("da ky boi duy dz, tk huy sida");
        appearance.setLayer2Font(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        appearance.setLayer2FontSize(8f);
        // 👉 Tuỳ chọn: gắn thêm ảnh chữ ký nếu có
        // ImageData image = ImageDataFactory.create("C:\\Users\\ruy_pa_\\Downloads\\signature.png");
        // appearance.setImage(image);
        // appearance.setImageScale(-1); // tự động fit vùng hiển thị

        // 👉 Xác định vùng hiển thị chữ ký (tọa độ theo đơn vị point)
        appearance.setPageRect(new com.itextpdf.kernel.geom.Rectangle(400, 50, 150, 100))
                .setPageNumber(1);

        signer.setFieldName("sig_1");

        // 4️⃣ Ký PDF
        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "SunRsaSign");
        IExternalDigest digest = new BouncyCastleDigest();

        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);

        System.out.println("✅ File PDF đã được ký và hiển thị: " + signedPdf);
    }

}
