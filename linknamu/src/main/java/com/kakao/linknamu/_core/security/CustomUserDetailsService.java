package com.kakao.linknamu._core.security;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(userJPARepository.findByEmail(email)
                .orElseThrow(() -> new Exception404(UserExceptionStatus.USER_NOT_FOUND)));
    }
}
