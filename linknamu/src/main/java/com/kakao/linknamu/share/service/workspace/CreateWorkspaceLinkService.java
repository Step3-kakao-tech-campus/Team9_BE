package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu._core.encryption.AESEncryption;
import com.kakao.linknamu.share.dto.CreateWorkSpaceLinkRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CreateWorkspaceLinkService {


    private final AESEncryption aesEncryption;

    public String createLink(CreateWorkSpaceLinkRequestDto requestDto) {
        

        String encodedString = aesEncryption.encode(requestDto.workSpaceId().toString());


        return encodedString;
    }
}
