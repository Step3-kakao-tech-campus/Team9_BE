package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WorkspaceUpdateServiceTest {
//	@InjectMocks
//	private WorkspaceUpdateService workspaceUpdateService;
//
//	@Mock
//	private WorkspaceJpaRepository workspaceJPARepository;
//
//	@DisplayName("워크스페이스 수정 테스트")
//	@Nested
//	class UpdateTest {
//		@DisplayName("작성자가 올바른 입력을 할 경우 성공한다")
//		@Test
//		void sucecss() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.user(user)
//				.workspaceName("test")
//				.linkProvider(LinkProvider.NORMAL)
//				.build();
//			String rename = "modi_test";
//			WorkspaceUpdateRequestDto requestDto = new WorkspaceUpdateRequestDto(rename);
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.of(workspace));
//
//			// when
//			workspaceUpdateService.updateWorkspaceName(1L, requestDto, user);
//
//			// then
//			assertEquals(rename, workspace.getWorkspaceName());
//		}
//
//		@DisplayName("DB에 없는 워크스페이스ID를 입력할 경우 예외를 발생시킨다")
//		@Test
//		void fail_workspace_not_found() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.user(user)
//				.workspaceName("test")
//				.linkProvider(LinkProvider.NORMAL)
//				.build();
//			String rename = "modi_test";
//			WorkspaceUpdateRequestDto requestDto = new WorkspaceUpdateRequestDto(rename);
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.empty());
//
//			// when
//			Throwable exception = assertThrows(Exception404.class,
//				() -> workspaceUpdateService.updateWorkspaceName(1L, requestDto, user));
//
//			// then
//			assertEquals(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND.getMessage(), exception.getMessage());
//		}
//
//		@DisplayName("작성자가 아닌 사용자라면 예외를 발생시킨다")
//		@Test
//		void fail_request_not_writer() {
//			// given
//			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
//			User anotherUser = getDummeyUser(2L, "rjsdnxogh@naver.com");
//			Workspace workspace = Workspace.builder()
//				.id(1L)
//				.user(user)
//				.workspaceName("test")
//				.linkProvider(LinkProvider.NORMAL)
//				.build();
//			String rename = "modi_test";
//			WorkspaceUpdateRequestDto requestDto = new WorkspaceUpdateRequestDto(rename);
//
//			// mock
//			given(workspaceJPARepository.findById(workspace.getId())).willReturn(Optional.of(workspace));
//
//			// when
//			Throwable exception = assertThrows(Exception403.class,
//				() -> workspaceUpdateService.updateWorkspaceName(1L, requestDto, anotherUser));
//
//			// then
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
