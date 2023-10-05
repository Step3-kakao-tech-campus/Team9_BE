package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.dto.WorkspaceGetResponseDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceReadService {
    private final WorkspaceJPARepository workspaceJPARepository;

    public List<WorkspaceGetResponseDto> getWorkspaceList(User user) {
        List<Workspace> workspaceList = workspaceJPARepository.findAllByUserIdFetchJoinCategory(user.getUserId());
        if (workspaceList.isEmpty()) return List.of();

        return workspaceList.stream()
                .map(WorkspaceGetResponseDto::of)
                .toList();
    }
}
