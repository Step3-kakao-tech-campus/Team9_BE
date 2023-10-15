package com.kakao.linknamu._core;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisContainerExtension implements BeforeAllCallback, AfterEachCallback {
    private static final int TEST_REDIS_PORT = 6379;
    private static final String DOCKER_REDIS_IMAGE = "redis:alpine";

    private static final GenericContainer<?> MY_REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(DOCKER_REDIS_IMAGE))
                    .withExposedPorts(TEST_REDIS_PORT)
                    .withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        MY_REDIS_CONTAINER.start();
        System.setProperty("spring.data.redis.host", MY_REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", MY_REDIS_CONTAINER.getMappedPort(TEST_REDIS_PORT).toString());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        // 테스트할 때마다 이전의 테스트 데이터는 삭제한다.
        MY_REDIS_CONTAINER.execInContainer("redis-cli", "flushall");
    }


}
