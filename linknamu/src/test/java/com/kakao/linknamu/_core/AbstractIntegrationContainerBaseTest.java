package com.kakao.linknamu._core;

import com.kakao.linknamu._core.redis.service.RefreshTokenService;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationContainerBaseTest {
    private static final int TEST_REDIS_PORT = 6379;
    private static final String DOCKER_REDIS_IMAGE = "redis:6.0.20-alpine";

    @Container
    private static final GenericContainer<?> MY_REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(DOCKER_REDIS_IMAGE))
                    .withExposedPorts(TEST_REDIS_PORT);

    @Autowired
    private RefreshTokenService refreshTokenService;

    static{
        MY_REDIS_CONTAINER.start();
    }

    @AfterEach
    void teardown() throws IOException, InterruptedException {
        // 테스트할 때마다 이전의 테스트 데이터는 삭제한다.
        MY_REDIS_CONTAINER.execInContainer("redis-cli", "flushall");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", MY_REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> MY_REDIS_CONTAINER.getMappedPort(TEST_REDIS_PORT).toString());
    }

//    @Test
//    void redisTest2() {
//        User user = User.builder()
//                .userId(1L)
//                .email("rjsdnxogh@naver.com")
//                .provider(Provider.PROVIDER_GOOGLE)
//                .role(Role.ROLE_USER)
//                .build();
//        refreshTokenService.save("5453", "2313", user);
//
//        assertFalse(refreshTokenService.existsById("123"));
//    }
//
//    @Test
//    void redisTest1() {
//        User user = User.builder()
//                .userId(1L)
//                .email("rjsdnxogh@naver.com")
//                .provider(Provider.PROVIDER_GOOGLE)
//                .role(Role.ROLE_USER)
//                .build();
//
//        refreshTokenService.save("123", "2313", user);
//    }
}
