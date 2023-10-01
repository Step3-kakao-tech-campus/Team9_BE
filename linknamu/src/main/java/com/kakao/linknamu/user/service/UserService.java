package com.kakao.linknamu.user.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu._core.redis.RedisExceptionStatus;
import com.kakao.linknamu._core.redis.service.BlackListTokenService;
import com.kakao.linknamu._core.redis.service.RefreshTokenService;
import com.kakao.linknamu._core.security.JwtProvider;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.LoginResponseDto;
import com.kakao.linknamu.user.dto.ReissueDto;
import com.kakao.linknamu.user.dto.oauth.OauthUserInfo;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final RefreshTokenService refreshTokenService;
    private final BlackListTokenService blackListTokenService;
    private final CategoryService categoryService;

    @Transactional
    public LoginResponseDto socialLogin(OauthUserInfo userInfo) {
        User user = userJPARepository.findByEmail(userInfo.email()).orElseGet(
                () -> {
                    User singUpUser = userJPARepository.save(User.builder()
                        .email(userInfo.email())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .role(Role.ROLE_USER)
                        .provider(userInfo.provider())
                        .build());

                    Category category = categoryService.save("root", null, singUpUser);
                    category.setRootCategory();

                    return singUpUser;
                });

        String accessToken = JwtProvider.create(user);
        String refreshToken = JwtProvider.createRefreshToken(user);

        refreshTokenService.save(refreshToken, accessToken, user);

        return LoginResponseDto.of(accessToken, refreshToken);
    }

    public ReissueDto.ReissueResponseDto reissue(ReissueDto.ReissueRequestDto requestDto) {
        String refreshToken = requestDto.refreshToken();

        // refresh 토큰으로 User정보 추출
        User user = getUserByRefreshToken(refreshToken);

        // refresh 토큰이 redis에 있는지 확인(유효한지 확인)
        checkRefreshTokenInRedis(refreshToken);

        String newAccessToken = JwtProvider.create(user);
        String newRefreshToken = JwtProvider.createRefreshToken(user);
        refreshTokenService.deleteById(refreshToken);
        refreshTokenService.save(newRefreshToken, newAccessToken, user);
        return ReissueDto.ReissueResponseDto.of(newAccessToken, newRefreshToken);
    }


    public void logout(String accessToken) {
        refreshTokenService.deleteByAccessToken(accessToken);
        blackListTokenService.save(accessToken);
    }

    @Transactional
    public void withdrawal(User user, String accessToken) {
        userJPARepository.findById(user.getUserId()).ifPresentOrElse(
                userJPARepository::delete,
                () -> {throw new Exception404(UserExceptionStatus.USER_NOT_FOUND);}
        );
        refreshTokenService.deleteByAccessToken(accessToken);
        blackListTokenService.save(accessToken);
    }

    // ---- private ----

    private void checkRefreshTokenInRedis(String refreshToken) {
        if(!refreshTokenService.existsById(refreshToken)) {
            throw new Exception404(RedisExceptionStatus.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    private User getUserByRefreshToken(String refreshToken) {
        try{
            DecodedJWT decodedJWT = JwtProvider.verifyRefreshToken(refreshToken);
            String email = decodedJWT.getClaim("email").asString();
            Long id = decodedJWT.getClaim("id").asLong();
            Role role = decodedJWT.getClaim("role").as(Role.class);
            return User.builder().userId(id).email(email).role(role).build();
        } catch (SignatureVerificationException | JWTDecodeException e) {
            log.error(e.getMessage());
            throw new Exception400(UserExceptionStatus.REFRESH_TOKEN_INVALID);
        } catch (TokenExpiredException tee) {
            throw new Exception400(UserExceptionStatus.REFRESH_TOKEN_EXPIRED);
        }
    }
}
