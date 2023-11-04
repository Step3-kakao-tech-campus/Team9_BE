package com.kakao.linknamu.workspace.controller;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.core.RestDocs;
import com.kakao.linknamu.core.security.JwtProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import com.kakao.linknamu.workspace.dto.WorkspaceCreateRequestDto;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import com.kakao.linknamu.workspace.service.WorkspaceDeleteService;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import com.kakao.linknamu.workspace.service.WorkspaceUpdateService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
public class WorkspaceControllerTest extends RestDocs {
	@Autowired
	private WorkspaceReadService workspaceReadService;
	@Autowired
	private WorkspaceSaveService workspaceSaveService;
	@Autowired
	private WorkspaceDeleteService workspaceDeleteService;
	@Autowired
	private WorkspaceUpdateService workspaceUpdateService;
	@Autowired
	private WorkspaceJPARepository workspaceJPARepository;
	@Autowired
	private UserJpaRepository userJPARepository;
	@Autowired
	private CategoryJpaRepository categoryJPARepository;
	@Autowired
	private EntityManager em;

	private static final String TEST_USER_EMAIL = "rjsdnxogh55@gmail.com";

	@BeforeEach
	void setup() {
		User user = User.builder()
			.email(TEST_USER_EMAIL)
			.provider(Provider.PROVIDER_GOOGLE)
			.role(Role.ROLE_USER)
			.password("testPassword")
			.build();
		userJPARepository.save(user);

		em.createNativeQuery("ALTER TABLE workspace_tb ALTER COLUMN `workspace_id` RESTART WITH 1")
			.executeUpdate();
		em.createNativeQuery("ALTER TABLE category_tb ALTER COLUMN `category_id` RESTART WITH 1")
			.executeUpdate();
	}

	@DisplayName("워크스페이스 리스트 조회 테스트")
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Transactional
	@Nested
	class GetWorkspaceListTest {
		@DisplayName("성공")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL).get();

			List<Workspace> workspaceList = List.of(
				Workspace.builder().workspaceName("test1").linkProvider(LinkProvider.NORMAL).user(user).build(),
				Workspace.builder().workspaceName("test2").linkProvider(LinkProvider.NORMAL).user(user).build(),
				Workspace.builder().workspaceName("test3").linkProvider(LinkProvider.NORMAL).user(user).build(),
				Workspace.builder().workspaceName("test4").linkProvider(LinkProvider.NORMAL).user(user).build(),
				Workspace.builder().workspaceName("test5").linkProvider(LinkProvider.NORMAL).user(user).build()
			);
			workspaceJPARepository.saveAll(workspaceList);

			List<Category> categorieList = List.of(
				Category.builder().categoryName("category1").workspace(workspaceList.get(0)).build(),
				Category.builder().categoryName("category2").workspace(workspaceList.get(0)).build(),
				Category.builder().categoryName("category3").workspace(workspaceList.get(1)).build(),
				Category.builder().categoryName("category4").workspace(workspaceList.get(3)).build(),
				Category.builder().categoryName("category5").workspace(workspaceList.get(3)).build(),
				Category.builder().categoryName("category6").workspace(workspaceList.get(4)).build()
			);
			categoryJPARepository.saveAll(categorieList);

			// em.clear를 안해주면 category가 영속성 컨텍스트 위에 올라가 있어 데이터를 가져오지 못한다.
			em.clear();
			String accessToken = JwtProvider.create(user);

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.get("/api/workspace/list")
					.header(JwtProvider.HEADER, accessToken)
			);

			//            String response = resultActions.andReturn().getResponse().getContentAsString();
			//            System.out.println(response);

			// then
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response[0].workspaceId").value(1),
				jsonPath("$.response[0].categoryList[0].categoryId").value(1),
				jsonPath("$.response[0].categoryList[1].categoryId").value(2),
				jsonPath("$.response[1].workspaceId").value(2),
				jsonPath("$.response[1].categoryList[0].categoryId").value(3),
				jsonPath("$.response[2].workspaceId").value(3),
				jsonPath("$.response[2].categoryList").isEmpty(),
				jsonPath("$.response[3].workspaceId").value(4),
				jsonPath("$.response[3].categoryList[0].categoryId").value(4),
				jsonPath("$.response[3].categoryList[1].categoryId").value(5),
				jsonPath("$.response[4].workspaceId").value(5),
				jsonPath("$.response[4].categoryList[0].categoryId").value(6),
				jsonPath("$.error").doesNotExist()
			);
		}
	}

	@DisplayName("워크스페이스 생성 테스트")
	@Transactional
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Nested
	class createWorkspaceTest {
		@DisplayName("성공")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL).get();
			WorkspaceCreateRequestDto requestDto = new WorkspaceCreateRequestDto("test");
			String request = om.writeValueAsString(requestDto);
			String accessToken = JwtProvider.create(user);

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post("/api/workspace/create")
					.header(JwtProvider.HEADER, accessToken)
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
			);
			String response = resultActions.andReturn().getResponse().getContentAsString();

			List<Workspace> workspaceList = workspaceJPARepository.findAllByUserId(user.getUserId());

			// then
			assertEquals(1, workspaceList.size());
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response").doesNotExist(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}

	@DisplayName("워크스페이스 수정 테스트")
	@Transactional
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Nested
	class UpdateWorkspaceTest {
		@DisplayName("성공")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL).get();
			Workspace workspace = Workspace.builder()
				.workspaceName("test")
				.linkProvider(LinkProvider.NORMAL)
				.user(user)
				.build();
			workspaceJPARepository.save(workspace);

			WorkspaceUpdateRequestDto requestDto = new WorkspaceUpdateRequestDto("modified");
			String request = om.writeValueAsString(requestDto);
			String accessToken = JwtProvider.create(user);

			em.clear();
			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post(
						"/api/workspace/update/{workspace_id}",
						workspace.getId())
					.header(JwtProvider.HEADER, accessToken)
					.content(request)
					.contentType(MediaType.APPLICATION_JSON)
			);
			String response = resultActions.andReturn().getResponse().getContentAsString();

			Workspace modiWorkspace = workspaceJPARepository.findById(workspace.getId()).get();

			// then
			assertEquals("modified", modiWorkspace.getWorkspaceName());
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response").doesNotExist(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}

	@DisplayName("워크스페이스 삭제 테스트")
	@Transactional
	@WithUserDetails(value = TEST_USER_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Nested
	class DeleteWorkspaceTest {
		@DisplayName("성공")
		@Test
		void success() throws Exception {
			// given
			User user = userJPARepository.findByEmail(TEST_USER_EMAIL).get();
			Workspace workspace = Workspace.builder()
				.workspaceName("test")
				.linkProvider(LinkProvider.NORMAL)
				.user(user)
				.build();
			workspaceJPARepository.save(workspace);

			String accessToken = JwtProvider.create(user);

			// when
			ResultActions resultActions = mvc.perform(
				RestDocumentationRequestBuilders.post(
						"/api/workspace/delete/{workspace_id}",
						workspace.getId())
					.header(JwtProvider.HEADER, accessToken)
			);
			String response = resultActions.andReturn().getResponse().getContentAsString();

			Optional<Workspace> findWorkspace = workspaceJPARepository.findById(workspace.getId());

			// then
			assertTrue(findWorkspace.isEmpty());
			resultActions.andExpectAll(
				jsonPath("$.success").value("true"),
				jsonPath("$.response").doesNotExist(),
				jsonPath("$.error").doesNotExist()
			);
		}
	}
}
