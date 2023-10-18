package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WorkspaceSaveServiceTest {
    @InjectMocks
    private WorkspaceSaveService workspaceSaveService;

    @Mock
    private WorkspaceJPARepository workspaceJPARepository;

    @DisplayName("워크스페이스 생성 서비스 테스트")
    @Nested
    class WorkspaceCreateTest {
        @DisplayName("중복없는 올바른 워크스페이스 정보를 입력하면 통과한다")
        @Test
        void success() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "새로운 워크스페이스";
            Workspace workspace = Workspace.builder().id(1L).workspaceName(workspaceName).user(user).build();
            ArgumentCaptor<Workspace> workspaceCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(any(Long.class), any(String.class))).willReturn(Optional.empty());
            given(workspaceJPARepository.save(any())).willReturn(workspace);

            // when
            workspaceSaveService.createWorkspace(workspaceName, user);

            // then
            verify(workspaceJPARepository, times(1)).save(workspaceCaptor.capture());
            assertEquals(workspaceName, workspaceCaptor.getValue().getWorkspaceName());
            assertEquals(user, workspaceCaptor.getValue().getUser());
        }

        @DisplayName("중복된 워크스페이스 이름이 있을 경우 예외를 발생시킨다")
        @Test
        void failDuplicatedData() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "새로운 워크스페이스";
            Workspace workspace = Workspace.builder().id(1L).workspaceName(workspaceName).user(user).build();
            ArgumentCaptor<Workspace> workspaceCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(any(Long.class), any(String.class))).willReturn(Optional.of(workspace));

            // when
            Throwable exception = assertThrows(Exception400.class, () -> workspaceSaveService.createWorkspace(workspaceName, user));

            // then
            assertEquals(WorkspaceExceptionStatus.WORKSPACE_DUPLICATED.getMessage(), exception.getMessage());
        }
    }

    @DisplayName("노션 연동 워크스페이스 생성 테스트")
    @Nested
    class NotionWorkspaceCreateTest {
        @DisplayName("노션 워크스페이스 제목이 있는 디비에 있다면 노션 연동 워크스페이스로 변환한다.")
        @Test
        void success_notion_in_db() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "노션 워크스페이스";
            Workspace workspace = Workspace.builder()
                    .id(1L)
                    .workspaceName(workspaceName)
                    .user(user)
                    .build();

            ArgumentCaptor<Workspace> workspaceArgumentCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName))
                    .willReturn(Optional.of(workspace));

            // when
            workspaceSaveService.createNotionWorkspace(workspaceName, user);

            // then
            verify(workspaceJPARepository, times(1)).saveAndFlush(workspaceArgumentCaptor.capture());
            assertEquals(LinkProvider.NOTION, workspaceArgumentCaptor.getValue().getLinkProvider());
        }

        @DisplayName("올바른 데이터를 입력하면 노션 연동 워크스페이스가 생성된다")
        @Test
        void success_notion_not_in_db() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "노션 워크스페이스";

            ArgumentCaptor<Workspace> workspaceArgumentCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName))
                    .willReturn(Optional.empty());

            // when
            workspaceSaveService.createNotionWorkspace(workspaceName, user);

            // then
            verify(workspaceJPARepository, times(1)).saveAndFlush(workspaceArgumentCaptor.capture());
            assertEquals(LinkProvider.NOTION, workspaceArgumentCaptor.getValue().getLinkProvider());
            assertEquals(workspaceName, workspaceArgumentCaptor.getValue().getWorkspaceName());
        }
    }

    @DisplayName("구글 연동 워크스페이스 생성 테스트")
    @Nested
    class GoogleWorkspaceCreateTest {
        @DisplayName("구글 워크스페이스 제목이 있는 디비에 있다면 구글 연동 워크스페이스로 변환한다.")
        @Test
        void success_google_in_db() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "구글 워크스페이스";
            Workspace workspace = Workspace.builder()
                    .id(1L)
                    .workspaceName(workspaceName)
                    .user(user)
                    .build();

            ArgumentCaptor<Workspace> workspaceArgumentCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName))
                    .willReturn(Optional.of(workspace));

            // when
            workspaceSaveService.createDocsWorkspace(workspaceName, user);

            // then
            verify(workspaceJPARepository, times(1)).saveAndFlush(workspaceArgumentCaptor.capture());
            assertEquals(LinkProvider.GOOGLE_DOCS, workspaceArgumentCaptor.getValue().getLinkProvider());
        }

        @DisplayName("올바른 데이터를 입력하면 구글 연동 워크스페이스가 생성된다")
        @Test
        void success_google_not_in_db() {
            // given
            User user = getDummyUser(1L, "rjsdnxogh@naver.com");
            String workspaceName = "구글 워크스페이스";

            ArgumentCaptor<Workspace> workspaceArgumentCaptor = ArgumentCaptor.forClass(Workspace.class);

            // mock
            given(workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName))
                    .willReturn(Optional.empty());

            // when
            workspaceSaveService.createDocsWorkspace(workspaceName, user);

            // then
            verify(workspaceJPARepository, times(1)).saveAndFlush(workspaceArgumentCaptor.capture());
            assertEquals(LinkProvider.GOOGLE_DOCS, workspaceArgumentCaptor.getValue().getLinkProvider());
            assertEquals(workspaceName, workspaceArgumentCaptor.getValue().getWorkspaceName());
        }
    }

    private User getDummyUser(Long id, String email) {
        return User.builder()
                .userId(id)
                .email(email)
                .build();
    }
}
