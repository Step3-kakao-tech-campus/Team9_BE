package com.kakao.linknamu.core.encryption;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Base64;


@Slf4j
@Component
public class AesEncryption {

	@Value("${AES_SECRET_KEY}")
	private String secretKey;


	private static final String ALGORITHM = EncryptionAlgorithm.AES.getAlgorithm();
	private static final String MAIN_ALGORITHM = EncryptionAlgorithm.AES.getMainAlgorithm();
	private static final int GCM_IV_BYTE = EncryptionAlgorithm.AES.getIvByteLength();
	private static final int GCM_TAG_BIT = EncryptionAlgorithm.AES.getTagBitLength();

	public String encode(String plainString) {
		try {
			byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			//암호화 작업초기화에 필요한 secretKeySpec생성
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, MAIN_ALGORITHM);

			// 랜덤한 GCM 초기화 벡터 생성
			byte[] iv = new byte[GCM_IV_BYTE];
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(iv);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// GCM 모드에 필요한 초기화 벡터(iv)와
			// 태그 길이를 설정하는 부분입니다.
			// 이렇게 설정된 gcmParameterSpec는 암호화 작업을 초기화할 때 사용됩니다.
			// 이것은 데이터의 무결성을 확인하고,
			// 암호문을 생성할 때 초기화 벡터 및 태그 길이를 정의하는 데 중요한 역할을 합니다.
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_BIT, iv);

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
			//workSpaceId를 앞서 설정한 암호화 방식으로 암호화된 바이트배열을 생성
			byte[] encryptedWorkSpaceIdBytes = cipher.doFinal(plainString.getBytes("UTF-8"));
			// 암호문과 IV를 Base64로 인코딩하여 반환
			String encodedBase64 = Base64.getEncoder().encodeToString(iv)
				+ ":"
				+ Base64.getEncoder().encodeToString(encryptedWorkSpaceIdBytes);
			return encodedBase64.replaceAll("/", "-");


		} catch (InvalidKeyException e) {
			throw new Exception400(EncryptionExceptionStatus.ENCRYPTION_INVALID_KEY);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(EncryptionExceptionStatus.ENCRYPTION_SERVER_ERROR);
		}
	}


	public String decode(String encodedString) {
		try {
			byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, MAIN_ALGORITHM);
			String[] parts = encodedString.replaceAll("-", "/").split(":");
			//복호화는 암호화의 역순. 우선 Base64 디코드

			byte[] iv = Base64.getDecoder().decode(parts[0]);
			//base64로 인코딩된 문자중에 /가 존재할 경우, path variable로 입력받을시 경로라고 인식되어 오류 발생함
			//이를 해결하기위해 base64에 없는 문자인 -(마이너스)로 /를 대체함
			//그리고 받은 link에서 -를 /로 다시 대체하고 복호화 진행.
			byte[] cipherBytes = Base64.getDecoder().decode(parts[1]);


			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//base64 복호화된 초기화 벡터 이용해서 GCMParameterSpec생성
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_BIT, iv);
			//복호화 작업초기화 과정. 복호화모드, 가지고있던 비밀키로 SecretKeySpec생성, 앞서 만든 GCMParameterSpec이용
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
			//복호화 진행
			byte[] decryptedBytes = cipher.doFinal(cipherBytes);
			//utf-8 인코딩 형식에 맞는 원본 문자열로 변경
			String plainString = new String(decryptedBytes, "UTF-8");

			return plainString;
		} catch (InvalidKeyException e) {
			throw new Exception400(EncryptionExceptionStatus.ENCRYPTION_INVALID_KEY);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(EncryptionExceptionStatus.ENCRYPTION_SERVER_ERROR);
		}
	}


}
