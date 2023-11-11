package com.kakao.linknamu.core.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsoupResult {
	private String title;
	private String imageUrl;

	public JsoupResult() {
	}

	public JsoupResult(String title, String imageUrl) {
		this.title = title;
		this.imageUrl = imageUrl;
	}
}
