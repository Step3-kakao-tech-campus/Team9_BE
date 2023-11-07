package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu.core.encryption.AesEncryption;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CreateLinkFromWorkspaceService {

	private final AesEncryption aesEncryption;
	private final WorkspaceService workspaceService;
	private static final String URL = "/share-link/workspace/share?workspace=";

	public String createLink(Long workSpaceId) {
		workspaceService.getWorkspaceById(workSpaceId);
		String encodedString = aesEncryption.encode(workSpaceId.toString());
		String link = URL + encodedString;
		return link;
	}
}
