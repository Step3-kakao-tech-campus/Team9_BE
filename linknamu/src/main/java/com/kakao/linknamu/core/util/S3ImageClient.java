package com.kakao.linknamu.core.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.thirdparty.utils.JsoupUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

// S3에 이미지 파일을 저장할 수 있도록 하는 객체
@RequiredArgsConstructor
@Component
public class S3ImageClient {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;
	private final JsoupUtils jsoupUtils;

	public String base64ImageToS3(String base64Data, String bookmarkLink) {

		try {
			// null 이면 메타데이터에서 썸네일 가져오기 시도, 실패하면 기본 이미지 url 반환
			if (base64Data == null) {
				return jsoupUtils.getImgUrl(bookmarkLink);
			}
			byte[] byteImage = java.util.Base64.getDecoder().decode(base64Data);
			InputStream imageInputStream = getValidImageInputStream(byteImage);

			// AWS S3 저장 로직
			String fileName = "image/" + UUID.randomUUID().toString();
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("image/png");
			metadata.setContentLength(byteImage.length);
			metadata.setCacheControl("public, max-age=31536000");

			amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, imageInputStream, metadata));
			return amazonS3Client.getUrl(bucket, fileName).toString();
		} catch (IllegalArgumentException e) {
			throw new Exception400(UtilExceptionStatus.NOT_BASE64_DATA);
		}

	}

	private ByteArrayInputStream getValidImageInputStream(byte[] byteImage) {

		ByteArrayInputStream imageInputStream = new ByteArrayInputStream(byteImage);

		try {
			
			BufferedImage image = ImageIO.read(imageInputStream);

			// image인지 체크하는 로직
			if (image == null) {
				throw new Exception400(UtilExceptionStatus.IMAGE_INVALID_DATA);
			} else {
				imageInputStream.reset();
			}
		} catch (IOException exception) {
			throw new Exception400(UtilExceptionStatus.IMAGE_UNREADABLE_DATA);
		}
		return imageInputStream;
	}
}
