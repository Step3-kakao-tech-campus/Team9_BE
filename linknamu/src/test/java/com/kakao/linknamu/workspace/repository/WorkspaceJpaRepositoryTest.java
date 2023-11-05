package com.kakao.linknamu.workspace.repository;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.core.TestConfig;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
@DataJpaTest
@Import(TestConfig.class)
public class WorkspaceJpaRepositoryTest {
	@Autowired
	private WorkspaceJPARepository workspaceJPARepository;
	@Autowired
	private UserJpaRepository userJPARepository;
	@Autowired
	private CategoryJpaRepository categoryJPARepository;
	@Autowired
	private EntityManager em;

	@BeforeEach
	void setup() {
		User user = User.builder()
			.email("rjsdnxogh55@gmail.com")
			.provider(Provider.PROVIDER_GOOGLE)
			.role(Role.ROLE_USER)
			.password("testpassword")
			.build();
		userJPARepository.save(user);
	}

	@DisplayName("findByUserIdAndWorkspaceName 테스트")
	@Test
	void findByUserIdAndWorkspaceNameTest() {
		// given
		User user = userJPARepository.findByEmail("rjsdnxogh55@gmail.com").orElseThrow();
		Workspace workspace = Workspace.builder()
			.workspaceName("워크스페이스")
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();
		workspaceJPARepository.save(workspace);

		Long findUserId = user.getUserId();
		String findWorkspaceName = "워크스페이스";
		// when
		Workspace findWorkspace = workspaceJPARepository.findByUserIdAndWorkspaceName(findUserId, findWorkspaceName)
			.orElseThrow();

		// then
		assertEquals(workspace, findWorkspace);
	}

	@DisplayName("워크스페이스 카테고리가 가져와지는지 테스트")
	@Test
	void workspaceCanTakeCategoryList() {
		// given
		User user = userJPARepository.findByEmail("rjsdnxogh55@gmail.com").orElseThrow();
		Workspace workspace1 = Workspace.builder()
			.workspaceName("워크스페이스1")
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();
		Workspace workspace2 = Workspace.builder()
			.workspaceName("워크스페이스2")
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();
		workspaceJPARepository.save(workspace1);
		workspaceJPARepository.save(workspace2);

		List<Category> categoryList = List.of(
			Category.builder().workspace(workspace1).categoryName("카테고리1").build(),
			Category.builder().workspace(workspace1).categoryName("카테고리2").build(),
			Category.builder().workspace(workspace1).categoryName("카테고리3").build(),
			Category.builder().workspace(workspace1).categoryName("카테고리4").build(),
			Category.builder().workspace(workspace1).categoryName("카테고리5").build(),
			Category.builder().workspace(workspace2).categoryName("카테고리6").build(),
			Category.builder().workspace(workspace2).categoryName("카테고리7").build(),
			Category.builder().workspace(workspace2).categoryName("카테고리8").build()
		);
		categoryJPARepository.saveAll(categoryList);
		em.clear();

		// when
		List<Workspace> findWorkspaces = workspaceJPARepository.findAllByUserId(user.getUserId());
		assertEquals(2, findWorkspaces.size());
		assertEquals(5, findWorkspaces.get(0).getCategorySet().size());
		assertEquals(3, findWorkspaces.get(1).getCategorySet().size());
	}

	@DisplayName("findAllByUserId 테스트")
	@Test
	void findAllByUserIdTest() {
		// given
		User user = userJPARepository.findByEmail("rjsdnxogh55@gmail.com").orElseThrow();
		Workspace workspace = Workspace.builder()
			.workspaceName("워크스페이스")
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();
		workspaceJPARepository.save(workspace);

		Long findUserId = user.getUserId();

		// when
		List<Workspace> findWorkspaces = workspaceJPARepository.findAllByUserId(findUserId);

		// then
		assertEquals(1, findWorkspaces.size());
	}
}
