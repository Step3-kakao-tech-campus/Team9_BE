package com.kakao.linknamu.core.security;

import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserJpaRepository userJpaRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return new CustomUserDetails(userJpaRepository.findByEmail(email)
			.orElseThrow(() -> new Exception404(UserExceptionStatus.USER_NOT_FOUND)));
	}
}
