package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagDeleteService {
    private final TagJPARepository tagJPARepository;

    public void deleteTagById(Long id) {
        Tag tag = tagJPARepository.findById(id).orElseThrow(
                // 예외처리
        );
        tagJPARepository.delete(tag);
    }
}
