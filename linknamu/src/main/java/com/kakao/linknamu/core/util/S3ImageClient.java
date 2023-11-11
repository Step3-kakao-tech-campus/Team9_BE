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
import java.net.URL;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ImageClient {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;
	private final JsoupUtils jsoupUtils;

	public String base64ImageToS3(String base64Data) {
		try {
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
			//base64로 인코딩된 이미지파일이 아닐경우, 이미지Url인지 확인
			if (isValidImageUrl(base64Data)) {
				return base64Data;
			}
			throw new Exception400(UtilExceptionStatus.NOT_BASE64_DATA);
		}
	}

	public String base64ImageToS3(String base64Data, String bookmarkLink) {
		if (base64Data == null) {
			return jsoupUtils.getImgUrl(bookmarkLink);
		}

		return base64ImageToS3(base64Data);
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

	private Boolean isValidImageUrl(String imgUrlString) {

		try {
			URL imgUrl = new URL(imgUrlString);
			BufferedImage image = ImageIO.read(imgUrl);

			// image인지 체크하는 로직
			if (image == null) {
				throw new Exception400(UtilExceptionStatus.IMAGE_URL_INVALID);
			}
		} catch (IOException exception) {
			throw new Exception400(UtilExceptionStatus.IMAGE_UNREADABLE_URL);
		}

		return true;
	}
}
