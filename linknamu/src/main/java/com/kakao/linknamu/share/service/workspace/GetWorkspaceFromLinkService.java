package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu._core.encryption.AESEncryption;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetWorkspaceFromLinkService {
    private final WorkspaceService workspaceService;

    private final AESEncryption aesEncryption;

    public GetWorkSpaceFromLinkResponseDto getWorkspace(String encodedWorkSpaceId) {

        String workSpaceId = aesEncryption.decode(encodedWorkSpaceId);
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
