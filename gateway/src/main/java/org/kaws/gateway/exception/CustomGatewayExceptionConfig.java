package org.kaws.gateway.exception;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author Bosco
 * @date 2022/4/7 3:55 下午
 */

@Configuration
public class CustomGatewayExceptionConfig {

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        CustomGatewayExceptionHandler customGatewayExceptionHandler = new CustomGatewayExceptionHandler();
        customGatewayExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        customGatewayExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        customGatewayExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return customGatewayExceptionHandler;
    }

}
