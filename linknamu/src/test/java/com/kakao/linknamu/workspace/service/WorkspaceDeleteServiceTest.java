package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceDeleteServiceTest {
//	@InjectMocks
//	private WorkspaceDeleteService workspaceDeleteService;
//
//	@Mock
//	private WorkspaceJpaRepository workspaceJPARepository;
//
//	@DisplayName("워크스페이스 삭제 테스트")
//	@Nested
//	class DeleteTest {
//
//		@DisplayName("올바른 유저 아이디와 워크스페이스 아이디를 입력하면 삭제가 된다")
//		@Test
//		void success() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.workspaceName("테스트 워크스페이스")
//				.linkProvider(LinkProvider.NORMAL)
//				.user(user)
//				.build();
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.of(workspace));
//
//			// when
//			workspaceDeleteService.deleteWorkspace(workspace.getId(), user);
//
//			// then
//			verify(workspaceJPARepository, times(1)).delete(workspace);
//
//		}
//
//		@DisplayName("존재하지 않는 워크스페이스 아이디를 입력하면 예외를 발생시킨다.")
//		@Test
//		void fail_not_exist_workspace_id() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.workspaceName("테스트 워크스페이스")
//				.linkProvider(LinkProvider.NORMAL)
//				.user(user)
//				.build();
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.empty());
//
//			// when
//			Throwable exception = assertThrows(Exception404.class,
//				() -> workspaceDeleteService.deleteWorkspace(workspace.getId(), user));
//
//			// then
//			verify(workspaceJPARepository, times(0)).delete(workspace);
//			assertEquals(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND.getMessage(), exception.getMessage());
//		}
//
//		@DisplayName("작성자가 아닌 사용자가 요청 시 예외를 발생시킨다.")
//		@Test
//		void fail_forbidden_user_request() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			User anotherUser = getDummeyUser(2L, "rjsdnxogh12@pusan.ac.kr");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.workspaceName("테스트 워크스페이스")
//				.linkProvider(LinkProvider.NORMAL)
//				.user(user)
//				.build();
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.of(workspace));
//
//			// when
//			Throwable exception = assertThrows(Exception403.class,
//				() -> workspaceDeleteService.deleteWorkspace(workspace.getId(), anotherUser));
//
//			// then
//			verify(workspaceJPARepository, times(0)).delete(workspace);
//			assertEquals(WorkspaceExceptionStatus.WORKSPACE_FORBIDDEN.getMessage(), exception.getMessage());
//		}
//	}
//
//	private User getDummeyUser(Long id, String email) {
//		return User.builder()
//			.userId(id)
//			.email(email)
//			.role(Role.ROLE_USER)
//			.build();
//	}
}
