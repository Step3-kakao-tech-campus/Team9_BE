package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu._core.encryption.EncryptionAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CreateWorkspaceFromLinkService {
    @Value("${AES_SECRET_KEY}")
    private String secretKey;
    private final String ALGORITHM = EncryptionAlgorithm.AES.getAlgorithm();
    private final String MAIN_ALGORITHM = EncryptionAlgorithm.AES.getMainAlgorithm();
    private final int GCM_TAG_BIT = EncryptionAlgorithm.AES.getTagBitLength();

    public String getWorkSpace(String link) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, MAIN_ALGORITHM);

        int startIndex = link.indexOf("share/") + 6; // "share/"의 길이는 6
        String encodedUrl = link.substring(startIndex);
        String[] parts = encodedUrl.split(":");
        //복호화는 암호화의 역순. 우선 Base64 디코드
        System.out.println(parts[0]);
        System.out.println(parts[1]);
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] cipherBytes = Base64.getDecoder().decode(parts[1].replaceAll("-", "/"));


        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //base64 복호화된 초기화 벡터 이용해서 GCMParameterSpec생성
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_BIT, iv);
        //복호화 작업초기화 과정. 복호화모드, 가지고있던 비밀키로 SecretKeySpec생성, 앞서 만든 GCMParameterSpec이용
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        //복호화 진행
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        //utf-8 인코딩 형식에 맞는 원본 문자열로 변경
        String workSpaceId = new String(decryptedBytes, "UTF-8");

        return workSpaceId;


    }

}
