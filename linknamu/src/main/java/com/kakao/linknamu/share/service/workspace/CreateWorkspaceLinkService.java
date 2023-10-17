package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu._core.encryption.AESEncryption;
import com.kakao.linknamu.share.dto.workspace.CreateWorkSpaceLinkRequestDto;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CreateWorkspaceLinkService {


    private final AESEncryption aesEncryption;
    private final WorkspaceService workspaceService;

    public String createLink(CreateWorkSpaceLinkRequestDto requestDto) {
        workspaceService.getWorkspaceById(requestDto.workSpaceId());
        String encodedString = aesEncryption.encode(requestDto.workSpaceId().toString());


        return encodedString;
    }
}
