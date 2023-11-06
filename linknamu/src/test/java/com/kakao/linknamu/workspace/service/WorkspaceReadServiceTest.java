package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.workspace.dto.WorkspaceGetResponseDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WorkspaceReadServiceTest {
	@InjectMocks
	private WorkspaceReadService workspaceReadService;

	@Mock
	private WorkspaceJpaRepository workspaceJPARepository;

	@DisplayName("워크스페이스 목록 조회 테스트")
	@Nested
	class GetWorkspaceListTest {
		@DisplayName("사용자가 등록한 워크스페이스 목록을 보여준다")
		@Test
		void success() {
			// given
			User user = getDummeyUser(1L, "rjsdnxogh55@gmail.com");
			Workspace workspace = Workspace.builder()
				.id(1L)
				.user(user)
				.workspaceName("test")
				.linkProvider(LinkProvider.NORMAL)
				.build();

			Workspace workspace2 = Workspace.builder()
				.id(2L)
				.user(user)
				.workspaceName("test2")
				.linkProvider(LinkProvider.NORMAL)
				.build();

			// mock
			given(workspaceJPARepository.findAllByUserId(user.getUserId())).willReturn(List.of(
				workspace,
				workspace2
			));

			// when
			List<WorkspaceGetResponseDto> resultDtos = workspaceReadService.getWorkspaceList(user);

			// then
			assertEquals("test", resultDtos.get(0).workspaceName());
			assertEquals("test2", resultDtos.get(1).workspaceName());
		}
	}

	private User getDummeyUser(Long id, String email) {
		return User.builder()
			.userId(id)
			.email(email)
			.role(Role.ROLE_USER)
			.build();
	}
}
