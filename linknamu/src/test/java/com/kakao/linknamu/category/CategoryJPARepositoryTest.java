package com.kakao.linknamu.category;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryJPARepositoryTest {

    @Autowired
    private CategoryJPARepository categoryJPARepository;
    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private WorkspaceJPARepository workspaceJPARepository;

    @Test
    @DisplayName("카테고리_생성_테스트")
    public void CategorySaveTest(){
        //given
        User user = User.builder()
                .email("grindabff@pusan.ac.kr")
                .password("password")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Workspace workspace = Workspace.builder()
                .workspaceName("Workspace")
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();
        workspace = workspaceJPARepository.save(workspace);

        Category category = Category.builder()
                .categoryName("Category")
                .workspace(workspace)
                .build();

        //when
        Category savedCategory = categoryJPARepository.save(category);

        //then
        assertThat(savedCategory.getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(savedCategory.getWorkspace().getWorkspaceName()).isEqualTo(workspace.getWorkspaceName());
    }

    @Test
    @DisplayName("카테고리_조회_테스트")
    public void CategoryFindByIdTest(){
        //given
        User user = User.builder()
                .email("grindabff@pusan.ac.kr")
                .password("password")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Workspace workspace = Workspace.builder()
                .workspaceName("Workspace")
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();
        workspace = workspaceJPARepository.save(workspace);

        Category category = Category.builder()
                .categoryName("Category")
                .workspace(workspace)
                .build();
        category = categoryJPARepository.save(category);

        //when
        Category found = categoryJPARepository.findById(category.getCategoryId()).get();

        //then
        assertThat(found.getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(found.getWorkspace().getWorkspaceName()).isEqualTo(workspace.getWorkspaceName());
    }

}
