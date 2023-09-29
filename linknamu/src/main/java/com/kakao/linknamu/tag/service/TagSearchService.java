package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagSearchService {
    private final TagJPARepository tagJPARepository;

    public Optional<Tag> searchByTagName(String name) {
        return tagJPARepository.findByName(name);
    }
}
