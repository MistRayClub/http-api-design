package org.kaws.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Bosco
 * @date 2022/4/12 11:13 下午
 */

@Configuration
public class RateLimiterConfig {

    @Bean(value = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

}
