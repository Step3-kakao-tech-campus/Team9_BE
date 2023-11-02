package com.kakao.linknamu.share.service.workspace;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.core.encryption.AESEncryption;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CreateLinkFromWorkspaceService {

	private final AESEncryption aesEncryption;
	private final WorkspaceService workspaceService;
	private static final String DOMAIN = "https://www.linknamu.com/share-link/workspace/share?workspace=";

	public String createLink(Long workSpaceId) {
		workspaceService.getWorkspaceById(workSpaceId);
		String encodedString = aesEncryption.encode(workSpaceId.toString());
		String link = DOMAIN + encodedString;
		return link;
	}
}
