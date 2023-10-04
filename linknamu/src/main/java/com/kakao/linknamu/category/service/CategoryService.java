package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJPARepository categoryJPARepository;

    public Category save(String categoryName, Workspace workspace){
        Category category = Category.builder()
                .categoryName(categoryName)
                .workspace(workspace)
                .build();
        return categoryJPARepository.save(category);
    }

    public Category findById(Long id) {
        return categoryJPARepository.findById(id).orElseThrow(
                () -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
        );
    }

//    public Page<Category> findByUserId(Pageable pageable, User user){
//        return categoryJPARepository.findByUserId(user.getUserId(), pageable);
//    }

//    public Page<Category> findByParentCategoryId(Pageable pageable, Category parentCategory){
//        return categoryJPARepository.findByParentCategoryId(parentCategory.getCategoryId(), pageable);
//    }
//
    public Optional<Category> findByWorkspaceIdAndCategoryName(Long workspaceId, String categoryName){
        return categoryJPARepository.findByWorkspaceIdAndCategoryName(workspaceId, categoryName);
    }

    public void deleteById(Long categoryId){
        categoryJPARepository.deleteById(categoryId);
    }

    // 워크스페이스의 유저와 로그인 유저가 같은지 체크
    public void validUser(Workspace workspace, User user){
        if (!workspace.getUser().getUserId().equals(user.getUserId())){
            throw new Exception403(CategoryExceptionStatus.CATEGORY_FORBIDDEN);
        }
    }

}
