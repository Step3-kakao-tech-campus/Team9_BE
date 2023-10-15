//package com.kakao.linknamu.share.service.workspace;
//
//import com.kakao.linknamu._core.encryption.EncryptionAlgorithm;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//@Service
//public class CreateWorkspaceFromLinkService {
//    @Value("${AES_SECRET_KEY}")
//    private String secretKey;
//    private final String ALGORITHM = EncryptionAlgorithm.AES.getAlgorithm();
//    private final String MAIN_ALGORITHM = EncryptionAlgorithm.AES.getMainAlgorithm();
//    private final int GCM_TAG_BIT = EncryptionAlgorithm.AES.getTagBitLength();
//
//    public String getWorkSpace(String link) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
//
//
//    }
//
//}
