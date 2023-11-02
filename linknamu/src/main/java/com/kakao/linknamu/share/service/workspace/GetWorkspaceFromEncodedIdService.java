package com.kakao.linknamu.share.service.workspace;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.encryption.AESEncryption;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetWorkspaceFromEncodedIdService {
	private final WorkspaceService workspaceService;
	private final AESEncryption aesEncryption;

	public GetWorkSpaceFromLinkResponseDto getWorkspace(String encodedId) {
		String workSpaceId = aesEncryption.decode(encodedId);
		Long id = Long.parseLong(workSpaceId);
		Workspace workspace = workspaceService.getWorkspaceById(id);

		List<String> links = new ArrayList<>();
		for (Category category : workspace.getCategorySet()) {
			String encodedCategoryId = aesEncryption.encode(category.getCategoryId().toString());
			links.add(encodedCategoryId);
		}

		return new GetWorkSpaceFromLinkResponseDto(workspace, links);

	}
}
