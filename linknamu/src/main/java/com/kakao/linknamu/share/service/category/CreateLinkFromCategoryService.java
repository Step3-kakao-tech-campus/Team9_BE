package com.kakao.linknamu.share.service.category;

import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.encryption.AesEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CreateLinkFromCategoryService {

	private final AesEncryption aesEncryption;
	private final CategoryService categoryService;
	private static final String DOMAIN = "https://www.linknamu.com/share-link/category/share?category=";

	public String createLink(Long categoryId) {
		categoryService.findById(categoryId);
		String encodedString = aesEncryption.encode(categoryId.toString());
		String link = DOMAIN + encodedString;
		return link;
	}
}
