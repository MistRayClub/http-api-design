package org.kaws.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.kaws.common.reponse.R;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;

/**
 * @author Bosco
 * @date 2022/4/13 4:54 下午
 */


@Slf4j
@Configuration
public class FallbackConfig {

    @Bean("defaultFallbackHandler")
    public HandlerFunction<ServerResponse> defaultFallbackHandler() {
        return serverRequest -> {
            String path = serverRequest.exchange().getRequest().getPath().value();
            log.error("path: {} frequently requested, please try again in a minute", path);
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(R.failure(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())));
        };
    }


    @Bean
    public RouterFunction<ServerResponse> fallbackRouter(HandlerFunction<ServerResponse> defaultFallbackHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/gateway/defaultFallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), defaultFallbackHandler);
    }

}
