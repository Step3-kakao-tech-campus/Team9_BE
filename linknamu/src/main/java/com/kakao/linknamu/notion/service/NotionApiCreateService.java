package com.kakao.linknamu.notion.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryReadService;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.notion.NotionExceptionStatus;
import com.kakao.linknamu.notion.dto.NotionApiRegistrationRequestDto;
import com.kakao.linknamu.notion.entity.NotionAccount;
import com.kakao.linknamu.notion.entity.NotionPage;
import com.kakao.linknamu.notion.repository.NotionAccountJPARepository;
import com.kakao.linknamu.notion.repository.NotionPageJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class NotionApiCreateService {
    private final NotionAccountJPARepository notionAccountJPARepository;
    private final NotionPageJPARepository notionPageJPARepository;
    private final WorkspaceReadService workspaceReadService;
    private final WorkspaceSaveService workspaceSaveService;
    private final CategoryService categoryService;

    private final static String DEFAULT_WORKSPACE_NAME = "Notion 연동";
    /*
        로직
        1. noticeAccount가 없다면 새로 생성
        2. 기존에 등록한 적이 있는지 확인 -> 있다면 예외처리
        3. 링크들을 연동할 워크스페이스 및 카테고리 생성
        4. noticePage 생성
     */
    public void createNotionApi(NotionApiRegistrationRequestDto requestDto,
                                User user) {

        // notionAccount가 이미 존재하다면 그대로 가져오고 아니면 새로 생성
        NotionAccount notionAccount = notionAccountJPARepository
                .findByUserIdAndAccessToken(user.getUserId(), requestDto.accessToken())
                .orElseGet(() -> {
                    NotionAccount createNotionAccount = NotionAccount.builder()
                            .token(requestDto.accessToken())
                            .user(user)
                            .build();
                    return notionAccountJPARepository.save(createNotionAccount);
                });

        if(notionPageJPARepository.existsByPageIdAndNotionAccount(requestDto.pageId(), notionAccount)) {
            throw new Exception400(NotionExceptionStatus.NOTION_ALREADY_EXIST);
        }

        // 노션 연동 워크스페이스가 있다면 해당 워크스페이스에 카테고리 생성 없으면 노션 워크스페이스 추가
        Workspace notionWorkspace = workspaceReadService.findWorkspaceByUserAndProvider(user, LinkProvider.NOTION)
                .orElseGet(() -> workspaceSaveService.createNotionWorkspace(DEFAULT_WORKSPACE_NAME, user));


        // 초기 카테고리의 이름은 노션 페이지의 ID로 지정한다.
        Category notionCategory = categoryService.findByWorkspaceIdAndCategoryName(notionWorkspace.getId(),
                requestDto.pageId()).orElseGet(() -> categoryService.save(requestDto.pageId(), notionWorkspace));

        // notionPage insert
        NotionPage notionPage = NotionPage.builder()
                .notionAccount(notionAccount)
                .pageId(requestDto.pageId())
                .category(notionCategory)
                .build();
        notionPageJPARepository.save(notionPage);
    }
}
