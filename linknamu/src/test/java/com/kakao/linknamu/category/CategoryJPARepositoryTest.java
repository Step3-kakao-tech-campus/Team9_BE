package com.kakao.linknamu.category;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CategoryJPARepositoryTest {

    @Autowired
    private CategoryJPARepository categoryJPARepository;
    @Autowired
    private UserJPARepository userJPARepository;

    @Test
    @DisplayName("카테고리_생성_테스트")
    public void CategorySaveTest(){
        //given
        // create user
        User user = User.builder()
                .email("grindabff@pusan.ac.kr")
                .password("password")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);
        // create root category
        Category category = Category.builder()
                .categoryName("Category")
                .parentCategory(null)
                .user(user)
                .build();

        //when
        Category saved = categoryJPARepository.save(category);

        //then
        assertThat(saved.getCategoryName()).isEqualTo(category.getCategoryName());
    }

    @Test
    @DisplayName("카테고리_조회_테스트")
    public void CategoryFindByIdTest(){
        //given
        // create user
        User user = User.builder()
                .email("grindabff@pusan.ac.kr")
                .password("password")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);
        // create root category
        Category rootCategory = Category.builder()
                .categoryName("Root Category")
                .parentCategory(null)
                .user(user)
                .build();
        rootCategory = categoryJPARepository.save(rootCategory);
        // create category
        Category category = Category.builder()
                .categoryName("Category1")
                .parentCategory(rootCategory)
                .user(user)
                .build();
        category = categoryJPARepository.save(category);

        //when
        Category found = categoryJPARepository.findById(category.getCategoryId()).orElse(null);

        //then
        assertThat(found.getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(found.getParentCategory()).isEqualTo(rootCategory);
    }

}
